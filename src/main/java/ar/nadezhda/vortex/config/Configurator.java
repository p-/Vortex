package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.support.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

	/**
	* <p>Permite obtener la configuración de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Configurator {

	private static final ObjectMapper mapper = new ObjectMapper();

	private Configurator() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static Configuration load(final String filename)
			throws JsonParseException, JsonMappingException, IOException {
		final File file = new File(filename);
		final Configuration config = mapper.readValue(file, Configuration.class);
		return Validator.validate(config);
	}
}
