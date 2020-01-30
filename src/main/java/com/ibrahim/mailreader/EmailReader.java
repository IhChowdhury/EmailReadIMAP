package com.ibrahim.mailreader;

import com.ibrahim.mailreader.model.MessageModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.search.FlagTerm;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class EmailReader {
    
    private EmailReader(){}

    @Deprecated
    private static void saveParts(Object content, String filename) throws MessagingException, IOException {
        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
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
//                            messageModel.setRawBody((String) mimePart.getContent());
                            System.out.println(content.toString());
                            extension = "txt";
                        } else if (part.isMimeType("text/plain")) {
                            System.out.println(part.getContent().toString());
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
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }

    }

    /**
     * 
     * @param store
     * @return
     * @throws MessagingException
     * @throws IOException
     * @throws Exception 
     */
    public static List<MessageModel> readInbox(Store store, int readingLimit) throws MessagingException, IOException, Exception {

        Folder folder = null;
        List<MessageModel> messageModelList = new LinkedList<MessageModel>();
        try {
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            /* Get the messages which is unread in the Inbox */
            Message messages[] = folder.search(new FlagTerm(new Flags(Flag.SEEN), false));
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());

            int count = 0;
            for (Message message : messages) {
                count++;
                if(count > readingLimit){
                    break;
                }
                System.out.println("No. " + count);
//                if (message.isSet(Flags.Flag.SEEN)) {
//                    continue;
//                }

                String from = "unknown";

                if (message.getFrom().length > 0) {
                    from = message.getFrom()[0].toString();
                }else if (message.getReplyTo().length > 0) {
                    from = message.getReplyTo()[0].toString();
                } 

                String subject = message.getSubject();
                String contentType = message.getContentType();

//                System.out.println("From: " + from);
//                System.out.println("Subject : " + subject);
//                System.out.println("contentType " + contentType);

                String senderName = "";
                String senderEmail = "";
                if(from.contains("<")){
                    String fromSplits[] = from.split("<");
                    senderName = fromSplits[0].trim();
                    senderEmail = fromSplits[1].replace("<", "").replace(">", "").trim();
                }else{
                    senderEmail = from.trim();
                }
                MimeMessage mimeMessage = (MimeMessage) message;
                String messageId = mimeMessage.getMessageID();
                System.out.println("");
                MimeMessageParser parser = new MimeMessageParser(mimeMessage).parse();
                String body = parser.getPlainContent();
                System.out.println("text");
                MessageModel messageModel = new MessageModel();
                messageModel.setFromEmail(senderEmail);
                messageModel.setSenderName(senderName);
                messageModel.setSubject(subject);
                messageModel.setBody(body);
                messageModel.setMessageId(messageId);
                if (message.getReceivedDate() != null) {
                    messageModel.setReceiveDate(message.getReceivedDate().toString());
                    messageModel.setReceiveTime(message.getReceivedDate().getTime());
                }
                
                messageModelList.add(messageModel);
                message.setFlag(Flag.SEEN, true);
//                break;
            }
        } finally {
            if (folder != null) {
                folder.close(true);
            }
        }
        
        return messageModelList;

    }
}
