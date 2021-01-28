package com.example.sendemail.EmailSenders;

/**
 * This is for HTTP result object for JSON marshalling and unmarshalling
 * 
 * @author pdabre
 *
 */
public class EmailResult {
	
	private boolean success;
	private String errorMessage;
	private Boolean serviceIsDown = false;
	private String status;

	public EmailResult(String status) {
		this.status = status;
	}
	
	public EmailResult(boolean success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isServiceDown() {
		return serviceIsDown;
	}

	public void setServiceIsDown(Boolean serviceIsDown) {
		this.serviceIsDown = serviceIsDown;
	}

	public String getStatus() {
		return status;
	}
	
}
