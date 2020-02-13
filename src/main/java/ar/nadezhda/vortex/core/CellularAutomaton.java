package ar.nadezhda.vortex.core;

import static ar.nadezhda.vortex.core.Mask.*;
import ar.nadezhda.vortex.config.Scenario;
import ar.nadezhda.vortex.interfaces.Cluster;
import ar.nadezhda.vortex.interfaces.Geometry;
import java.awt.Point;
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

	private final Cluster cluster;
	private final int height;
	private final short massInjection;
	private final short [] momentum;
	private final MersenneTwister prng;
	private final byte [] randomness;
	private final int ratio;
	private final Scenario scenario;
	private final int steps;
	private final short [] system;
	private final int width;

	private short [][] lattice0;
	private short [][] lattice1;
	private int [] xSources;
	private int [] ySources;

	private CellularAutomaton(final Builder builder) {
		this.cluster = builder.cluster;
		this.height = builder.height;
		this.massInjection = loadMassInjection(builder.scenario.getMomentum());
		this.momentum = loadMomentum(this.massInjection);
		this.prng = new MersenneTwister(builder.seed);
		this.randomness = new byte [4 + builder.width * builder.height / 8];
		this.ratio = (int) (0xFFFFFFFFL * builder.scenario.getRatio() - 2147483648L);
		this.scenario = builder.scenario;
		this.steps = builder.steps;
		this.system = CollisionSystem.noSlipFHP_I();
		this.width = builder.width;
		this.lattice0 = new short [builder.width][builder.height];
		this.lattice1 = new short [builder.width][builder.height];
		log.info("Loading scenario...");
		loadSinks();
		loadSolids();
		loadSources();
		log.info("Automaton ready.");
	}

	public void evolve() {
		log.info("Evolving...");
		final int rate = scenario.getRate();
		for (int k = 0; k < steps; ++k) {
			prng.nextBytes(randomness);
			if (k % rate == 0) {
				injectMass();
				cluster.compute(this::momentum);
			}
			cluster.compute(this::collide);
			cluster.compute(this::propagate);
			swap();
		}
		log.info("Releasing cluster...");
		cluster.release();
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

	private void propagate(final int x, final int y) {
		final int down = wrapOnHeight(y - 1);
		final int left = wrapOnWidth(x - 1);
		final int up = wrapOnHeight(y + 1);
		lattice1[x][y] = (short) (
			(nextBit(x, y)? 0 : RANDOM)
			| ((SINK | SOLID) & lattice0[x][y])
			| (A & lattice0[left][y])
			| (B & lattice0[left][down])
			| (C & lattice0[x][down])
			| (D & lattice0[wrapOnWidth(x + 1)][y])
			| (E & lattice0[x][wrapOnHeight(up)])
			| (F & lattice0[left][up])
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

		private Cluster cluster;
		private int height;
		private Scenario scenario;
		private long seed;
		private int steps;
		private int width;

		public CellularAutomaton build() {
			return new CellularAutomaton(this);
		}

		public Builder cluster(final Cluster cluster) {
			this.cluster = cluster;
			return this;
		}

		public Builder height(final int height) {
			this.height = height;
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
	}
}
