package ar.nadezhda.vortex.interfaces;

	/**
	* <p>Un cluster permite computar una función de forma eficiente, en general
	* disponiendo la misma en varias unidades de procesamiento.</p>
	*
	* @author Agustín Golmar
	*/

public interface Cluster {

	public void compute(final Kernel kernel);
	public void release();
}
