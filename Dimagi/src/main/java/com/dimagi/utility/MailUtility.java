package com.dimagi.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class sendMail.
 */
public class MailUtility {

	public static String userName;

	public static String password ;

	public static String emailTo ;

	public static String emailToCC ;

	public static String starttls ;

	public static String host ;

	public static String port ;

	public static Properties prop = new Properties();

	public static String socketFactoryClass ;

	public static String fallback ;

	public static String path;

	public static String moduleName;
	
	public static String subject;

	public static int indexOfComma = 0;

	public static String userFullName ;
	
	public static String htmlFileName;
	
	public static String reportUrl;
	
	public static String body="";

	public static boolean flag=true;
	
	public static String EMAIL_REGEX = "[a-z0-9\\_\\-\\.]+@[a-z0-9\\_\\-\\.]+\\.[a-z]+";

	/**
	 * Send email to client.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void sendEmailToClient() throws Exception {
		
		/**
		 * Reading Mail essential 
		 * data from Mail Properties
		 * file 
		 */
		
		userName=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "SenderEmailId");
	
		password= PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Password");

		emailTo=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "EmailTo");

		emailToCC=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "EmailToCC");

		subject=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Subject");
	
		host=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Host");
	
		port=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Port");

		starttls=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Starttls");
	
		socketFactoryClass=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "SocketFactoryClass");
	
		fallback=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "Fallback");

		userFullName=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir")+ "\\Resources\\Mail.properties", "UserName");
		
		htmlFileName=System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Mail.properties", "HtmlFileName");

		reportUrl=PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Mail.properties", "ReportUrl");
		
		System.out.println(htmlFileName);
		Properties props = System.getProperties();
		
		/**	
		 * Default user name for SMTP.
		 * @Return Type:String
		 * 
		 */
		props.put("mail.smtp.user", userName);
		
		
		/**	
		 * The SMTP server to connect to.
		 * @Return Type:String
		 * 
		 */
		props.put("mail.smtp.host", host);
		
		
		/**
		 * If true, attempt to authenticate the user using the AUTH command. 
		 * Defaults to false
		 * @Return Type:boolean
		 */
		props.put("mail.smtp.auth", "true");


		/**
		 * The SMTP server port to connect to, 
		 * if the connect() method doesn't explicitly specify one. 
		 * Defaults to 25
		 * 
		 * @Return Type:int
		 * 
		 */
		
		if (!"".equals(port)) {
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.socketFactory.port", port);
		}

		/**
		 * If true, enables the use of the STARTTLS command 
		 *  to switch the connection to a TLS-protected connection 
		 *  before issuing any login commands. 
		 *  Defaults to false.
		 *  
		 *  @Return Type:boolean
		 *  
		 */
		
		if (!"".equals(starttls))
			props.put("mail.smtp.starttls.enable", starttls);

		/**
		 * 	If set, specifies the name of a class
		 *  that implements the javax.net.SocketFactory interface. 
		 *  This class will be used to create SMTP sockets.
		 *  
		 *  @Return Type:String
		 */
		
		if (!"".equals(socketFactoryClass))
			props.put("mail.smtp.socketFactory.class", socketFactoryClass);

		/**	
		 * If set to true, failure to create a socket 
		 * using the specified socket factory class 
		 * will cause the socket to be created using the
		 * java.net.Socket class. 
		 * Defaults to true.
		 * 
		 * @Return Type:boolean
		 * 
		 */
		if (!"".equals(fallback))
			props.put("mail.smtp.socketFactory.fallback", fallback);

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);

		try {

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userName,prop.getProperty(userFullName)));
			msg.setSubject(subject);


			if (emailTo.contains(",")) {
				String[] multipleEmailTo = emailTo.split(",");
				for (int j = 0; j < multipleEmailTo.length; j++) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(multipleEmailTo[j]));
				}

			} else {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			}

			if (!"".equals(emailToCC)) {

				if (emailToCC.contains(",")) {
					String[] multipleEmailTo = emailToCC.split(",");
					for (int j = 0; j < multipleEmailTo.length; j++) {
						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(multipleEmailTo[j]));
					}

				} else {
					msg.addRecipient(Message.RecipientType.CC, new InternetAddress(emailToCC));
				}
			}
			
			System.out.println(userFullName);
			
			
				BufferedReader in = new BufferedReader(new FileReader(htmlFileName));
				String str;
				while ((str = in.readLine()) != null) {
					body +=str;
				}
				
				/**
				 * Reading URL of the Output Report of Test Execution
				 *  from Properties file for
				 * drafting the email body to be sent
				 * 
				 */
				System.out.println(password);
				body = body.replace("{Url}", reportUrl);
				in.close();

				System.out.println(body+"Dead Code");
				
			
			BodyPart messageBodyPart = new MimeBodyPart();

/*			messageBodyPart.setText(
					"Hi Nick,\n\nGreetings for the day!\n\nPlease find attached the Test Execution Result for Dimagi .\n\n\nThanks & Regards\nTesingXperts");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
*/
			if (body.contains("<body>"))
				messageBodyPart.setContent(body, "text/html");
			
			else
			messageBodyPart.setText(body);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if(flag)
			{
			messageBodyPart = new MimeBodyPart();
			String path = System.getProperty("user.dir") + "/ExecutionResult.zip";
			DataSource source = new FileDataSource(path);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("ExecutionResult.zip");
			multipart.addBodyPart(messageBodyPart);
		
			}
			msg.setContent(multipart);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("Mail Sent successfully");

		 
		}
		
		catch (IOException e) {
			e.getMessage();
		}
		
		catch (AuthenticationFailedException e1) {
			JFrame wrongCredentials = new JFrame();
			JOptionPane.showMessageDialog(wrongCredentials, "Wrong Username or Password");
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}