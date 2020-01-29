package ar.nadezhda.vortex.mode;

import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.interfaces.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Simulation implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Simulation.class);

	@Override
	public void run(final Configuration config) {
		log.info("Simulating...");
	}
}
