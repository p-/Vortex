package ar.nadezhda.vortex;

import static tech.units.indriya.unit.Units.SECOND;
import ar.nadezhda.vortex.config.Configuration;
import ar.nadezhda.vortex.config.Configurator;
import ar.nadezhda.vortex.interfaces.Mode;
import ar.nadezhda.vortex.support.Message;
import ar.nadezhda.vortex.support.Timer;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Punto de entrada principal de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Main {

	private static final String CONFIGURATION_FILE = "vortex.json";
	private static final String LOCATION = "vortex.config";
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String ... arguments) {
		final String configLocation = System.getProperty(LOCATION, CONFIGURATION_FILE);
		try {
			final Timer timer = Timer.start();
			log.info(Message.APP_NAME.toString());
			final Configuration config = Configurator.load(configLocation);
			log.info(Message.CONFIGURATION.toString(), config);
			runMode(config);
			log.info(Message.FINISH_TIME.toString(), timer.time(SECOND));
		}
		catch (final ClassNotFoundException exception) {
			log.error(Message.UNKNOWN_MODE.toString(),
				exception.getLocalizedMessage());
		}
		catch (final FileNotFoundException exception) {
			log.error(Message.CONFIG_FILE_NOT_FOUND.toString(), configLocation);
		}
		catch (final InvalidFormatException | JsonParseException exception) {
			final JsonLocation location = exception.getLocation();
			log.error(Message.CONFIG_INVALID_FORMAT.toString(),
				location.getLineNr(),
				location.getColumnNr());
			log.error(exception.getOriginalMessage());
		}
		catch (final UnrecognizedPropertyException exception) {
			final JsonLocation location = exception.getLocation();
			log.error(Message.CONFIG_UNRECOGNIZED_PROPERTY.toString(),
				exception.getPropertyName(),
				location.getLineNr(),
				location.getColumnNr());
		}
		catch (final Exception exception) {
			log.error(Message.UNKNOWN_ERROR.toString(),
				exception.getClass().getName(),
				exception.getMessage(),
				exception);
			log.error("End of stacktrace.");
		}
	}

	private static void runMode(final Configuration config)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException,
				InvocationTargetException, NoSuchMethodException, SecurityException,
				ClassNotFoundException {
		((Mode) Class.forName(modeClassName(config.getMode()))
			.getDeclaredConstructor()
			.newInstance())
			.run(config);
	}

	private static String modeClassName(final String className) {
		return "ar.nadezhda.vortex.mode."
			+ className.substring(0, 1).toUpperCase()
			+ className.substring(1);
	}
}
