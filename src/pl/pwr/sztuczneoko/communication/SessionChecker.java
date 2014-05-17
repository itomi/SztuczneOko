package pl.pwr.sztuczneoko.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SessionChecker {

	private static final Set<Session> activeSessions = new HashSet<Session>();
	private static final Map<Session, Long> renewalPeriods = new HashMap<Session, Long>();
	
	static void addNewSessionAndRegisterForActivityChecks(final Session session, final long renewalPeriod) throws Exception {
		if( activeSessions.contains(session) ) {
			throw new Exception("Session is already established and active.");
		} else {
			activeSessions.add(session);
			renewalPeriods.put(session, Long.valueOf(renewalPeriod) );
		}
	}
	
	
	
	boolean isSessionActive(final Session session) {
		//TODO: really check if session is still active and the device is in range
		return true;
	}
	
}
