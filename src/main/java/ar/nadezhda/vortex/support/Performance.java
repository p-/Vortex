package ar.nadezhda.vortex.support;

	/**
	* <p>Performance.</p>
	*
	* @author Agustín Golmar
	*/

public final class Performance {

	private static final Runtime RUNTIME = Runtime.getRuntime();

	private Performance() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static long totalMemory() {
		return RUNTIME.totalMemory();
	}

	public static long freeMemory() {
		return RUNTIME.freeMemory();
	}

	public static long maximumMemory() {
		return RUNTIME.maxMemory();
	}

	public static long usedMemory() {
		return totalMemory() - freeMemory();
	}

	public static int availableProcessors() {
		return RUNTIME.availableProcessors();
	}
}
