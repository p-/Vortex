package ar.nadezhda.vortex.interfaces;

	/**
	* <p>Función de procesamiento de muestras de ocupación.</p>
	*
	* @author Agustín Golmar
	*/

@FunctionalInterface
public interface Sampler {

	/**
	* <p>Permite procesar una muestra de números de ocupación, asociadas a un
	* nodo ubicado en las coordenadas reales (x, y), sobre el que aplican 6
	* grados de libertad posibles.</p>
	*
	* @param t
	*	El tiempo en el cual ocurrió la muestra.
	* @param x
	*	Posición real del nodo sobre el eje horizontal.
	* @param y
	*	Posición real del nodo sobre el eje vertical.
	* @param occupation
	*	Números de ocupación promedio para el nodo, uno por cada velocidad.
	*/
	public void sample(final int t, final double x,
		final double y, final double [] occupation);
}
