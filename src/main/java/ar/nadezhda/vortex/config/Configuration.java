package ar.nadezhda.vortex.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.nio.file.Path;

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

	/** La cantidad de threads a utilizar durante el procesamiento. */
	private int cores;

	/** Habilita el uso de CUDA para acelerar el procesamiento. */
	@JsonProperty()
	private boolean cuda;

	/** Las dimensiones de la grilla. */
	private int [] lattice;

	/** El modo a ejecutar. */
	private String mode;

	/** El directorio de salida. */
	private String output;

	/** Indica si se debe almacenar el estado del autómata celular. */
	@JsonProperty()
	private boolean saveAutomaton;

	/** La estructura del escenario a simular. */
	private Scenario scenario;

	/** La semilla del PRNG asociada a la simulación. */
	private long seed;

	/** La cantidad de pasos temporales de la simulación. */
	private int steps;

	/** La cantidad de pasos requeridos antes de computar alguna magnitud. */
	private int window;

	public Configuration() {
		this.average = 1;
		this.cores = -1;
		this.cuda = false;
		this.lattice = new int [] {0, 0};
		this.mode = "?";
		this.output = "?";
		this.saveAutomaton = false;
		this.seed = System.nanoTime();
		this.scenario = new Scenario();
		this.steps = 0;
		this.window = 1;
	}

	@Override
	public String toString() {
		return new StringBuilder(1024)
			.append("\tmode            : '").append(getMode())
			.append("'\n\toutput          : '").append(getOutput())
			.append("'\n\tlattice         : ").append(getLatticeWidth()).append("x").append(getLatticeHeight())
			.append(" nodes\n\tsteps           : ").append(getSteps())
			.append("\n\twindow          : after ").append(getWindow())
			.append(" steps\n\taverage         : ").append(getAverage()).append("x").append(getAverage())
			.append(" nodes\n\tseed            : ").append(getSeed())
			.append("\n\tcores           : ").append(getCores())
			.append("\n\n\tUse CUDA?       : ").append(useCUDA())
			.append("\n\tSave automaton? : ").append(saveAutomaton())
			.append("\n\n\tscenario        : {\n").append(getScenario())
			.append("\n\t}\n")
			.toString();
	}

	public int getAverage() {
		return average;
	}

	public int getCores() {
		final int cores = Runtime.getRuntime().availableProcessors();
		return this.cores < 0 ? cores : this.cores;
	}

	public int [] getLattice() {
		return lattice.clone();
	}

	public int getLatticeHeight() {
		return lattice[1];
	}

	public int getLatticeWidth() {
		return lattice[0];
	}

	public String getMode() {
		return mode;
	}

	public String getOutput() {
		try {
			return Path.of(output)
				.toRealPath()
				.toString();
		}
		catch (final IOException exception) {
			return "Non-existent! (" + output + ")";
		}
	}

	public Scenario getScenario() {
		return scenario;
	}

	public long getSeed() {
		return seed;
	}

	public int getSteps() {
		return steps;
	}

	public int getWindow() {
		return window;
	}

	public boolean saveAutomaton() {
		return saveAutomaton;
	}

	public boolean useCUDA() {
		return cuda;
	}
}
