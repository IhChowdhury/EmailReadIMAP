package com.ibrahim.mailreader.service;

import com.ibrahim.mailreader.EmailReader;
import com.ibrahim.mailreader.configuration.EmailConfiguration;
import com.ibrahim.mailreader.exception.MailBotException;
import com.ibrahim.mailreader.model.MessageModel;
import java.io.IOException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class EmailReaderServiceImp implements IEmailRederService {
    EmailReader emailReader = new EmailReader();

    @Override
    public Store configureEmail(String host, String port, String protocal, String email, String password) throws MessagingException, IOException, MailBotException {
        return EmailConfiguration.configureEmail(host, port, protocal, email, password);
    }

    @Override
    public Message[] readInbox(Folder folder, Store store) throws MessagingException {
        return emailReader.readInbox(folder,store);
    }

    @Override
    public MessageModel parseMessageModel(Message message) throws MessagingException, Exception {
        return emailReader.parseMessageModel(message);
    }

    

}
