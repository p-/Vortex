package ar.nadezhda.vortex.mode;

import static ar.nadezhda.vortex.core.Mask.*;
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
	* <p>Computa el campo de velocidades.</p>
	*
	* @author Agustín Golmar
	*/

public final class Velocity implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Velocity.class);

	@Override
	public void run(final Configuration config) {
		log.info("Velocity field...");
		final Timer timer = Timer.start();
		final int coarseHeight = config.getLatticeHeight()/config.getAverage();
		final int coarseWidth = config.getLatticeWidth()/config.getAverage();
		final int nodes = coarseWidth*coarseHeight;
		try (
			final OccupationReader reader = new OccupationReader(config);
			final PrintWriter writer = new PrintWriter(
				new BufferedOutputStream(
					new FileOutputStream(config.getOutput() + "/velocity.xyz")));
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
					final double ρ = N[0] + N[1] + N[2] + N[3] + N[4] + N[5];
					final double Jx = Ax*N[0] + Bx*N[1] + Cx*N[2] + Dx*N[3] + Ex*N[4] + Fx*N[5];
					final double Jy = Ay*N[0] + By*N[1] + Cy*N[2] + Dy*N[3] + Ey*N[4] + Fy*N[5];
					writer.print(x + " " + y + " ");
					writer.println((Jx/ρ) + " " + (Jy/ρ));
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
		log.info("Velocity field ended in {}.", timer.time(SECOND));
	}
}
