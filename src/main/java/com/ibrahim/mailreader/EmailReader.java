package com.ibrahim.mailreader;

import com.ibrahim.mailreader.exception.MailBotException;
import com.ibrahim.mailreader.model.MessageModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.search.FlagTerm;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class EmailReader {

    public void ReadEmail(String host, String port, String protocal, String email,
            String password) throws MessagingException, IOException, MailBotException {

        Folder folder = null;
        Store store = null;
        try {
            Properties properties = new Properties();
            if (protocal.equalsIgnoreCase("imap") || protocal.equalsIgnoreCase("imaps")) {
                putImapsProperties(properties, protocal, host, port);
            } else if (protocal.equalsIgnoreCase("pop3")) {
                putPopsProperties(properties, host, port, email);
            } else {
                System.out.println("Invalid Protocal");
                return;
            }

            Session session = Session.getDefaultInstance(properties);

            store = session.getStore(protocal);
            if (store == null) {
                throw new MailBotException("Error -Problem on mail store for " + email);
            }
            store.connect(host, email, password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

//            Message messages[] = folder.getMessages();
            /* Get the messages which is unread in the Inbox */
            Message messages[] = folder.search(new FlagTerm(new Flags(Flag.SEEN), false));
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());

            for (Message message : messages) {
                if (message.isSet(Flags.Flag.SEEN)) {
                    continue;
                }

                String from = "unknown";

                if (message.getReplyTo().length > 0) {
                    from = message.getReplyTo()[0].toString();
                } else if (message.getFrom().length > 0) {
                    from = message.getFrom()[0].toString();
                }

                String subject = message.getSubject();
                String contentType = message.getContentType();

                System.out.println("From: " + from);
                System.out.println("Subject : " + subject);
                System.out.println("contentType " + contentType);

                String filename = "temp/" + subject;
                saveParts(message.getContent(), filename);
                message.setFlag(Flag.SEEN, true);
                break;
            }
        } finally {
            if (folder != null) {
                folder.close(true);
            }
            if (store != null) {
                store.close();
            }
        }

    }

    private static void saveParts(Object content, String filename) throws MessagingException, IOException {
        OutputStream outputStream = null;
        InputStream inputStream = null;

        MessageModel messageModel = new MessageModel();
        if (content instanceof Multipart) {
            Multipart multipart = ((MimeMultipart) content);
            int parts = multipart.getCount();

            for (int j = 0; j < parts; j++) {
                MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);
                if (part.getContent() instanceof Multipart) {
                    saveParts(part.getContent(), filename);
                } else {
                    String extension = "";
                    if (part.isMimeType("text/html")) {
                        MimePart mimePart = (MimePart) multipart.getBodyPart(j);
                        messageModel.setMailContentType(((Multipart) content).getContentType());
                        messageModel.setRawBody((String) mimePart.getContent());
                        System.out.println((String) content);
                        extension = "txt";
                    } else if (part.isMimeType("text/plain")) {
                        System.out.println((String) part.getContent());
                    } else {
                        System.out.println(part.getContentType());
                    }

//                    filename = filename + "." + extension;
//                    System.out.println("... " + filename);
//                    outputStream = new FileOutputStream(new File(filename));
//                    inputStream = part.getInputStream();
//                    int k;
//                    while ((k = inputStream.read()) != -1) {
//                        outputStream.write(k);
//                    }
                }
            }
        } else {
            System.out.println("Text: " + content.toString());
        }

        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
    }

    private void putImapsProperties(Properties props, String protocal, String host, String port) {
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

    private void putPopsProperties(Properties props, String host, String port, String email) {
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
