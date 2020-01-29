package ar.nadezhda.vortex.support;

import static javax.measure.MetricPrefix.NANO;
import static tech.units.indriya.unit.Units.SECOND;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Time;
import tech.units.indriya.quantity.Quantities;

	/**
	* <p>Un timer.</p>
	*
	* @author Agustín Golmar
	*/

public final class Timer {

	private final Quantity<Time> start;

	private Timer(final Quantity<Time> start) {
		this.start = start;
	}

	public static Quantity<Time> currentTime() {
		return Quantities.getQuantity(System.nanoTime(), NANO(SECOND));
	}

	public static Timer start() {
		return new Timer(currentTime());
	}

	public Quantity<Time> startTime() {
		return start;
	}

	public Quantity<Time> time() {
		return time(SECOND);
	}

	public Quantity<Time> time(final Unit<Time> unit) {
		return currentTime()
			.subtract(start)
			.to(unit);
	}
}
