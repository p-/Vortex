package ar.nadezhda.vortex.config;

import com.fasterxml.jackson.annotation.JsonProperty;

	/**
	* <p>Configuración global.</p>
	*
	* @author Agustín Golmar
	*/

public final class Configuration {

	/** La cantidad de celdas a promediar para obtener una magnitud. El valor
	* indica tanto el ancho como el alto, es decir, para un valor <i>N</i>, se
	* promediarán <i>NxN</i> celdas.
	*/
	private int average;

	/** La condición de contorno. */
	private String contour;

	/** Habilita el uso de CUDA para acelerar el procesamiento. */
	@JsonProperty()
	private boolean cuda;

	/** Las dimensiones de la grilla. */
	private int [] lattice;

	/** El modo a ejecutar. */
	private String mode;

	/** El tipo de momento inyectado. */
	private String momentum;

	/** El directorio de salida. */
	private String output;

	/** La probabilidad de inyección de momento. */
	private double ratio;

	/** Indica si se debe almacenar el estado del autómata celular. */
	@JsonProperty()
	private boolean saveAutomaton;

	/** La semilla del PRNG asociada a la simulación. */
	private long seed;

	/** La estructura del sólido a simular en la grilla. */
	private String shape;

	/** La cantidad de pasos temporales de la simulación. */
	private int steps;

	/** La cantidad de pasos requeridos antes de computar alguna magnitud. */
	private int window;

	/** La cantidad de threads a utilizar durante el procesamiento. */
	private int workers;

	public Configuration() {
		this.average = 1;
		this.contour = "non-periodic";
		this.cuda = false;
		this.lattice = new int [] {0, 0};
		this.mode = "?";
		this.momentum = "left-to-right";
		this.output = "?";
		this.ratio = 0.0;
		this.saveAutomaton = false;
		this.seed = System.nanoTime();
		this.shape = "?";
		this.steps = 0;
		this.window = 1;
		this.workers = -1;
	}

	@Override
	public String toString() {
		return new StringBuilder(1024)
			.append("\tmode            : '").append(getMode())
			.append("'\n\tshape           : '").append(getShape())
			.append("'\n\toutput          : '").append(getOutput())
			.append("'\n\tlattice         : ").append(getLattice()[0]).append("x").append(getLattice()[1])
			.append(" nodes\n\tsteps           : ").append(getSteps())
			.append("\n\twindow          : ").append(getWindow())
			.append(" steps\n\taverage         : ").append(getAverage())
			.append(" nodes\n\tcontour         : ").append(getContour())
			.append("\n\tmomentum        : ").append(getMomentum())
			.append("\n\tratio           : ").append(100.0 * getRatio())
			.append(" %\n\tseed            : ").append(getSeed())
			.append("\n\tworkers         : ").append(getWorkers())
			.append("\n\n\tUse CUDA        : ").append(useCUDA())
			.append("\n\tSave automaton? : ").append(saveAutomaton())
			.append("\n")
			.toString();
	}

	public int getAverage() {
		return average;
	}

	public String getContour() {
		return contour;
	}

	public int [] getLattice() {
		return lattice.clone();
	}

	public String getMode() {
		return mode;
	}

	public String getMomentum() {
		return momentum;
	}

	public String getOutput() {
		return output;
	}

	public double getRatio() {
		return ratio;
	}

	public long getSeed() {
		return seed;
	}

	public String getShape() {
		return shape;
	}

	public int getSteps() {
		return steps;
	}

	public int getWindow() {
		return window;
	}

	public int getWorkers() {
		final int cores = Runtime.getRuntime().availableProcessors();
		return workers < 0 ? cores : workers;
	}

	public boolean saveAutomaton() {
		return saveAutomaton;
	}

	public boolean useCUDA() {
		return cuda;
	}
}
