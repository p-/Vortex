package ar.nadezhda.vortex.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ar.nadezhda.vortex.config.Line;

import java.awt.Point;

@JsonSubTypes({
	@JsonSubTypes.Type(value = Line.class, name = "line"),
})
@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	include = JsonTypeInfo.As.PROPERTY,
	property = "geometry"
)
public interface Geometry {

	public Point [] points();
}
