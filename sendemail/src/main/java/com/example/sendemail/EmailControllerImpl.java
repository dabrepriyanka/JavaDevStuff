package com.example.sendemail;

import com.example.sendemail.EmailSenders.EmailContext;
import com.example.sendemail.EmailSenders.EmailResult;
import com.example.sendemail.EmailSenders.EmailServiceProvider;
import com.example.sendemail.RequestProcessor.HtmlToPlainTextConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@Validated
public class EmailControllerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EmailControllerImpl.class);
    // private static final String EMAIL_ERROR = "Email should be valid";
    // private static final String NAME_SIZE_ERROR = "Name size has to be less than
    // or equal to 10";
    // private static final String SUBJECT_SIZE_ERROR = "Subject must be between 10
    // and 50 characters";

    @Autowired
    public HtmlToPlainTextConverter htmlToPlainTextConverter;

    @Autowired
    public EmailServiceProvider emailServiceProvider;

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> sendSimpleEmail(@RequestBody EmailContext emailContext) {

        try {
            String message = htmlToPlainTextConverter.html2text(emailContext.getBody());
            emailContext.setBody(message);
            
            // send plain text message
            EmailResult result = emailServiceProvider.sendEmail(emailContext);
            if (result.isSuccess()) {
                return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(result.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            LOG.error("Error while sending out email..{}", exception.getMessage());
            ;
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
