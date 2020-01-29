package ar.nadezhda.vortex.interfaces;

import ar.nadezhda.vortex.config.Configuration;

	/**
	* <p>Un modo de ejecución de la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public interface Mode {

	public void run(final Configuration config);
}
