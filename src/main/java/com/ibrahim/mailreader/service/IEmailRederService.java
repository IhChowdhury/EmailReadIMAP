package com.ibrahim.mailreader.service;

import com.ibrahim.mailreader.exception.MailBotException;
import com.ibrahim.mailreader.model.MessageModel;
import java.io.IOException;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Ibrahim Chowdhury
 */
public interface IEmailRederService {

    public Store configureEmail(String host, String port, String protocal, String email,
            String password) throws MessagingException, IOException, MailBotException;

    public List<MessageModel> readInbox(Store store, int readingLimit) throws MessagingException, IOException, Exception;

}
