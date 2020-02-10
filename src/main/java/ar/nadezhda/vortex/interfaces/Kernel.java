package ar.nadezhda.vortex.interfaces;

	/**
	* <p>Aplica una función sobre un nodo específico del espacio.</p>
	*
	* @author Agustín Golmar
	*/

@FunctionalInterface
public interface Kernel {

	public void at(final int x, final int y);
}
