package com.example.sendemail.RequestProcessor;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

/**
 * This class converts html to plain text.
 */
@Service
public class HtmlToPlainTextConverter {
    public String html2text(String html) {
        return Jsoup.parse(html).wholeText();
    }
    
}
