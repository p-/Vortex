package ar.nadezhda.vortex.mode;

import static tech.units.indriya.unit.Units.SECOND;
import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.interfaces.Mode;
import ar.nadezhda.vortex.io.OccupationReader;
import ar.nadezhda.vortex.support.Timer;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Computa la densidad de masa.</p>
	*
	* @author Agustín Golmar
	*/

public final class Density implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Density.class);

	@Override
	public void run(final Configuration config) {
		log.info("Density...");
		final Timer timer = Timer.start();
		final int coarseHeight = config.getLatticeHeight()/config.getAverage();
		final int coarseWidth = config.getLatticeWidth()/config.getAverage();
		final int nodes = coarseWidth*coarseHeight;
		try (
			final OccupationReader reader = new OccupationReader(config);
			final PrintWriter writer = new PrintWriter(
				new BufferedOutputStream(
					new FileOutputStream(config.getOutput() + "/density.xyz")));
		) {
			int k = 0;
			boolean canRead = true;
			while (canRead) {
				if (k % nodes == 0) {
					writer.println(nodes);
					writer.println(k * config.getWindow() / nodes);
				}
				++k;
				canRead = reader.readSample((t, x, y, N) -> {
					writer.print(x + " " + y + " ");
					writer.println(N[0] + N[1] + N[2] + N[3] + N[4] + N[5]);
				});
			}
		}
		catch (final FileNotFoundException exception) {
			log.error("The occupation file can't be found ('occupation.bin').");
		}
		catch (final IOException exception) {
			log.error("Error inesperado al leer la ocupación media.");
			exception.printStackTrace();
		}
		log.info("Density ended in {}.", timer.time(SECOND));
	}
}
