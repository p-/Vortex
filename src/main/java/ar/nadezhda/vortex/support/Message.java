package ar.nadezhda.vortex.support;

	/**
	* <p>Mensajes comunes a toda la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public enum Message {

	APP_NAME
		("(2020) Vortex."),
	CONFIG_FILE_NOT_FOUND
		("No se encontró el archivo de configuración ('{}')."),
	CONFIG_INVALID_FORMAT
		("El archivo de configuración no está en formato JSON (ver línea {}, columna {})."),
	CONFIG_UNRECOGNIZED_PROPERTY
		("La configuración posee una propiedad desconocida ('{}'), en la línea {}, columna {}."),
	CONFIG_UNKNOWN_ERROR
		("Error desconocido al leer la configuración."),
	USING_DEFAULT_CONFIG
		("Usando configuración por defecto."),
	UNKNOWN_MODE
		("Modo desconocido ('{}'). Vea la documentación."),
	UNSPECIFIED_MODE
		("El modo no fue especificado."),
	CONFIGURATION
		("Configuración:\n{}"),
	FINISH_TIME
		("Finalizado en {} segundos.");

	private final String message;

	private Message(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
