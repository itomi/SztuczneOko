package pl.pwr.sztuczneoko.communication;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CommunicationProvider {
	/**
	 * Provides communication module for further usage, it is dynamically typed.
	 * 
	 * @param type 
	 * 			chosen type of communication, can be found {@link CommunicationType}
	 * @return
	 * 			returns instance of <code>Communication</code> implementation, chosen by developer
	 * @throws Exception 
	 * 			when bundle preparation failed to properly initialize module
	 */
	public static Communication provideCommunication(CommunicationType type) throws Exception {
		Class communicationClassType = type.getCommunicationType();
		Constructor[] ctors = communicationClassType.getDeclaredConstructors();
		Constructor ctor = null;
		
		for( int i = 0 ; i < ctors.length; i++ ) {
			ctor = ctors[i];
			if( ctor.getParameterTypes().length == 0) {
				ctor.setAccessible(true);
				break;
			}
		}
	
		Communication comm = (Communication) ctor.newInstance();
		ctor.setAccessible(false);
		comm.prepareCommunicationBundle();
		return comm;
		
	}
}
