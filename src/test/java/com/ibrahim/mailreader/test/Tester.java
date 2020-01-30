package com.ibrahim.mailreader.test;

import com.ibrahim.mailreader.exception.MailBotException;
import com.ibrahim.mailreader.model.MessageModel;
import com.ibrahim.mailreader.service.EmailReaderServiceImp;
import com.ibrahim.mailreader.service.IEmailRederService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class Tester {

    public static void main(String[] args) {
        String host = "imap.gmail.com";// change accordingly
        String port = "993";
        String protocal = "imaps";
        String email = "mahtab.uddinnn@gmail.com";// change accordingly
        String password = "qwerty142536";// change accordingly
//        String username = "skyroproject10@gmail.com";// change accordingly
//        String password = "ab142536";// change accordingly
        IEmailRederService emailRederService = new EmailReaderServiceImp();
        Store store;
        try {
            store = emailRederService.configureEmail(host, port, protocal, email, password);
            List<MessageModel> messageModelList = emailRederService.readInbox(store,5);
        } catch (MessagingException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MailBotException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

}
