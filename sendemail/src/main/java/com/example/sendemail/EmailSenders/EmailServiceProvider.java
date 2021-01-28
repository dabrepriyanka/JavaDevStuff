package com.example.sendemail.EmailSenders;

/**
 * @author pdabre
 * Initiate the email service provider interface
 */

public interface EmailServiceProvider {

	public EmailResult sendEmail(EmailContext emailContext) throws Exception;

}