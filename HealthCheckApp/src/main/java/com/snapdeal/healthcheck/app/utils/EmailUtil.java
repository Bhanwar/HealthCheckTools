package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/** The email address to. */
	private List<String> emailAddressTo = new ArrayList<String>();
	
	private List<String> emailAddressCc = new ArrayList<String>();
	
	public List<String> getEmailAddressCc() {
		return emailAddressCc;
	}

	public void setEmailAddressCc(List<String> emailAddressCc) {
		this.emailAddressCc = emailAddressCc;
	}

	public List<String> getEmailAddressBcc() {
		return emailAddressBcc;
	}

	public void setEmailAddressBcc(List<String> emailAddressBcc) {
		this.emailAddressBcc = emailAddressBcc;
	}

	public String getMsgSubject() {
		return msgSubject;
	}

	public void setMsgSubject(String msgSubject) {
		this.msgSubject = msgSubject;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		EmailUtil.props = props;
	}

	public Authenticator getAuth() {
		return auth;
	}

	public void setAuth(Authenticator auth) {
		this.auth = auth;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getFROM_ADDRESS() {
		return FROM_ADDRESS;
	}

	public void setFROM_ADDRESS(String fROM_ADDRESS) {
		FROM_ADDRESS = fROM_ADDRESS;
	}

	public List<String> getEmailAddressTo() {
		return emailAddressTo;
	}


	private List<String> emailAddressBcc = new ArrayList<String>();
	
	/** The msg subject. */
	private String msgSubject = new String();
	
	/** The msg text. */
	private String msgText = new String();
	
	/** The props. */
	//Create email sending properties
	private static Properties props = new Properties();
	
	/** The auth. */
	// creates a new session with an authenticator
	private Authenticator auth;


	/** The user name. */
	private String USER_NAME = "cx.automation.qa@snapdeal.com";   //User name of the Goole(gmail) account
	
	/** The passsword. */
	private String PASSSWORD = "Tech@2015";  //Password of the Goole(gmail) account
	
	/** The from address. */
	private String FROM_ADDRESS = "cx.automation.qa@snapdeal.com";  //From addresss

	/**
	 * Instantiates a new email util.
	 *
	 * @param emailAddressTo the email address to
	 * @param msgSubject the msg subject
	 * @param msgBody the msg body
	 */
	public EmailUtil(List<String> emailAddressTo, String msgSubject, String msgBody) {
		this.emailAddressTo = emailAddressTo;
		this.msgSubject = msgSubject;
		this.msgText = msgBody;
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER_NAME, PASSSWORD);
			}
		};
	}
	
	public EmailUtil(List<String> emailAddressTo, List<String> emailAddressCc, List<String> emailAddressBcc, String msgSubject, String msgBody) {
		this(emailAddressTo, msgSubject, msgBody);
		this.emailAddressCc = emailAddressCc;
		this.emailAddressBcc = emailAddressBcc;
	}

	/**
	 * Sets the email address to.
	 *
	 * @param emailAddressTo the new email address to
	 */
	public void setEmailAddressTo(List<String> emailAddressTo) {
		this.emailAddressTo = emailAddressTo;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.msgSubject = subject;
	}

	/**
	 * Sets the message text.
	 *
	 * @param msgText the new message text
	 */
	public void setMessageText(String msgText) {
		this.msgText = msgText;
	}

	@Override
	public String toString() {
		return "From: " + this.FROM_ADDRESS + " , Subject: " + this.msgSubject + " , Recipients: " + this.emailAddressTo;
	}

	/**
	 * Send html email.
	 *
	 * @throws Exception the exception
	 */
	public boolean sendHTMLEmail() {

		Session session = Session.getInstance(props,auth);
		boolean mailSent = false;
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_ADDRESS)); //Set from address of the email
			message.setContent(msgText,"text/html"); //set content type of the email
			Address[] toAddress = new Address[emailAddressTo.size()];
			for (int i=0; i<emailAddressTo.size(); i++) {
				toAddress[i] = new InternetAddress(emailAddressTo.get(i));
			}
			message.addRecipients(RecipientType.TO, toAddress);
			if(emailAddressCc != null && emailAddressCc.size() > 0) {
				Address[] ccAddress = new Address[emailAddressCc.size()];
				for (int i=0; i<emailAddressCc.size(); i++) {
					ccAddress[i] = new InternetAddress(emailAddressCc.get(i));
				}
				message.addRecipients(RecipientType.CC, ccAddress);
			}
			if(emailAddressBcc != null && emailAddressBcc.size() > 0) {
				Address[] bccAddress = new Address[emailAddressBcc.size()];
				for (int i=0; i<emailAddressBcc.size(); i++) {
					bccAddress[i] = new InternetAddress(emailAddressBcc.get(i));
				}
				message.addRecipients(RecipientType.BCC, bccAddress);
			}
			message.setSubject(msgSubject); //Set email message subject
			Transport.send(message); //Send email message

			log.debug("Successfully Sent Mail !! " + this.toString());
			mailSent = true;
		} catch (MessagingException e) {
			log.error("Exception Occured while sending sendHTMLEmailMessage : " + this.toString(), e);
		}
		return mailSent;
	}

	/**
	 * Send html multi part email.
	 */
	public void sendHTMLMultiPartEmail() {

		Session session = Session.getInstance(props, auth);

		try {

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM_ADDRESS)); //Set from address of the email
			
			for (String recipient : emailAddressTo) {
				Address a = new InternetAddress(recipient);
				msg.addRecipient(Message.RecipientType.TO, a); //Set email recipient
			}

			//msg.setRecipients(RecipientType.CC, cc);
			msg.setSubject("subject");
			msg.setSentDate(new Date());

			MimeMultipart mp = new MimeMultipart();
			MimeBodyPart part = new MimeBodyPart();
			//part.setText(msgText);
			part.setContent(msgText, "text/html");
			mp.addBodyPart(part);
			msg.setContent(mp);

			// Content type has to be set after the message is put together
			// Then saveChanges() must be called for it to take effect
			part.setHeader("Content-Type", "text/html");
			part.setHeader("charset", "utf-8");
			msg.saveChanges();
			Transport.send(msg); //Send email message

			log.debug("Successfully Sent Mail !! " + this.toString());

		} catch (Exception e) {
			log.error("Exception Occured while sending sendHTMLEmailMultiPartMessage : " + this.toString(), e);
		}
	}


	/**
	 * Send embedded image html email.
	 *
	 * @param inlineImagesMap the inline images map
	 */
	public void sendEmbeddedImageHTMLEmail ( Map<String, String> inlineImagesMap) {
		
		Session session = Session.getInstance(props, auth);

		try {
			// creates a new e-mail message
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(USER_NAME));
			
			for (String recipient : emailAddressTo) {
				Address a = new InternetAddress(recipient);
				msg.addRecipient(Message.RecipientType.TO, a); //Set email recipient
			}
			msg.setSubject(msgSubject);
			msg.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msgText, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// adds inline image attachments
			if (inlineImagesMap != null && inlineImagesMap.size() > 0) {
				Set<String> setImageID = inlineImagesMap.keySet();

				for (String contentId : setImageID) {
					MimeBodyPart imagePart = new MimeBodyPart();
					imagePart.setHeader("Content-ID", "<" + contentId + ">");
					imagePart.setDisposition(MimeBodyPart.INLINE);

					String imageFilePath = inlineImagesMap.get(contentId);
					try {
						imagePart.attachFile(imageFilePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					multipart.addBodyPart(imagePart);
				}
			}
			msg.setContent(multipart);
			Transport.send(msg);
			log.debug("Successfully Sent Mail !! " + this.toString());

		} catch (Exception e) {
			log.error("Exception Occured while sending sendHTMLEmailMultiPartMessage : " + this.toString(), e);
		}
	}
	
	/**
	 * Send plain text email.
	 *
	 * @throws Exception the exception
	 */
	public void sendTextEmail() throws Exception {

		Session session = Session.getInstance(props,auth);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_ADDRESS)); //Set from address of the email
			message.setContent(msgText,"text/plain"); //set content type of the email

			for (String recipient : emailAddressTo) {
				Address a = new InternetAddress(recipient);
				message.addRecipient(Message.RecipientType.TO, a); //Set email recipient
			}
			message.setSubject(msgSubject); //Set email message subject
			Transport.send(message); //Send email message

			log.debug("Successfully Sent Mail !! " + this.toString());

		} catch (MessagingException e) {
			log.error("Exception Occured while sending sendHTMLEmailMessage : " + this.toString(), e);
		}
	}
}
