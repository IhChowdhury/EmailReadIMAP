package com.ibrahim.mailreader.service;

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
public interface IEmailRederService {

    Store configureEmail(String host, String port, String protocal, String email,
            String password) throws MessagingException, IOException, MailBotException;

    Message[] readInbox(Folder folder, Store store) throws MessagingException;
    
    MessageModel parseMessageModel(Message message) throws MessagingException, Exception;

}
