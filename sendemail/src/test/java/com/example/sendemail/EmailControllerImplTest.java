package com.example.sendemail;

import static org.mockito.ArgumentMatchers.any;

import java.nio.charset.Charset;

import com.example.sendemail.EmailSenders.EmailContext;
import com.example.sendemail.EmailSenders.EmailResult;
import com.example.sendemail.EmailSenders.EmailServiceProvider;
import com.example.sendemail.RequestProcessor.HtmlToPlainTextConverter;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@AutoConfigureMockMvc
public class EmailControllerImplTest {

    @MockBean
    private HtmlToPlainTextConverter htmlToPlainTextConverter;

    @MockBean
    private EmailServiceProvider emailServiceProvider;

    @Autowired
    EmailControllerImpl emailControllerImpl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToSendEmailAndValidUser_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        Mockito.when(htmlToPlainTextConverter.html2text("This is a test email")).thenReturn("This is a test email");
        EmailResult result = new EmailResult(true, null);
        Mockito.when(emailServiceProvider.sendEmail(any(EmailContext.class))).thenReturn(result);
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(textPlainUtf8));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidTo_Name_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.to_name", Is.is("To name must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidFrom_Name_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from_name", Is.is("From name must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidTo_Email_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", Is.is("To Email must be a valid email address")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidFrom_Email_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"alice\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from", Is.is("From Email must be a valid email address")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndEmptyTo_Email_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", Is.is("To email must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndEmptyFrom_Email_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from", Is.is("From email must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidSubject_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"\", \"body\" : \"This is a test email\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subject", Is.is("Subject must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToSendEmailAndInValidBody_thenCorrectResponse() throws Exception {
        String user = "{\"to\": \"bob@domain.com\", \"to_name\" : \"bob\", \"from\": \"alice@domain.com\", \"from_name\" : \"alice\", \"subject\": \"Test email\", \"body\" : \"\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/email").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Is.is("Body must not be empty")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}