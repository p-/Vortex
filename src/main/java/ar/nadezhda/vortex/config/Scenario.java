package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.interfaces.Geometry;
import java.util.Arrays;
import java.util.stream.Collectors;

	/**
	* <p>Configuración del escenario.</p>
	*
	* @author Agustín Golmar
	*/

public final class Scenario {

	/** El tipo de rebote contra los nodos sólidos. */
	private String bouncing;

	/** Las direcciones del momento inyectado. */
	private String [] momentum;

	/** El intervalo de inyección de momento. */
	private int rate;

	/** La probabilidad de inyección de momento. */
	private double ratio;

	/** Los nodos absorbentes. */
	private Geometry [] sink;

	/** Los nodos sólidos. */
	private Geometry [] solid;

	/** Las fuentes de generación de partículas. */
	private Geometry [] source;

	public Scenario() {
		this.bouncing = "no-slip";
		this.momentum = new String [] {};
		this.rate = 1;
		this.ratio = 0.0;
		this.sink = new Geometry [] {};
		this.solid = new Geometry [] {};
		this.source = new Geometry [] {};
	}

	@Override
	public String toString() {
		return new StringBuilder(1024)
			.append("\t\tbouncing    : '").append(getBouncing())
			.append("'\n\t\tmomentum    : [").append(listToString(getMomentum()))
			.append("]\n\t\trate        : ").append(getRate())
			.append("\n\t\tratio       : ").append(100 * getRatio())
			.append(" %\n\n\t\tsink        : {\n\t\t\t").append(geometryToString(getSink()))
			.append("\n\t\t}\n\t\tsolid       : {\n\t\t\t").append(geometryToString(getSolid()))
			.append("\n\t\t}\n\t\tsource      : {\n\t\t\t").append(geometryToString(getSource()))
			.append("\n\t\t}")
			.toString();
	}

	public String getBouncing() {
		return bouncing;
	}

	public String [] getMomentum() {
		return momentum.clone();
	}

	public int getRate() {
		return rate;
	}

	public double getRatio() {
		return ratio;
	}

	public Geometry [] getSink() {
		return sink.clone();
	}

	public Geometry [] getSolid() {
		return solid.clone();
	}

	public Geometry [] getSource() {
		return source.clone();
	}

	private static String geometryToString(final Geometry [] geometries) {
		return new StringBuilder()
			.append(Arrays.asList(geometries).stream()
				.map(Geometry::toString)
				.collect(Collectors.joining(",\n\t\t\t")))
			.toString();
	}

	private static String listToString(final String [] list) {
		return new StringBuilder()
			.append(Arrays.asList(list).stream()
				.map(String::toString)
				.collect(Collectors.joining(", ")))
			.toString();
	}
}
