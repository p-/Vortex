package ar.nadezhda.vortex.mode;

import static tech.units.indriya.unit.Units.SECOND;
import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.core.CellularAutomaton;
import ar.nadezhda.vortex.core.ThreadedCluster;
import ar.nadezhda.vortex.interfaces.Mode;
import ar.nadezhda.vortex.support.Timer;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Proceso de simulación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Simulation implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Simulation.class);

	@Override
	public void run(final Configuration config) {
		log.info("Simulating...");
		final Timer timer = Timer.start();
		try {
			new CellularAutomaton.Builder()
				.average(config.getAverage())
				.cluster(new ThreadedCluster.Builder()
					.cores(config.getCores())
					.height(config.getLatticeHeight())
					.width(config.getLatticeWidth())
					.build())
				.height(config.getLatticeHeight())
				.output(config.getOutput())
				.scenario(config.getScenario())
				.seed(config.getSeed())
				.steps(config.getSteps())
				.width(config.getLatticeWidth())
				.window(config.getWindow())
				.build()
				.evolve();
		}
		catch (final IOException exception) {
			log.error("Cannot save the occupation state to a file.");
		}
		log.info("Simulation ended in {}.", timer.time(SECOND));
	}
}
