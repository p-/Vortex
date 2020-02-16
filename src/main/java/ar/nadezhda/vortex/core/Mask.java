package ar.nadezhda.vortex.core;

import ar.nadezhda.vortex.support.Message;

	/**
	* <p>Máscara de estados de las celdas.</p>
	*
	* @author Agustín Golmar
	*/

public final class Mask {

	public static final short MASS = 0x3F;
	public static final short META = 0x01C0;

	public static final short A = 0x01;
	public static final short B = 0x02;
	public static final short C = 0x04;
	public static final short D = 0x08;
	public static final short E = 0x10;
	public static final short F = 0x20;

	public static final short SOLID = 0x40;
	public static final short RANDOM = 0x80;
	public static final short SINK = 0x0100;

	public static final double SQRT_3_2 = 0.5 * Math.sqrt(3.0);

	public static final double Ax = 1.0;
	public static final double Ay = 0.0;
	public static final double Bx = 0.5;
	public static final double By = SQRT_3_2;
	public static final double Cx = -0.5;
	public static final double Cy = SQRT_3_2;
	public static final double Dx = -1.0;
	public static final double Dy = 0.0;
	public static final double Ex = -0.5;
	public static final double Ey = -SQRT_3_2;
	public static final double Fx = 0.5;
	public static final double Fy = SQRT_3_2;

	private Mask() {
		throw new AssertionError(Message.CANNOT_INSTANTIATE);
	}

	public static short bounce(final int direction) {
		switch (direction) {
			case 0: return 0;
			case A: return D;
			case B: return E;
			case C: return F;
			case D: return A;
			case E: return B;
			case F: return C;
			default: {
				throw new IllegalArgumentException(
					"Cannot bounce the direction "
					+ Integer.toBinaryString(direction)
					+ " (in binary).");
			}
		}
	}

	public static short bounceAll(final int directions) {
		return (short) (bounce(directions & A)
			| bounce(directions & B)
			| bounce(directions & C)
			| bounce(directions & D)
			| bounce(directions & E)
			| bounce(directions & F));
	}

	public static double speed(final int direction) {
		double speedX = 0.0;
		double speedY = 0.0;
		if ((direction & A) != 0) {
			speedX += 1.0;
			speedY += 0.0;
		}
		if ((direction & B) != 0) {
			speedX += 0.5;
			speedY += SQRT_3_2;
		}
		if ((direction & C) != 0) {
			speedX -= 0.5;
			speedY += SQRT_3_2;
		}
		if ((direction & D) != 0) {
			speedX -= 1.0;
			speedY += 0.0;
		}
		if ((direction & E) != 0) {
			speedX -= 0.5;
			speedY -= SQRT_3_2;
		}
		if ((direction & F) != 0) {
			speedX += 0.5;
			speedY -= SQRT_3_2;
		}
		return Math.hypot(speedX, speedY);
	}
}
