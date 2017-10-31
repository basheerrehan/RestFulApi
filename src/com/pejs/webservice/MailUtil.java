package com.pejs.webservice;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailUtil {

	public static void sendEmailRegistrationLink(int id, String email) throws AddressException, MessagingException, UnsupportedEncodingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Setup.MAIL_SMTP_HOST);
		props.put("mail.smtp.port", "587");
		long unixTime = System.currentTimeMillis();
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Setup.MAIL_USERNAME, Setup.MAIL_PASSWORD);
			}
		  });

		String link = Setup.MAIL_REGISTRATION_SITE_LINK+"/"+id+"/" +unixTime;
		
		  StringBuilder bodyText = new StringBuilder(); 
			bodyText.append("<div>")
			     .append("  Dear User<br/><br/>")
			     .append("  Thank you for registration. Your mail ("+email+")<br/>")
			     .append("  Please click <a href=\""+link+"\">here</a> or open below link in browser<br/>")
			     .append("  <a href=\""+link+"\">"+link+"</a>")
			     .append("  <br/><br/>")
			     .append("  Thanks,<br/>")
			     .append("  Basheer Ahmed")
			     .append("</div>");
			Message message = new MimeMessage(session);
			/*message.setFrom(new InternetAddress(Setup.MAIL_USERNAME));*/
			message.setFrom(new InternetAddress(Setup.MAIL_USERNAME,"PEJS Solutions"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Email Registration");
			message.setContent(bodyText.toString(), "text/html; charset=utf-8");
			Transport.send(message);
	}
}
