package ar.nadezhda.vortex.core;

import static ar.nadezhda.vortex.core.Mask.*;
import ar.nadezhda.vortex.config.Scenario;
import ar.nadezhda.vortex.interfaces.Cluster;
import ar.nadezhda.vortex.interfaces.Geometry;
import ar.nadezhda.vortex.io.OccupationWriter;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>El autómata celular.</p>
	*
	* @author Agustín Golmar
	*/

public final class CellularAutomaton {

	private static final Logger log = LoggerFactory.getLogger(CellularAutomaton.class);

	private final int average;
	private final Cluster cluster;
	private final int height;
	private final short massInjection;
	private final short [] momentum;
	private final int [][][] occupation;
	private final OccupationWriter occupationFile;
	private final MersenneTwister prng;
	private final byte [] randomness;
	private final int ratio;
	private final Scenario scenario;
	private final int steps;
	private final short [] system;
	private final int width;
	private final int window;

	private short [][] lattice0;
	private short [][] lattice1;
	private int [] xSources;
	private int [] ySources;

	private CellularAutomaton(final Builder builder)
			throws FileNotFoundException {
		this.average = builder.average;
		this.cluster = builder.cluster;
		this.height = builder.height;
		this.massInjection = loadMassInjection(builder.scenario.getMomentum());
		this.momentum = loadMomentum(this.massInjection);
		this.occupation = new int [builder.width/builder.average][builder.height/builder.average][6];
		this.occupationFile = new OccupationWriter(builder.output);
		this.prng = new MersenneTwister(builder.seed);
		this.randomness = new byte [4 + builder.width * builder.height / 8];
		this.ratio = (int) (0xFFFFFFFFL * builder.scenario.getRatio() - 2147483648L);
		this.scenario = builder.scenario;
		this.steps = builder.steps;
		this.system = CollisionSystem.noSlipFHP_I();
		this.width = builder.width;
		this.window = builder.window;
		this.lattice0 = new short [builder.width][builder.height];
		this.lattice1 = new short [builder.width][builder.height];
		log.info("Loading scenario...");
		loadParticles();
		loadSinks();
		loadSolids();
		loadSources();
		log.info("Automaton ready.");
	}

	public void evolve() throws IOException {
		log.info("Evolving...");
		final int rate = scenario.getRate();
		final float threshold = steps /
			((steps < 1000)? ((steps < 100)? 10 : 100) : 1000);
		for (int k = 0; k < steps; ++k) {
			prng.nextBytes(randomness);
			if (k % rate == 0) {
				injectMass();
				cluster.compute(this::momentum);
			}
			cluster.compute(this::collide);
			cluster.compute(this::propagate);
			swap();
			cluster.compute(this::occupation);
			if (k % threshold == 0) {
				log.info("{}% completed.", 100.0f*k/steps);
			}
			if (k % window == 0) {
				occupationFile.saveSample(occupation);
				clearOccupation();
			}
		}
		occupationFile.saveSample(occupation);
		log.info("Releasing cluster and resources...");
		cluster.release();
		occupationFile.close();
	}

	private void clearOccupation() {
		for (int w = 0; w < occupation.length; ++w) {
			for (int h = 0; h < occupation[0].length; ++h) {
				for (int v = 0; v < 6; ++v) {
					occupation[w][h][v] = 0;
				}
			}
		}
	}

	private void collide(final int x, final int y) {
		lattice0[x][y] = system[lattice0[x][y]];
	}

	private void injectMass() {
		for (int k = 0; k < xSources.length; ++k) {
			lattice0[xSources[k]][ySources[k]] |= massInjection;
		}
	}

	private void momentum(final int x, final int y) {
		if (prng.nextInt() < ratio) {
			lattice0[x][y] = momentum[lattice0[x][y]];
		}
	}

	private void loadParticles() {
		for (int w = 0; w < width; ++w) {
			for (int h = 0; h < height; ++h) {
				lattice0[w][h] = (short) prng.nextInt(64);
			}
		}
	}

	private void loadSinks() {
		for (final Geometry geometry : scenario.getSink()) {
			for (final Point point : geometry.points()) {
				lattice0[point.x][point.y] |= SINK;
			}
		}
	}

	private void loadSolids() {
		for (final Geometry geometry : scenario.getSolid()) {
			for (final Point point : geometry.points()) {
				lattice0[point.x][point.y] |= SOLID;
			}
		}
	}

	private void loadSources() {
		final List<Point []> geometries = new ArrayList<>();
		int size = 0;
		for (final Geometry geometry : scenario.getSource()) {
			final Point [] points = geometry.points();
			geometries.add(points);
			size += points.length;
		}
		xSources = new int [size];
		ySources = new int [size];
		int k = 0;
		for (final Point [] geometry : geometries) {
			for (int i = 0; i < geometry.length; ++i) {
				xSources[k] = geometry[i].x;
				ySources[k] = geometry[i].y;
				++k;
			}
		}
	}

	private boolean nextBit(final int x, final int y) {
		final int bit = x * height + y;
		return 0 == ((randomness[bit >>> 3] >>> (bit & 0x07)) & 0x01);
	}

	private void occupation(final int x, final int y) {
		final int w = x/average;
		final int h = y/average;
		final short node = lattice0[x][y];
		occupation[w][h][0] += node & 0x0001;
		occupation[w][h][1] += (node >>> 1) & 0x0001;
		occupation[w][h][2] += (node >>> 2) & 0x0001;
		occupation[w][h][3] += (node >>> 3) & 0x0001;
		occupation[w][h][4] += (node >>> 4) & 0x0001;
		occupation[w][h][5] += (node >>> 5) & 0x0001;
	}

	private void propagate(final int x, final int y) {
		final int parity = y & 0x00000001;
		final int down = wrapOnHeight(y - 1);
		final int leftWithParity = wrapOnWidth(x - parity - 1);
		final int rightWithParity = wrapOnWidth(x + parity);
		final int up = wrapOnHeight(y + 1);
		lattice1[x][y] = (short) (
			(nextBit(x, y)? 0 : RANDOM)
			| ((SINK | SOLID) & lattice0[x][y])
			| (A & lattice0[wrapOnWidth(x - 1)][y])
			| (B & lattice0[leftWithParity][down])
			| (C & lattice0[rightWithParity][down])
			| (D & lattice0[wrapOnWidth(x + 1)][y])
			| (E & lattice0[rightWithParity][up])
			| (F & lattice0[leftWithParity][up])
		);
	}

	private void swap() {
		final short [][] temp = lattice0;
		lattice0 = lattice1;
		lattice1 = temp;
	}

	private int wrapOnHeight(final int y) {
		return y + height * ((y >>> 31) - (height - y - 1 >>> 31));
	}

	private int wrapOnWidth(final int x) {
		return x + width * ((x >>> 31) - (width - x - 1 >>> 31));
	}

	private static short loadMassInjection(final String [] directions) {
		short massInjection = 0;
		for (final String direction : directions) {
			switch (direction) {
				case "A": massInjection |= A; break;
				case "B": massInjection |= B; break;
				case "C": massInjection |= C; break;
				case "D": massInjection |= D; break;
				case "E": massInjection |= E; break;
				case "F": massInjection |= F; break;
				default: break;
			}
		}
		return massInjection;
	}

	private static short [] loadMomentum(final short massInjection) {
		final short [] momentum = new short [512];
		for (int k = 0; k < momentum.length; ++k) {
			short injectable = (short) ((~k) & massInjection);
			short movable = (short) (k & (~massInjection) & MASS);
			final int available = Integer.bitCount(movable);
			momentum[k] = (short) k;
			for (int i = 0; i < available; ++i) {
				final short in = (short) Integer.lowestOneBit(injectable);
				if (in == 0) break;
				final short out = (short) Integer.lowestOneBit(movable);
				momentum[k] |= in;
				momentum[k] &= ~out;
				injectable &= ~in;
				movable &= ~out;
			}
		}
		return momentum;
	}

	public static final class Builder {

		private int average;
		private Cluster cluster;
		private int height;
		private String output;
		private Scenario scenario;
		private long seed;
		private int steps;
		private int width;
		private int window;

		public CellularAutomaton build()
				throws FileNotFoundException {
			return new CellularAutomaton(this);
		}

		public Builder average(final int average) {
			this.average = average;
			return this;
		}

		public Builder cluster(final Cluster cluster) {
			this.cluster = cluster;
			return this;
		}

		public Builder height(final int height) {
			this.height = height;
			return this;
		}

		public Builder output(final String output) {
			this.output = output;
			return this;
		}

		public Builder scenario(final Scenario scenario) {
			this.scenario = scenario;
			return this;
		}

		public Builder seed(final long seed) {
			this.seed = seed;
			return this;
		}

		public Builder steps(final int steps) {
			this.steps = steps;
			return this;
		}

		public Builder width(final int width) {
			this.width = width;
			return this;
		}

		public Builder window(final int window) {
			this.window = window;
			return this;
		}
	}
}
