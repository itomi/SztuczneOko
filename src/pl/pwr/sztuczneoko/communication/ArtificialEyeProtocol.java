package pl.pwr.sztuczneoko.communication;

public class ArtificialEyeProtocol implements ProtocolWrapper{

	private static final long version = 1;
	
	@Override
	public byte[] wrap(byte[] data) {
		return data;
	}

	@Override
	public byte[] unwrap(byte[] data) {
		return data;
	}

	@Override
	public String getVersion() {
		return Long.toString(version);
	}
}
