package ar.nadezhda.vortex.support;

	/**
	* <p>Mensajes comunes a toda la aplicación.</p>
	*
	* @author Agustín Golmar
	*/

public enum Message {

	APP_NAME
		("(2020) Vortex."),
	CANNOT_INSTANTIATE
		("You shall not instantiate this class."),
	CANNOT_TERMINATE_THREAD_POOL
		("Cannot shutdown threads in the cluster."),
	CLUSTER_INTERRUPTED
		("The cluster was interrupted during his operation."),
	CONFIG_FILE_NOT_FOUND
		("Configuration file not found ('{}')."),
	CONFIG_INVALID_FORMAT
		("The configuration file is not a valid JSON (see line {}, column {})."),
	CONFIG_UNRECOGNIZED_PROPERTY
		("The configuration has an unknown property ('{}'), at line {}, column {}."),
	CONFIGURATION
		("Configuration:\n{}"),
	FINISH_TIME
		("Finish in {}."),
	UNKNOWN_ERROR
		("Unknown exception of type: '{}'.\n\tThe error message is: '{}'.\n\tThe stacktrace is:"),
	UNKNOWN_MODE
		("Unknown mode (trying to find '{}'). See usage."),
	UNSPECIFIED_MODE
		("The mode wasn't specified.");

	private final String message;

	private Message(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
