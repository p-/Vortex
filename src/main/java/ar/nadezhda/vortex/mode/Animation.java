package ar.nadezhda.vortex.mode;

import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.interfaces.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Animation implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Animation.class);

	@Override
	public void run(final Configuration config) {
		log.info("Animating...");
	}
}
