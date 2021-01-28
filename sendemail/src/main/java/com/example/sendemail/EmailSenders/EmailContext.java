package com.example.sendemail.EmailSenders;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * The class used for marshalling and unmarshalling email as json 
 * 
 * @author pdabre
 *
 */
@Getter
@Setter
public class EmailContext {
	
	@NotEmpty(message = "From name must not be empty")
	private String from_name;
	@NotEmpty(message = "From Email must not be empty")
    @Email(message = "From Email must be a valid email address")
	private String from;
	@NotEmpty(message = "To Email must not be empty")
    @Email(message = "To Email must be a valid email address")
	private String to;
	@NotEmpty(message = "To name must not be empty")
	private String to_name;
	@NotEmpty(message = "Subject must not be empty")
	private String subject;
	@NotEmpty(message = "Body must not be empty")
	private String body;

	public EmailContext(String to, String to_name, String from, String from_name, String subject, String body) {
		this.to = to;
		this.to_name = to_name;
		this.from = from;
		this.from_name = from_name;
		this.subject = subject;
		this.body = body;
	}
    
}
