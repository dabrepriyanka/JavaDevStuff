package com.example.sendemail.EmailSenders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;

/**
 * This class implements the email sending using Spendgrid REST API service
 * 
 * @author pdabre
 *
 */
@Service
@Primary
public class SpendGridEmailSender implements EmailServiceProvider {

	private static final Logger LOG = LoggerFactory.getLogger(SpendGridEmailSender.class);

	private static final String APIKEY = "api_key_wSgSqHtxOIdVrxTjp8Ms2y9l";
	private static final String SPENDGRID = "SpendGrid";
	private static final String URL = "https://bw-interviews.herokuapp.com/spendgrid/send_email";

	public SpendGridEmailSender() {
	}

	@Override
	public EmailResult sendEmail(EmailContext emailContext) throws Exception {

		EmailResult result = null;
		try {

			// create a map for post parameters
			Map<String, String> map = new HashMap<>();

			String sender = String.format("%s <%s>", emailContext.getFrom_name(), emailContext.getFrom());
			String recipient = String.format("%s <%s>", emailContext.getTo_name(), emailContext.getTo());
			map.put(EmailSenderStrings.SENDER, sender);
			map.put(EmailSenderStrings.RECIPIENT, recipient);
			map.put(EmailSenderStrings.SUBJECT, emailContext.getSubject());
			map.put(EmailSenderStrings.BODY, emailContext.getBody());

			ObjectMapper objectMapper = new ObjectMapper();
			String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
					.header(EmailSenderStrings.CONTENT_TYPE, EmailSenderStrings.APPLICATION_TYPE)
					.header(EmailSenderStrings.X_API_KEY, APIKEY).POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();

			var client = HttpClient.newHttpClient();

			HttpResponse.BodyHandler<String> asString = HttpResponse.BodyHandlers.ofString();
			HttpResponse<String> response = client.send(request, asString);

			// check response status code
			if (response.statusCode() == 200) {
				LOG.info(String.format(EmailSenderStrings.EMAIL_SENDING_SUCCESSFUL, SPENDGRID));
				result = new EmailResult(true, null);
			} else {
				LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SPENDGRID) + response.body());
				result = new EmailResult(false, EmailSenderStrings.THIRDPARTY_SERVICE_FAILURE);
			}
		} catch (Exception e) {
			LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SPENDGRID), e);
			result = new EmailResult(false, EmailSenderStrings.THIRDPARTY_SERVICE_FAILURE);
			result.setServiceIsDown(true);
		}
		return result;
	}

}