package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.support.Message;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Permite obtener la configuración de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Configurator {

	private static final Logger log = LoggerFactory.getLogger(Configurator.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static Configuration load(final String filename) {
		try {
			return Configurator.tryToLoad(filename);
		}
		catch (final FileNotFoundException exception) {
			log.error(Message.CONFIG_FILE_NOT_FOUND.toString(), filename);
		}
		catch (final JsonParseException exception) {
			final JsonLocation location = exception.getLocation();
			log.error(
					Message.CONFIG_INVALID_FORMAT.toString(),
					location.getLineNr(), location.getColumnNr());
			log.error(exception.getOriginalMessage());
		}
		catch (final UnrecognizedPropertyException exception) {
			final JsonLocation location = exception.getLocation();
			log.error(
					Message.CONFIG_UNRECOGNIZED_PROPERTY.toString(),
					exception.getPropertyName(),
					location.getLineNr(), location.getColumnNr());
		}
		catch (final IOException exception) {
			log.error(Message.CONFIG_UNKNOWN_ERROR.toString());
			log.error(exception.getMessage());
		}
		log.warn(Message.USING_DEFAULT_CONFIG.toString());
		return new Configuration();
	}

	private static Configuration tryToLoad(final String filename)
			throws JsonParseException, JsonMappingException, IOException {
		final File file = new File(filename);
		return mapper.readValue(file, Configuration.class);
	}
}
