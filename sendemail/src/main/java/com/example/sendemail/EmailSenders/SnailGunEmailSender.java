package com.example.sendemail.EmailSenders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
//@Primary
public class SnailGunEmailSender implements EmailServiceProvider {
  private static final Logger LOG = LoggerFactory.getLogger(SnailGunEmailSender.class);

  private static final String APIKEY = "api_key_77Nf7aKxrrec3sbGjebbXogP";
  private static final String SNAILGUN = "SnailGun";
  private static final String POST_URL = "https://bw-interviews.herokuapp.com/snailgun/send_email";
  private static final String GET_URL = "https://bw-interviews.herokuapp.com/snailgun/emails";

  ObjectMapper objectMapper;

  public SnailGunEmailSender() {
    objectMapper = new ObjectMapper();
  }

  public EmailResult sendEmail(EmailContext emailContext) throws Exception {

    Map<String, String> map = new HashMap<>();
    map.put(EmailSenderStrings.FROM_EMAIL, emailContext.getFrom());
    map.put(EmailSenderStrings.FROM_NAME, emailContext.getFrom_name());
    map.put(EmailSenderStrings.TO_EMAIL, emailContext.getTo());
    map.put(EmailSenderStrings.TO_NAME, emailContext.getTo_name());
    map.put(EmailSenderStrings.SUBJECT, emailContext.getSubject());
    map.put(EmailSenderStrings.BODY, emailContext.getBody());

    EmailResult result;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    try {
      String requestBody = objectMapper
          			.writerWithDefaultPrettyPrinter()
          			.writeValueAsString(map);
      var postRequest = HttpRequest.newBuilder().uri(URI.create(POST_URL))
          .header(EmailSenderStrings.CONTENT_TYPE, EmailSenderStrings.APPLICATION_TYPE)
          .header(EmailSenderStrings.X_API_KEY, APIKEY).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

      var client = HttpClient.newBuilder().executor(executor).build();
      
      CompletableFuture<HttpResponse<String>> completableFuture = client.sendAsync(postRequest,
          HttpResponse.BodyHandlers.ofString());
      completableFuture.thenApplyAsync(HttpResponse::headers).thenAcceptAsync(System.out::println);
      HttpResponse<String> response = completableFuture.join();
      if (response.statusCode() == 200) {
        LOG.info(String.format(EmailSenderStrings.EMAIL_SENDING_SUCCESSFUL, SNAILGUN));
      } else {
        LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SNAILGUN) + response.body());
      }
      ClientResponse clientResponse = objectMapper.readValue(response.body(), ClientResponse.class);
      result = getEmailStatusFromService(emailContext, clientResponse.id);

    } catch (Exception e) {
      LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SNAILGUN), e);
      result = new EmailResult(false, EmailSenderStrings.THIRDPARTY_SERVICE_FAILURE);
      result.setServiceIsDown(true);
    }
    executor.shutdownNow();
    return result;
  }

  public EmailResult getEmailStatusFromService(EmailContext emailContext, String responseId) {
    EmailResult result = null;
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GET_URL + "/" + responseId))
          .header(EmailSenderStrings.CONTENT_TYPE, EmailSenderStrings.APPLICATION_TYPE)
          .header(EmailSenderStrings.X_API_KEY, APIKEY).GET().build();

      var client = HttpClient.newHttpClient();

      HttpResponse.BodyHandler<String> asString = HttpResponse.BodyHandlers.ofString();
      HttpResponse<String> response = client.send(request, asString);

      ClientResponse clientResponse = objectMapper.readValue(response.body(), ClientResponse.class);

      // check response status code
      if (clientResponse.status == EmailSenderStrings.QUEUED) {
        LOG.info(String.format(EmailSenderStrings.EMAIL_SENDING_QUEUED, SNAILGUN));
        result = new EmailResult(false, EmailSenderStrings.EMAIL_SENDING_QUEUED);
      } else if (clientResponse.status == EmailSenderStrings.SENT) {
        LOG.info(String.format(EmailSenderStrings.EMAIL_SENDING_SUCCESSFUL, SNAILGUN));
        result = new EmailResult(true, null);
      } else if (clientResponse.status == EmailSenderStrings.FAILED) {
        LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SNAILGUN) + response.body());
        result = new EmailResult(false, EmailSenderStrings.THIRDPARTY_SERVICE_FAILURE);
      } else {
        LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_UNKNOWN_STATUS, SNAILGUN) + response.body());
        result = new EmailResult(false, EmailSenderStrings.EMAIL_SENDING_UNKNOWN_STATUS);
      }
    } catch (Exception e) {
      LOG.error(String.format(EmailSenderStrings.EMAIL_SENDING_FAILED, SNAILGUN), e);
      result = new EmailResult(false, EmailSenderStrings.THIRDPARTY_SERVICE_FAILURE);
      result.setServiceIsDown(true);
    }
    return result;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static class ClientResponse {
    public String id;
    public String status;

    public ClientResponse() {

    }

    public ClientResponse(String id, String status) {
      this.id = id;
      this.status = status;
    }
  }
}