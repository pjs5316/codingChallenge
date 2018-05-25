package com;
import com.twilio.Twilio;
import com.twilio.rest.lookups.v1.PhoneNumber;

public class LookUp {
	public static final String ACCOUNT_SID = "ACf00bf3124c56a8dfc71e0f62e2dbfeaa";
    public static final String AUTH_TOKEN = "842f98db546101f50e35f27c3db33f46";
    
	public boolean check(String input){
		 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	try {
		com.twilio.rest.lookups.v1.PhoneNumber number = PhoneNumber
            .fetcher(new com.twilio.type.PhoneNumber(input))
            .setType("carrier")
            .fetch();
 
    return true;
 
    } catch(com.twilio.exception.ApiException e) {
        if(e.getStatusCode() == 404) {
            System.out.println("Phone number not found.");
        } else {
            throw e;
        }
    }
	return false;
	}
}
