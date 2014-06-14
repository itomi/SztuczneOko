package pl.pwr.sztuczneoko.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SessionsHolder {

	private static final Set<Session> activeSessions = new HashSet<Session>();
	private static final Map<Session, Long> renewalPeriods = new HashMap<Session, Long>();
	
	static void addNewSessionAndRegisterForActivityChecks(final Session session, final long renewalPeriod) throws Exception {
		if( activeSessions.contains(session) ) {
			
			clear();
			session.send(ProtocolWrapper.SESSION_START);
			activeSessions.add(session);
			renewalPeriods.put(session, Long.valueOf(renewalPeriod) );
			throw new Exception("Session is already established and active.");
		} else {
			session.send(ProtocolWrapper.SESSION_START);
			activeSessions.add(session);
			renewalPeriods.put(session, Long.valueOf(renewalPeriod) );
		}
	}
	
	public static void clear() {
		activeSessions.clear();
	}
	
	static Set<Session> getActiveSessions() {
		//TODO: make this module realy check the session, if it is not able to communicate, then kill it
		return activeSessions;
	}
	
	boolean isSessionActive(final Session session) {
		//TODO: really check if session is still active and the device is in range
		return true;
	}
	
}
