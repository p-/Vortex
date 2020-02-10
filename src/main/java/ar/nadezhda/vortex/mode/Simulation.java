package ar.nadezhda.vortex.mode;

import static tech.units.indriya.unit.Units.SECOND;
import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.core.CellularAutomaton;
import ar.nadezhda.vortex.core.ThreadedCluster;
import ar.nadezhda.vortex.interfaces.Mode;
import ar.nadezhda.vortex.support.Timer;
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
		new CellularAutomaton.Builder()
			.cluster(new ThreadedCluster.Builder()
				.cores(config.getCores())
				.height(config.getLatticeHeight())
				.width(config.getLatticeWidth())
				.build())
			.height(config.getLatticeHeight())
			.seed(config.getSeed())
			.steps(config.getSteps())
			.width(config.getLatticeWidth())
			.build()
			.evolve();
		log.info("Simulation ended in {}.", timer.time(SECOND));
	}
}
