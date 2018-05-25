package com;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageFetcher;
import com.twilio.type.PhoneNumber;
import static spark.Spark.*;

import java.util.Map;


@Controller
public class TheController {

	public static final String ACCOUNT_SID = "YourInfoHere";
    public static final String AUTH_TOKEN = "YourInfoHere";
    public static final String TWILIO_NUMBER = "+YourInfoHere";
    
    @GetMapping("/home")
    public String greeting(@RequestParam(value="name", required=false) String name, Model model) {
        model.addAttribute("name", name);
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
		} catch (Exception e) {
			return "home";

		}

		return "home";
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
    		dataBase.createRecord(messageBody.getResponse());
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
