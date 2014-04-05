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
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Communication provideCommunication(CommunicationType type) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class communicationClassType = type.getCommunicationType();
		Constructor[] ctors = communicationClassType.getConstructors();
		Constructor ctor = null;
		
		for( int i = 0 ; i < ctors.length; i++ ) {
			ctor = ctors[i];
			if( ctor.getGenericExceptionTypes().length == 0)
				break;
		}
	
		Communication comm = (Communication) ctor.newInstance();	
		comm.prepareCommunicationBundle();
		return comm;
		
	}
}
