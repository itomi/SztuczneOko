package pl.pwr.sztuczneoko.core;

public class ExternDevice {
	protected String name;
	protected boolean connected;
	protected int id;
	protected String description;
	public ExternDevice() {
		
	}
	public ExternDevice(String n, boolean conn, String desc){
		name = n;
		connected = conn;
		description = desc;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	@Override
	public String toString() {
		return getName();
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
