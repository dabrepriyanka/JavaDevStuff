package com.example.sendemail.EmailSenders;

/**
 * This is an abstract class (with default methods and common attributes) and any Email Senders should extend this class
 * 
 * @author pdabre
 *
 */
public class EmailSenderStrings {
	protected static final String THIRDPARTY_SERVICE_FAILURE = "Thirdparty service failure.";
	protected static final String EMAIL_SENDING_SUCCESSFUL = "Email sent successfully via %s.";
	protected static final String EMAIL_SENDING_FAILED = "Failed to send Email via %s. Error: ";
	protected static final String EMAIL_SENDING_QUEUED = "Email has been queued for sending via %s.";
	protected static final String EMAIL_SENDING_UNKNOWN_STATUS = "Email sending status is unknown";

	protected static final String QUEUED = "queued";
	protected static final String SENT = "sent";
	protected static final String FAILED = "failed";

	protected static final String X_API_KEY = "x-api-key";
	protected static final String FROM_EMAIL = "from_email";
	protected static final String FROM_NAME = "from_name";
	protected static final String TO_EMAIL = "to_email";
	protected static final String TO_NAME = "to_name";
	protected static final String SUBJECT = "subject";
	protected static final String BODY = "body";
	protected static final String ID = "users_qHWglBEyDq3LrrD5r8lHVObR";
	protected static final String CONTENT_TYPE = "Content-Type";
	protected static final String APPLICATION_TYPE = "application/json";
	protected static final String SENDER = "sender";
	protected static final String RECIPIENT = "recipient";

		
}