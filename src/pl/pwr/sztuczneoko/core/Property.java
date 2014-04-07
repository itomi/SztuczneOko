package pl.pwr.sztuczneoko.core;

import java.util.ArrayList;

public class Property{
	String name;
	boolean state;
	public Property(String name, boolean state){
		this.name = name;
		this.state = state;
	}
	public boolean isState() {
		return state;
	}
	public String getName() {
		return name;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public void setName(String name) {
		this.name = name;
	}
}
