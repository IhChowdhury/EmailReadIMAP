package com.ibrahim.mailreader.configuration;

import com.ibrahim.mailreader.exception.MailBotException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class EmailConfiguration {

    private EmailConfiguration(){}
    
    public static Store configureEmail(String host, String port, String protocal, String email,
            String password) throws MessagingException, IOException, MailBotException {

        Store store = null;
//        try {
        Properties properties = new Properties();
        if (protocal.equalsIgnoreCase("imap") || protocal.equalsIgnoreCase("imaps")) {
            putImapsProperties(properties, protocal, host, port);
        } else if (protocal.equalsIgnoreCase("pop3")) {
            putPopsProperties(properties, host, port, email);
        } else {
            System.out.println("Invalid Protocal");
            return null;
        }
        Session session = Session.getDefaultInstance(properties);

        store = session.getStore(protocal);
        if (store == null) {
            throw new MailBotException("Error -Problem on mail store for " + email);
        }
        store.connect(host, email, password);

        return store;

//        } finally {
//
//            if (store != null) {
//                store.close();
//            }
//        }
    }

    private static void putImapsProperties(Properties props, String protocal, String host, String port) {
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.mime.ignoreunknownencoding", "true");

        if (protocal.equalsIgnoreCase("imap")) {

            props.setProperty("mail.imap.connectiontimeout", "17000");
            props.setProperty("mail.imap.timeout", "17000");
        }

        if (protocal.equalsIgnoreCase("imaps")) {
            props.setProperty("mail.imaps.host", host);
            props.setProperty("mail.imaps.port", port);
            props.setProperty("mail.imaps.connectiontimeout", "17000");
            props.setProperty("mail.imaps.timeout", "17000");
        }
    }

    private static void putPopsProperties(Properties props, String host, String port, String email) {
        props.put("mail.pop3.host", host.trim());
        props.put("mail.pop3.port", port.trim());
        props.setProperty("mail.pop3.connectiontimeout", "17000");
        props.setProperty("mail.pop3.timeout", "17000");
        props.setProperty("mail.mime.ignoreunknownencoding", "true");
        if (false) {
            props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.pop3.socketFactory.fallback", "false");
            props.put("mail.pop3.socketFactory.port", port.trim());
            props.put("mail.pop3.user", email);
            props.put("mail.store.protocol", "pop3");
        } else {
            props.put("mail.pop3.starttls.enable", "true");
        }
    }

}
