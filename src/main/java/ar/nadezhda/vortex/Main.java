package ar.nadezhda.vortex;

import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.config.Configurator;
import ar.nadezhda.vortex.support.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Punto de entrada principal de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Main {

	private static final String CONFIGURATION_FILE = "vortex.json";
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String ... arguments) {
		try {
			log.info(Message.APP_NAME.toString());
			final Configuration config = Configurator.load(CONFIGURATION_FILE);
			log.info(Message.CONFIGURATION.toString(), config);
			log.info(Message.FINISH_TIME.toString(), 0.0);
		}
		catch (final Exception exception) {
			log.error("Unknown exception of type: '{}'.", exception.getClass().getName());
			log.error("The error message is: '{}'.", exception.getMessage());
			log.error("The stacktrace is:", exception);
			log.error("End of stacktrace.");
		}
	}
}
