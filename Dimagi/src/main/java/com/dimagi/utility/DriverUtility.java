package com.dimagi.utility;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.*;

/**
 * <h1>DriverUtility class</h1> <br>
 * Utility Class for Driver <br>
 * <br>
 * .
 *
 * @author Deeksha Sethi
 * @version 1.0 <br>
 *          <br>
 */
public class DriverUtility  {

	/** The driver. */
	static WebDriver driver;
	
	
	/**
	 * This function sets the WebDriver and launches the URL specified in
	 * Frameowrk.properties file with property key BaseURL. <br>
	 * <br>
	 *
	 * @author Deeksha Sethi <br>
	 *         <br>
	 * @version 1.0 <br>
	 *          <br>
	 * @param browserName
	 *            - Specify the name of the browser to be launched. <br>
	 *            <br>
	 * @return the driver
	 * @exception Exception
	 *                If incorrect browser name entered. <br>
	 *                <br>
	 * @category Driver Utility Class for getting WebDriver
	 */
	
	public static void getDriver(String browserName) throws Exception {

		if (browserName.equalsIgnoreCase("Chrome") || browserName.equalsIgnoreCase("Ch")) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") 
					+ PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "CH_DriverPath32_Bit"));

			driver = new ChromeDriver();

		} else if (browserName.equalsIgnoreCase("Firefox") || browserName.equalsIgnoreCase("Ff")) {

			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")
					+ PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "FF_DriverPath64_Bit"));

			driver = new FirefoxDriver();

		}

		else if (browserName.equalsIgnoreCase("Explorer") || browserName.equalsIgnoreCase("IE")) {

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")
					+ PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "IE_DriverPath64_Bit"));

			driver = new InternetExplorerDriver();

		} else {

			throw new Exception("Incorrect Input");

		}

		System.out.println(System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "BaseURL"));
		driver.manage().timeouts().pageLoadTimeout(PropertiesUtility.readLongFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "PageLoadTimeOut"),
				TimeUnit.SECONDS);

		driver.manage().timeouts().implicitlyWait(PropertiesUtility.readIntFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "ImplicitWaitTimeOut"),
				TimeUnit.SECONDS);

		driver.manage().deleteAllCookies();

		/**
		 * Reading Application Base URL
		 * from the Properties file
		 * 
		 */
		
		driver.get(PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "BaseURL"));
		
		
		
		driver.manage().window().maximize();

	}

	/**
	 * This function appends data from an Excel file. <br>
	 * <br>
	 *
	 * @author Deeksha Sethi <br>
	 *         <br>
	 * @version 1.0 <br>
	 *          <br>
	 * @param scrollDriver
	 *            - The driver which has launched the site. <br>
	 *            <br>
	 * @category Utility Class for getting WebDriver
	 */
	public static void scrollToBottom(WebDriver scrollDriver) {
		JavascriptExecutor scroller = (JavascriptExecutor) scrollDriver;

		try {
			double pos = 0;
			while (true) {
				scroller.executeScript("window.scrollBy(0,755)");
				pos += 755;
				// wait for a second and half before scrolling again
				Thread.sleep(1500);

				if (pos > Double.parseDouble(scroller.executeScript("return document.body.scrollHeight;").toString()))
					break;
			}
		} catch (InterruptedException e) {
			System.err.println("Sleep Routine was Interrupted");
			e.printStackTrace();
		}
	}

}
