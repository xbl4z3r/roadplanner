package me.xbl4z3r.roadplanner.node;

public class Marker extends Node {
	public double displacement;
	public String code;

	public Marker(double displacement) {

	}

	public Marker(double displacement, String code, Type type) {
		super();
		this.setType(type);
		this.displacement = displacement;
		this.code = code;
	}
}
