package com.ibrahim.mailreader.service;

import com.ibrahim.mailreader.EmailReader;
import com.ibrahim.mailreader.configuration.EmailConfiguration;
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
public class EmailReaderServiceImp implements IEmailRederService {

    @Override
    public Store configureEmail(String host, String port, String protocal, String email, String password) throws MessagingException, IOException, MailBotException {
        return EmailConfiguration.configureEmail(host, port, protocal, email, password);
    }

    @Override
    public List<MessageModel> readInbox(Store store) throws MessagingException, IOException, Exception {
        return EmailReader.readInbox(store);
    }
    
}
