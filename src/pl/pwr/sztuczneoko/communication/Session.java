package pl.pwr.sztuczneoko.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class Session {
	private Device device;
	private long renewPeriod;
	private BluetoothSocket socket = null;
	
	private Session() {}
	
	Session(Device device, final long renewPeriod) {
		this.device = device;
		this.renewPeriod = renewPeriod;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public long getRenewPeriod() {
		return renewPeriod;
	}
	
	public void establishConnection(final Service service) throws IOException {
		socket = device.connect(service);
	}
	
	public void send(byte[] data) throws IOException {
		if(socket != null ) {
			OutputStream os = socket.getOutputStream();
			//TODO: Marshalling into some sort of Protocol
			os.write(data);
		}
		throw new IllegalStateException("Session is not active");
	}
	
	public byte[] receive(final int length) throws IOException {
		byte[] data = new byte[length];
		if( socket != null ) {
			InputStream is = socket.getInputStream();
			//TODO: UNMarshalling out to some sort of Protocol
			if(is.available() < length)
				data = new byte[is.available()];
			//TODO: zmienic handlowanie wielkosci bufora
			is.read(data);
		}
		throw new IllegalStateException("SEssion is not active");
	}
}
