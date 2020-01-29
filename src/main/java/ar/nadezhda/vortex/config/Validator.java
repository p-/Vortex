package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.support.Message;

	/**
	* <p>Valida la configuración de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public final class Validator {

	private Validator() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static Configuration validate(final Configuration config) {
		return config;
	}
}
