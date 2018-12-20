package com.tmkoo.searchapi.util;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;
public class MailUtil {    
    public static void send(Boolean needAuth,String fromUser,String fromPwd,String smtpHost,String smtpPort,
    		String toEmail,String fromEmail,String title,String content) throws  Exception {
        // 配置javamail
        Properties props = System.getProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.auth",needAuth?"true":"false");
        props.put("mail.smtp.connectiontimeout", 180);
        props.put("mail.smtp.timeout", 600);
        props.setProperty("mail.mime.encodefilename", "false");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUser, fromPwd);
            }
        });

        SMTPTransport transport = (SMTPTransport) mailSession.getTransport("smtp");

        MimeMessage message = new MimeMessage(mailSession);
        // 发信人
        message.setFrom(new InternetAddress(fromEmail, fromUser, "UTF-8"));
        // 收件人地址
        message.addRecipient(RecipientType.TO, new InternetAddress(toEmail));
        // 邮件主题
        message.setSubject(title, "UTF-8");
        message.setContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title></title></head> <body>"+content+"</body></html>", "text/html;charset=UTF-8");
         

        // 连接smtp服务器，发送邮件
        transport.connect();
        transport.sendMessage(message, message.getRecipients(RecipientType.TO)); 
        String messageId = getMessage(transport.getLastServerResponse());
        String emailId = messageId + "0$" + toEmail;
        System.out.println("messageId:" + messageId);
        System.out.println("emailId:" + emailId);
        transport.close();
         
    }
    
    private static String getMessage(String reply) {
        String[] arr = reply.split("#");

        String messageId = null;

        if (arr[0].equalsIgnoreCase("250 ")) {
            messageId = arr[1];
        }

        return messageId;
    }
}
