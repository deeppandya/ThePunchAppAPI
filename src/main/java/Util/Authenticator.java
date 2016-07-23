package Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Authenticator {
	private static Authenticator authenticator = null;
	
	private final Map<String, String> authTokens = new HashMap<String, String>();
	
	public static Authenticator getInstance() {
        if ( authenticator == null ) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }
	
	public boolean isAuthTokenValid(String authToken ) {
        return authTokens.containsKey(authToken);
    }
	
	public String generateAuthToken(String email){
		String authToken = UUID.randomUUID().toString();
        authTokens.put(authToken, email);
        return authToken;
	}
	
	public void removeAuthToken(String authToken){
		authTokens.remove(authToken);
	}
	
}
