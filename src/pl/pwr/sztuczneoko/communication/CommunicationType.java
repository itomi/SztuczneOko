package pl.pwr.sztuczneoko.communication;

public enum CommunicationType {
	BLUETOOTH("Bluetooth communication type.", BluetoothCommunication.class);
	
	private String description;
	private Class<? extends Communication> comunicationBundleClass;
	
	CommunicationType(final String description, Class<? extends Communication> clazz) {
		this.description = description;
		this.comunicationBundleClass = clazz;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Class<? extends Communication> getCommunicationType() {
		return this.comunicationBundleClass;
	}
}