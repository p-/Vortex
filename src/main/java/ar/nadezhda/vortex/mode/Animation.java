package ar.nadezhda.vortex.mode;

import static tech.units.indriya.unit.Units.SECOND;
import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.interfaces.Mode;
import ar.nadezhda.vortex.support.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Animation implements Mode {

	private static final Logger log = LoggerFactory.getLogger(Animation.class);

	@Override
	public void run(final Configuration config) {
		log.info("Animating...");
		final Timer timer = Timer.start();
		// ...
		log.info("Animation ended in {}.", timer.time(SECOND));
	}
}
