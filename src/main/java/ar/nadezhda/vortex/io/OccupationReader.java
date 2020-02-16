package ar.nadezhda.vortex.io;

import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.interfaces.Sampler;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Permite obtener y manipular los parámetros de ocupación desde un
	* archivo generado durante una simulación.</p>
	*
	* @author Agustín Golmar
	*/

public final class OccupationReader implements AutoCloseable {

	private static final Logger log = LoggerFactory.getLogger(OccupationReader.class);
	private static final double SQRT_3_2 = 0.5 * Math.sqrt(3.0);

	private final int coarseHeight;
	private final int coarseWidth;
	private final Configuration config;
	private final String input;
	private final double factor;
	private final DataInputStream file;

	private int t = 0;
	private int w = 0;
	private int h = 0;

	public OccupationReader(final Configuration config)
			throws FileNotFoundException {
		this.coarseHeight = config.getLatticeHeight()/config.getAverage();
		this.coarseWidth = config.getLatticeWidth()/config.getAverage();
		this.config = config;
		this.input = config.getOutput() + "/occupation.bin";
		this.factor = config.getWindow() * Math.pow(config.getAverage(), 2);
		log.info("Using averaging factor of {}.", factor);
		this.file = new DataInputStream(
			new BufferedInputStream(
				new FileInputStream(this.input), 100*60*30*6*4));
	}

	@Override
	public void close() {
		try {
			file.close();
		}
		catch (final IOException exception) {
			log.error("Cannot close the file '{}'.", input);
		}
	}

	/**
	* <p>Permite obtener una muestra de ocupación, compuesta por 6 enteros,
	* correspondientes a cada una de las 6 velocidades, el tiempo y la posición
	* en la que ocurrió.</p>
	*
	* @return
	*	Verdadero, si todavía hay más muestras.
	* @throws IOException
	*	En caso de que no se pueda leer la muestra.
	*/
	public boolean readSample(final Sampler sampler) throws IOException {
		final double x = w;
		final double y = h * SQRT_3_2;
		final double [] occupation = new double [6];
		occupation[0] = file.readInt()/factor;
		occupation[1] = file.readInt()/factor;
		occupation[2] = file.readInt()/factor;
		occupation[3] = file.readInt()/factor;
		occupation[4] = file.readInt()/factor;
		occupation[5] = file.readInt()/factor;
		sampler.sample(t, x, y, occupation);
		if (++h >= coarseHeight) {
			h = 0;
			if (++w >= coarseWidth) {
				w = 0;
				t += config.getWindow();
				if (t > config.getSteps()) {
					t = 0;
					return false;
				}
			}
		}
		return true;
	}

	/**
	* <p>Permite obtener todas las muestras.</p>
	*
	* @param sampler
	* @throws IOException
	*/
	public void readSamples(final Sampler sampler) throws IOException {
		log.info("Reading a lattice of {}x{} coarse-nodes.", coarseWidth, coarseHeight);
		file.mark(Integer.MAX_VALUE);
		for (int t = 0; t <= config.getSteps(); t += config.getWindow()) {
			for (int w = 0; w < coarseWidth; ++w) {
				for (int h = 0; h < coarseHeight; ++h) {
					final double x = w;
					final double y = h * SQRT_3_2;
					final double [] occupation = new double [6];
					occupation[0] = file.readInt()/factor;
					occupation[1] = file.readInt()/factor;
					occupation[2] = file.readInt()/factor;
					occupation[3] = file.readInt()/factor;
					occupation[4] = file.readInt()/factor;
					occupation[5] = file.readInt()/factor;
					sampler.sample(t, x, y, occupation);
				}
			}
		}
		file.reset();
	}
}
