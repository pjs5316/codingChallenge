package com;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Map;


@Controller
public class TheController {

	public static final String ACCOUNT_SID = "YourInfoHere";
    public static final String AUTH_TOKEN = "YourInfoHere";
    public static final String TWILIO_NUMBER = "+YourInfoHere";
    
    @GetMapping("/home")
    public String greeting(Model model) {
        model.addAttribute("customerinfo", new CustomerInfo());      
        return "home";
    }
    @PostMapping("/home")
    public String formSumbit(@ModelAttribute CustomerInfo customerinfo){
		System.out.println(customerinfo.getName());
		LookUp lookUp = new LookUp();
		try {
			if (lookUp.check(customerinfo.getPhoneNumber())) {
				sendSMS(customerinfo.getPhoneNumber(), customerinfo.getMessage());
				return "landing";
			}
			else{
				return "error";
			}
		} catch (Exception e) {
			return "home";
		}
    }
    @PostMapping("/error")
    public String formReSumbit(@ModelAttribute CustomerInfo customerinfo){
		
		LookUp lookUp = new LookUp();
		try {
			if (lookUp.check(customerinfo.getPhoneNumber())) {
				sendSMS(customerinfo.getPhoneNumber(), customerinfo.getMessage());
				return "landing";
			}
			else{
				return "error";
			}
		} catch (Exception e) {
			return "error";			
		}
    }
    
    @PostMapping(value = "/sms")
    public ResponseEntity<String> recieveMessage(@RequestParam Map<String, String> body){
    	System.out.println("Recieved Message!");
    	System.out.println(body.toString());
    	try{
    		MessageBody messageBody = new MessageBody();
    		messageBody.setResponse(body.get("Body"));
    		System.out.println(messageBody.getResponse());
    		DataBase dataBase = new DataBase();
    		dataBase.printRecords();
    	}
    	catch (Exception e) {
			System.out.println("message body empty");
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    public void sendSMS(String number, String textMessage) {
        try {
            
        	 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        	 Message message = Message.creator(new PhoneNumber(number),  new PhoneNumber(TWILIO_NUMBER),
        		        textMessage).create();

        	 System.out.println(message.getSid());
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }  
    
}
