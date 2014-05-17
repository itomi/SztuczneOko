package pl.pwr.sztuczneoko.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum Service {
	SDP(new UUID(0x0, 0x0001), "Service Discovery Protocol"),
	RFCOMM(new UUID(0x0, 0x0003), "RFCOMM"),
	TSCBIN(new UUID(0x0, 0x0005), "Telephony Control Specification / TSC Binary [ deprecated ]"),
	ATT(new UUID(0x0, 0x0007), "Attribute Protocol"),
	OBEX(new UUID(0x0, 0x0008),"OBEX IrDA Interoperability"),
	BNEP(new UUID(0x0, 0x000F),"Bluetooth Network Encapsulation Protocol"),
	UPNP(new UUID(0x0, 0x0010),"Extended Service Discovery Profile (ESDP) [ deprecated ] "),
	HIDP(new UUID(0x0, 0x0011), "Human Interface Device Profile (HID)"),
	SP(new UUID(0x0, 0x1101), "Serial Port"),
	PANP(new UUID(0x0,0x1117), "Personal Area Networking Profile"),
	HSP(new UUID(0x0, 0x1131), "HeadSet Profile"),
	AEYE(new UUID(0x6666,0x6666),"Sztuczne Oko"),
	UNKNOWN(new UUID(0xFFFF, 0xFFFF), "Unknown Service");

	private static final UUID[] allServices;
	
	public static final Map<UUID, Service> mapUuidToService;

	private final UUID serviceUUID;

	private final String description;

	Service(UUID serviceUUID, final String description) {
		this.serviceUUID = serviceUUID;
		this.description = description;
	}

	public static UUID[] getAll() {
		return allServices;
	}

	public String getDescription() {
		return this.description;
	}

	public UUID UUID() {
		return this.serviceUUID;
	}

	public static Service getServiceByUUID(final UUID uuid) {
		return mapUuidToService.get(uuid);
	}
	
	static {
		List<UUID> listOfUUIDs = new ArrayList<UUID>();
		mapUuidToService = new HashMap<UUID, Service>();
		
		for (final Service service : Service.values()) {
			listOfUUIDs.add(service.serviceUUID);
			mapUuidToService.put(service.UUID(), service);
		}

		allServices = new UUID[listOfUUIDs.size()];
		listOfUUIDs.toArray(allServices);
	}

}