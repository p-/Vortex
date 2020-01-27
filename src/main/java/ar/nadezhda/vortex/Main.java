package ar.nadezhda.vortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String ... arguments) {
		try {
			log.info("(2020) Vortex.");
		}
		catch (final Exception exception) {
			log.error("Unknown exception of type: '{}'.", exception.getClass().getName());
			log.error("The error message is: '{}'.", exception.getMessage());
			log.error("The stacktrace is:", exception);
			log.error("End of stacktrace.");
		}
	}
}
