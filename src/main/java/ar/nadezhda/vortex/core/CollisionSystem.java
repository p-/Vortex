package ar.nadezhda.vortex.core;

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
		final short [] system = new short [256];
		for (int k = 0; k < system.length; ++k) {
			// Solids and non-solids:
			if ((k & Mask.SOLID) != 0) {
				system[k] = (short) ((k & Mask.META) | (Mask.bounceAll(k)));
			}
			else {
				system[k] = (short) k;
			}
		}
		// 2-particle head-on collisions:
		system[9] = Mask.B + Mask.E;
		system[18] = Mask.C + Mask.F;
		system[36] = Mask.A + Mask.D;
		system[137] = Mask.C + Mask.F + Mask.RANDOM;
		system[146] = Mask.A + Mask.D + Mask.RANDOM;
		system[164] = Mask.B + Mask.E + Mask.RANDOM;
		// symmetric 3-particle collisions:
		system[21] = Mask.B + Mask.D + Mask.F;
		system[42] = Mask.A + Mask.C + Mask.E;
		system[149] = Mask.B + Mask.D + Mask.F + Mask.RANDOM;
		system[170] = Mask.A + Mask.C + Mask.E + Mask.RANDOM;
		// 4-particle head-on collisions:
		system[27] = Mask.B + Mask.C + Mask.E + Mask.F;
		system[45] = Mask.A + Mask.B + Mask.D + Mask.E;
		system[54] = Mask.A + Mask.C + Mask.D + Mask.F;
		system[155] = Mask.A + Mask.C + Mask.D + Mask.F + Mask.RANDOM;
		system[173] = Mask.B + Mask.C + Mask.E + Mask.F + Mask.RANDOM;
		system[182] = Mask.A + Mask.B + Mask.D + Mask.E + Mask.RANDOM;
		return system;
	}
}
