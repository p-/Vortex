package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.support.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

	private Configurator() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static Configuration load(final String filename)
			throws JsonParseException, JsonMappingException, IOException {
		final File file = new File(filename);
		final Configuration config = mapper.readValue(file, Configuration.class);
		log.info("Configuration loaded from '{}'.", Path.of(filename).toRealPath().toString());
		return Validator.validate(config);
	}
}
