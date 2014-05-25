package pl.pwr.sztuczneoko.communication;

public interface ProtocolWrapper {

	byte[] CLOSE_OPERATION = {(byte)0xDE, (byte)0xAD};
	byte[] SESSION_START = {(byte)0xAB, (byte)0xCD};

	/**
	 * 
	 * Method that wraps data passed through parameter into protocol frame, so that it can be transported
	 * to device that can handle the same protocol
	 * 
	 * @param data 
	 * 			byte array containing data that has will be passed by protocol
	 * @return
	 * 			wrapped data with additional information specific to choosen protocol
	 */
	byte[] wrap(byte[] data);
	
	/**
	 * 
	 * Method that unwraps the passed through parameter data, and checks its validity.
	 * 
	 * @param data
	 * 			byte array that contains wrapped protocol data
	 * @return
	 * 			data without additional, protocol specific information
	 */
	byte[] unwrap(byte[] data);
	
	/**
	 * 
	 * @return
	 */
	String getVersion();
}
