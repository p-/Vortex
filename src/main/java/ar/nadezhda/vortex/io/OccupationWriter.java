package ar.nadezhda.vortex.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	* <p>Almacena la ocupación en formato binario. La ocupación es la variable
	* principal que permite determinar los parámetros del sistema simulado.</p>
	*
	* @author Agustín Golmar
	*/

public final class OccupationWriter {

	private static final Logger log = LoggerFactory.getLogger(OccupationWriter.class);

	private final String output;
	private final DataOutputStream file;

	public OccupationWriter(final String output)
			throws FileNotFoundException {
		this.output = output + "/occupation.bin";
		this.file = new DataOutputStream(
			new BufferedOutputStream(
				new FileOutputStream(this.output), 100*60*30*6*4));
	}

	/**
	* <p>Almacena una muestra, provista en un vector tridimensional, donde las
	* primeras 2 componentes representan el <i>coarse graining</i> del lattice,
	* y la última la ocupación para cada una de las velocidades.</p>
	*
	* @param occupation
	*	El vector de ocupación.
	* @throws IOException
	*	En caso de que no se pueda almacenar la muestra.
	*/
	public void saveSample(final int [][][] occupation)
			throws IOException {
		for (int w = 0; w < occupation.length; ++w) {
			for (int h = 0; h < occupation[0].length; ++h) {
				for (int v = 0; v < occupation[0][0].length; ++v) {
					file.writeInt(occupation[w][h][v]);
				}
			}
		}
	}

	public void close() {
		try {
			file.close();
		}
		catch (final IOException exception) {
			log.error("Cannot close the file '{}'.", output);
		}
	}
}
