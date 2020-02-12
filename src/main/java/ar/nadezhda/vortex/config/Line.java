package ar.nadezhda.vortex.config;

import ar.nadezhda.vortex.interfaces.Geometry;
import java.awt.Point;

	/**
	* <p>Geometría de línea recta.</p>
	*
	* @author Agustín Golmar
	*/

public final class Line implements Geometry {

	private int [] destination;
	private int [] source;

	public Line() {
		this.destination = new int [] {};
		this.source = new int [] {};
	}

	@Override
	public String toString() {
		return new StringBuilder(64)
			.append("line from (").append(source[0])
			.append(", ").append(source[1])
			.append(") to (").append(destination[0])
			.append(", ").append(destination[1])
			.append(")")
			.toString();
	}

	@Override
	public Point [] points() {
		int Δx = 0, Δy = 0;
		if (source[0] < destination[0]) Δx = 1;
		else if (destination[0] < source[0]) Δx = -1;
		if (source[1] < destination[1]) Δy = 1;
		else if (destination[1] < source[1]) Δy = -1;
		final int size = 1 + Math.max(
			Δx * (destination[0] - source[0]),
			Δy * (destination[1] - source[1]));
		final Point [] points = new Point [size];
		points[0] = new Point(source[0], source[1]);
		for (int k = 0; k < size - 1; ++k) {
			points[k + 1] = new Point(points[k].x + Δx, points[k].y + Δy);
		}
		return points;
	}

	public int [] getDestination() {
		return destination.clone();
	}

	public int [] getSource() {
		return source.clone();
	}
}
