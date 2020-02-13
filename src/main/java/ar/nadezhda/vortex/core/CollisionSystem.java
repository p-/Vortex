package ar.nadezhda.vortex.core;

import static ar.nadezhda.vortex.core.Mask.*;
import ar.nadezhda.vortex.support.Message;

	/**
	* <p>Representa el sistema de colisiones utilizado.</p>
	*
	* @author Agustín Golmar
	*/

public final class CollisionSystem {

	private CollisionSystem() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static short [] noSlipFHP_I() {
		final short [] system = new short [512];
		for (int k = 0; k < 256; ++k) {
			// Solids and non-solids:
			if ((k & SOLID) != 0) {
				system[k] = (short) ((k & META) | (bounceAll(k)));
			}
			else {
				system[k] = (short) k;
			}
		}
		for (int k = 256; k < 512; ++k) {
			// Sinkholes:
			system[k] = SINK;
		}
		// 2-particle head-on collisions:
		system[A + D] = B + E;
		system[B + E] = C + F;
		system[C + F] = A + D;
		system[A + D + RANDOM] = C + F + RANDOM;
		system[B + E + RANDOM] = A + D + RANDOM;
		system[C + F + RANDOM] = B + E + RANDOM;
		// symmetric 3-particle collisions:
		system[A + C + E] = B + D + F;
		system[B + D + F] = A + C + E;
		system[A + C + E + RANDOM] = B + D + F + RANDOM;
		system[B + D + F + RANDOM] = A + C + E + RANDOM;
		// 4-particle head-on collisions:
		system[A + B + D + E] = B + C + E + F;
		system[A + C + D + F] = A + B + D + E;
		system[B + C + E + F] = A + C + D + F;
		system[A + B + D + E + RANDOM] = A + C + D + F + RANDOM;
		system[A + C + D + F + RANDOM] = B + C + E + F + RANDOM;
		system[B + C + E + F + RANDOM] = A + B + D + E + RANDOM;
		return system;
	}
}
