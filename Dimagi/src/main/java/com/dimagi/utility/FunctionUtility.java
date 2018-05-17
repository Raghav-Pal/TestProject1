package com.dimagi.utility;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * <h1>FunctionsUtility class</h1> <br>
 * Utility Class for Functions <br>
 * <br>
 * .
 *
 * @author Deeksha Sethi
 * @version 1.0 <br>
 *          <br>
 */
public class FunctionUtility  {

	/** The extent. */
	public static ExtentReports extent;

	/** The logger. */
	public static ExtentTest logger;

	/** The element. */
	public static By element;

	/** The wait. */
	public static WebDriverWait wait;

	/** The robo. */
	public static Robot robo;

	/** The scan. */
	public static Scanner scan;

	/** The util. */
	static FileUtils util;

	/** The result. */
	public static ITestResult result;

	/** The class name. */
	public static String className;

	/** The image path. */
	public static String imagePath;

	/** The image path. */
	public static String imagePathRelative;

	/** The path for logger. */
	public static String pathForLogger;

	/** The browser name. */
	public static String browserName;

	/** The html. */
	public static String html;

	/** The excel report file full path. */
	public static String excelReportFileFullPath;

	/** The excel report file name. */
	public static String excelReportFileName;

	/** The suite name. */
	public static String suiteName;

	/** The keyboard. */
	public static Keyboard keyboard;

	/** The executor. */
	public static JavascriptExecutor executor;

	/**Exception message For Jira*/
	public static String exceptionMsg;

	/**Jiraapi used to interact with Jira Server Application*/
	public String jiraapi;

	/**Summary for logging bug in Jira*/
	public String summary = null;


	/**
	 * This method prints the suite name in the Extent Report and initiates Extent
	 * Reporting. <br>
	 * <br>
	 * <span style='color:red'><b>Change the suiteName variable in the first line
	 * for correct SUITE name to be printed.</b></span> <br>
	 * <br>
	 * 
	 * @category Utility Class for Functions
	 */
	@BeforeSuite(alwaysRun = true)
	public void load_parameters() throws Exception{

		suiteName = "Dimagi";

		extent = new ExtentReports(System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(
				System.getProperty("user.dir") + "/Resources/Framework.properties", "ExtentReportFile"));

		extent.loadConfig(new File(System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(
				System.getProperty("user.dir") + "/Resources/Framework.properties", "ExtentConfigXML")));



		/** 
		 * Writing System Information 
		 * in the Extent Report 
		 * 
		 */

		extent.addSystemInfo("Host", "Testingxperts");
		extent.addSystemInfo("Environment", "Regression");
		extent.addSystemInfo("Username", "Deeksha Sethi");

	}

	/**
	 * This method sets up the browser and driver and prints Class name. <br>
	 * <br>
	 * <span style='color:red'><b>Change the className variable in the VARIABLE
	 * DECLARATION section.</b></span> <br>
	 * <br>
	 * 
	 * @exception Exception
	 *                The exception is thrown by the method getDriver(). <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */


	@BeforeMethod(alwaysRun = true)
	public static void initiateBrowser() throws Exception {

		/**
		 * Reading browser name from
		 *					 Properties file
		 * followed by calling 
		 * 				getDriver method 
		 * of DriverUtility Class includes getting 
		 * 				the Base URl 
		 * of Application from 	Properties file 
		 *
		 */

		browserName = PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "browserName");
		DriverUtility.getDriver(browserName);

	}

	/**
	 * This method calls Zip Utility class <br>
	 * <br>
	 *  & then Mail Utility class & sends 
	 *  the zip of the Execution Report 
	 *  through mail                     
	 * @category Utility Class for Functions
	 */

	@AfterSuite(alwaysRun = true)
	public void afterTest() throws Exception {


		/**
		 * Zip Execution Report 
		 * & sends the Email to the Client
		 * @throws Exception
		 *             
		 */

		ZipExecutionResult.zipOutputResult();
		Thread.sleep(2000);
		System.out.println("Zip done succesuufully");


		/**
		 * Sending an e-mail of the Test Execution 
		 * Result with zip of Execution Result
		 * folder as an attachment
		 * 
		 */

		MailUtility.sendEmailToClient();
	}


	/**
	 * This method takes a screenshot of the web page opened by the driver. <br>
	 * <br>
	 * <span style='color:red'><b>The name of the image will be same as the name of
	 * the class (the className variable is the NAME of the image file).</b></span>
	 * <br>
	 * <br>
	 * 
	 * @return Absolute path of the image file. <br>
	 *         <br>
	 * @exception IOException
	 *                This exception is thrown if the directory specified is not
	 *                accessible. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static String testFailTakeScreenshot() throws IOException {

		String path = "ExecutionResult\\FailedScreenshots" + "\\" + className + ".jpg";

		// Take screenshot and store as a file format
		File src = ((TakesScreenshot) DriverUtility.driver).getScreenshotAs(OutputType.FILE);
		File des = new File(path);

		
		/**
		 * Now copy the screenshot to 
		 * desired location using 
		 * copyFile method
		 * 
		 */
				
		FileUtils.copyFile(src, des);

		imagePath = des.getAbsolutePath();

		return "../FailedScreenshots/" + className + ".jpg";
	}

	/**
	 * <span style='color:red'><b>This function changes the color of the string
	 * passed to RED.</b></span> <br>
	 * <br>
	 * 
	 * @param stepName
	 *            - The string to be changed to
	 *            <span style='color:red'><b>RED.</b></span> <br>
	 *            <br>
	 * @return A String appended with HTML tags. <br>
	 *         <br>
	 * @category Utility Class for Functions
	 */
	public static String failStringRedColor(String stepName) {

		html = "<span style='color:red'><b>" + stepName + "</b></span>";
		return html;

	}

	/**
	 * <span style='color:#2E86C1'><b>This function changes the color of the string
	 * passed to BLUE.</b></span> <br>
	 * <br>
	 * 
	 * @param stepName
	 *            - The string to be changed to
	 *            <span style='color:#2E86C1'><b>BLUE.</b></span> <br>
	 *            <br>
	 * @return A String appended with HTML tags. <br>
	 *         <br>
	 * @category Utility Class for Functions
	 */
	public static String normalInfoColor(String stepName) {

		html = "<span style='color:#2E86C1'><b>" + stepName + "</b></span>";
		return html;

	}

	/**
	 * <span style='color:#008000'><b>This function changes the color of the string
	 * passed to GREEN.</b></span> <br>
	 * <br>
	 * 
	 * @param stepName
	 *            - The string to be changed to
	 *            <span style='color:#008000'><b>GREEN.</b></span> <br>
	 *            <br>
	 * @return A String appended with HTML tags. <br>
	 *         <br>
	 * @category Utility Class for Functions
	 */
	public static String passStringGreenColor(String stepName) {

		html = "<span style='color:#008000'><b>" + stepName + "</b></span>";
		return html;

	}

	/**
	 * <span style='color:#F39C12'><b>This function changes the color of the string
	 * passed to YELLOW.</b></span> <br>
	 * <br>
	 * 
	 * @param stepName
	 *            - The string to be changed to
	 *            <span style='color:#F39C12'><b>YELLOW.</b></span> <br>
	 *            <br>
	 * @return A String appended with HTML tags. <br>
	 *         <br>
	 * @category Utility Class for Functions
	 */
	public static String skipStringYellowColor(String stepName) {

		html = "<span style='color:#F39C12'><b>" + stepName + "</b></span>";
		return html;

	}

	/**
	 * This function checks the Title of the page opened. <br>
	 * <br>
	 * 
	 * @param title
	 *            - The expected title is passed in this variable. <br>
	 *            <br>
	 * @exception IOException
	 *                thrown by testFailTakeScreenshot() method. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static void checkTitle(String title,String logStep) throws IOException {

		try {

			assertEquals(DriverUtility.driver.getTitle(), title);

			Reporter.log("Website Opened");

			logger.log(LogStatus.PASS, passStringGreenColor("Correct Website Opened."));

		} catch (AssertionError e) {

			System.out.println(e.getMessage());

			Reporter.log("Incorrect Website Opened");

			logger.log(LogStatus.INFO, failStringRedColor("Site Open"));

			imagePathRelative = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL, failStringRedColor("Error Opening Page : ") + pathForLogger);

			exceptionMsg=e.getMessage();

			throw e;
		}
	}

	/**
	 * This function is to select the locator type from the types available in
	 * Selenium. <br>
	 * <br>
	 * 
	 * @param locator_Type
	 *            - The type of locator used. <br>
	 *            <br>
	 * @param locator_Path
	 *            - The value of the locator. <br>
	 *            <br>
	 * @return An object of By class with the passed locator type. <br>
	 *         <br>
	 * @category Utility Class for Functions
	 */
	public static By selectLocator(String locator_Type, String locator_Path) {

		switch (locator_Type) {

		case "id":
			element = By.id(locator_Path);
			return element;

		case "className":
			element = By.className(locator_Path);
			return element;

		case "cssSelector":
			element = By.cssSelector(locator_Path);
			return element;

		case "linkText":
			element = By.linkText(locator_Path);
			return element;

		case "name":
			element = By.name(locator_Path);
			return element;

		case "partialLinkText":
			element = By.partialLinkText(locator_Path);
			return element;

		case "tagName":
			element = By.tagName(locator_Path);
			return element;

		case "xpath":
			element = By.xpath(locator_Path);
			return element;

		default:
			Reporter.log("Incorrect Locator property Found " + locator_Type);

			logger.log(LogStatus.FAIL, failStringRedColor(locator_Type + " is an Incorrect Locator Type."));

			fail("Incorrect Locator Property " + locator_Type);
		}

		return null;
	}

	/**
	 * Gets the text.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @return the text
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getText(String locator_Type, String locator_Path) throws IOException {


		element = selectLocator(locator_Type, locator_Path);

		String text = DriverUtility.driver.findElement(element).getText();

		return text;
	}

	/**
	 * Gets the list of elements.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @return the list of elements
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static List<WebElement> getListOfElements(String locator_Type, String locator_Path,String logStep) throws IOException {


		element = selectLocator(locator_Type, locator_Path);

		logger.log(LogStatus.PASS, passStringGreenColor(logStep + " - get elements"));
		return DriverUtility.driver.findElements(element);
	}

	/**
	 * Gets the web element.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @return the web element
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static WebElement getWebElement(String locator_Type, String locator_Path,String logStep) throws IOException {


		element = selectLocator(locator_Type, locator_Path);

		logger.log(LogStatus.PASS, passStringGreenColor(logStep + " - get web element"));
		return DriverUtility.driver.findElement(element);
	}

	/**
	 * Check web element present.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean checkWebElementPresent(String locator_Type, String locator_Path) throws IOException {
		try {

			element = selectLocator(locator_Type, locator_Path);

			DriverUtility.driver.findElement(element);
			return true;
		} 

		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Click by javascript.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void clickByJavascript(String locator_Type, String locator_Path,String logStep) throws IOException {
		try {

			element = selectLocator(locator_Type, locator_Path);

			executor = (JavascriptExecutor) DriverUtility.driver;

			executor.executeScript("arguments[0].click();", DriverUtility.driver.findElement(element));

		} 
		catch (Exception e) {
			Reporter.log(logStep + " not Clicked");

			logger.log(LogStatus.INFO, failStringRedColor("Click on Element : " + logStep + " - Failed."));

			imagePathRelative = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + logStep + "on Page :-" + pathForLogger));

			exceptionMsg = e.getMessage();
			throw e;

		}
	}

	/**
	 * This function performs click on the element. <br>
	 * <br>
	 *
	 * @param locator_Type
	 *            - The type of locator used. <br>
	 *            <br>
	 * @param locator_Path
	 *            - The value of the locator. <br>
	 *            <br>
	 * @exception IOException
	 *                thrown by testFailTakeScreenshot() method. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static void clickOnElement(String locator_Type, String locator_Path, String logStep) throws Exception {

		try {
			element = selectLocator(locator_Type, locator_Path);

			DriverUtility.driver.findElement(element).click();

			logger.log(LogStatus.PASS, passStringGreenColor(logStep + "--Passed"));

		} catch (Exception e) {
			Reporter.log(locator_Path + " not Clicked");

			logger.log(LogStatus.INFO, failStringRedColor("Click on Element : " + logStep + " - Failed."));

			imagePathRelative = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL, failStringRedColor("Cannot find " + logStep + " on Page :-" + pathForLogger));

			exceptionMsg = e.getMessage();

			throw e;
		}
	}

	/**
	 * Select by visible text.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @param text
	 *            the text
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void selectByVisibleText(String locator_Type, String locator_Path, String text) throws IOException {

		try {
			element = selectLocator(locator_Type, locator_Path);

			Select dropDown = new Select(DriverUtility.driver.findElement(element));

			dropDown.selectByVisibleText(text);

			logger.log(LogStatus.PASS, passStringGreenColor(locator_Path + " - Selected from drop down" + text));

		} catch (Exception e) {
			Reporter.log(locator_Path + " not Selected");

			logger.log(LogStatus.INFO, failStringRedColor("Select action : " + locator_Path + " - Failed."));

			imagePath = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePath);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + locator_Path + "on Page :-" + pathForLogger));

			throw e;

		}
	}

	/**
	 * Select by value.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @param valueAttribute
	 *            the value attribute
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void selectByValue(String locator_Type, String locator_Path, String valueAttribute)
			throws IOException {

		try {
			element = selectLocator(locator_Type, locator_Path);

			Select dropDown = new Select(DriverUtility.driver.findElement(element));

			dropDown.selectByValue(valueAttribute);

			logger.log(LogStatus.PASS,
					passStringGreenColor(locator_Path + " - Selected from drop down" + valueAttribute));

		} catch (Exception e) {
			Reporter.log(locator_Path + " not Selected");

			logger.log(LogStatus.INFO, failStringRedColor("Select action : " + locator_Path + " - Failed."));

			imagePath = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePath);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + locator_Path + "on Page :-" + pathForLogger));

			throw e;

		}
	}

	/**
	 * Select by index.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @param index
	 *            the index
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void selectByIndex(String locator_Type, String locator_Path, int index) throws IOException {

		try {
			element = selectLocator(locator_Type, locator_Path);

			Select dropDown = new Select(DriverUtility.driver.findElement(element));

			dropDown.selectByIndex(index);

			logger.log(LogStatus.PASS, passStringGreenColor(locator_Path + " - Selected from drop down" + index));

		} catch (Exception e) {
			Reporter.log(locator_Path + " not Selected");

			logger.log(LogStatus.INFO, failStringRedColor("Select action : " + locator_Path + " - Failed."));

			imagePath = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePath);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + locator_Path + "on Page :-" + pathForLogger));

			throw e;

		}
	}

	/**
	 * Switch browser tab.
	 *
	 * @param windowNumber
	 *            the window number
	 * @throws IOException
	 */
	public static void switchBrowserTab(int windowNumber) throws IOException {
		try {
			System.out.println("start");
			Set<String> wind = DriverUtility.driver.getWindowHandles();
			Iterator<String> iter = wind.iterator();
			String firstwindow = iter.next();
			String secondwind = iter.next();
			// this script is used for window handler
			if (windowNumber == 1)
				DriverUtility.driver.switchTo().window(firstwindow);
			else if (windowNumber == 2)
				DriverUtility.driver.switchTo().window(secondwind);
			System.out.println("End");

			logger.log(LogStatus.PASS, passStringGreenColor("Window switched to " + windowNumber));
		} catch (Exception e) {
			Reporter.log("Window not switched.");

			logger.log(LogStatus.INFO, failStringRedColor("Window Switch" + " - Failed."));

			imagePath = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePath);

			logger.log(LogStatus.FAIL, failStringRedColor("Window Switch - " + pathForLogger));

			throw e;

		}

	}

	/**
	 * Scroll down bottom.
	 */
		public static void scrollDownbottom() {
			executor = (JavascriptExecutor) DriverUtility.driver;
			executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		}
	
		/**
		 * Press enter.
		 */
		public static void pressEnter() {
			keyboard = ((RemoteWebDriver) DriverUtility.driver).getKeyboard();
			keyboard.pressKey(Keys.ENTER);
		}

	/**
	 * Scrolling to element of A page.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 */
		public static void scrollingToElementofAPage(String locator_Type, String locator_Path) {
			element = selectLocator(locator_Type, locator_Path);
			executor = (JavascriptExecutor) DriverUtility.driver;
			executor.executeScript("arguments[0].scrollIntoView();",DriverUtility.driver.findElement(element));
		}
	
		/**
		 * Scrolling to element of A page with web element.
		 *
		 * @param webelement
		 *            the web element
		 */
		public static void scrollingToElementOfAPageWithWebElement(WebElement webelement) {
			executor = (JavascriptExecutor) DriverUtility.driver;
			executor.executeScript("arguments[0].scrollIntoView();", webelement);
		}

	/**
	 * This function clears the field specified by the element. <br>
	 * <br>
	 *
	 * @param locator_Type
	 *            - The type of locator used. <br>
	 *            <br>
	 * @param locator_Path
	 *            - The value of the locator. <br>
	 *            <br>
	 * @exception IOException
	 *                thrown by testFailTakeScreenshot() method. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static void clearField(String locator_Type, String locator_Path) throws Exception {

		wait = new WebDriverWait(DriverUtility.driver, PropertiesUtility.readLongFromProperties("Framework.properties", "ExplicitWaitTimeOut"));

		try {
			element = selectLocator(locator_Type, locator_Path);

			DriverUtility.driver.findElement(element).clear();

			logger.log(LogStatus.PASS, passStringGreenColor(locator_Path + " - Cleared."));
		} 

		catch (Exception e) {
			Reporter.log(locator_Path + " not Cleared");

			logger.log(LogStatus.INFO, failStringRedColor("Clear Field for : " + locator_Path + " - Failed."));

			imagePath = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePath);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + locator_Path + "on Page :-" + pathForLogger));

			throw e;
		}
	}

	/**
	 * This function enters data into the field specified by the element. <br>
	 * <br>
	 *
	 * @param locator_Type
	 *            - The type of locator used. <br>
	 *            <br>
	 * @param locator_Path
	 *            - The value of the locator. <br>
	 *            <br>
	 * @param input
	 *            - The value to be input in the field. <br>
	 *            <br>
	 * @exception IOException
	 *                thrown by testFailTakeScreenshot() method. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static void inputData(String locator_Type, String locator_Path, String input,String logStep) throws IOException {


		try {

			element = selectLocator(locator_Type, locator_Path);

			DriverUtility.driver.findElement(element).clear();

			DriverUtility.driver.findElement(element).sendKeys(input);

			logger.log(LogStatus.PASS, passStringGreenColor(logStep + " --Passed."));
		} 

		catch (Exception e)
		{
			Reporter.log("Value not Entered in " + logStep);

			logger.log(LogStatus.INFO, failStringRedColor("Data input for : " + logStep + " - Failed."));

			imagePathRelative = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + logStep + " on Page :-" + pathForLogger));

			exceptionMsg=e.getMessage();

			throw e;
		}
	}

	/**
	 * Input data without clearing.
	 *
	 * @param locator_Type
	 *            the locator type
	 * @param locator_Path
	 *            the locator path
	 * @param input
	 *            the input
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void inputDataWithoutClearing(String locator_Type, String locator_Path, String input,String logStep)
			throws IOException {

		try {

			element = selectLocator(locator_Type, locator_Path);

			DriverUtility.driver.findElement(element).sendKeys(input);

			logger.log(LogStatus.PASS, passStringGreenColor(logStep + "'s Value Entered."));
		} 

		catch (Exception e) 
		{
			Reporter.log("Value not Entered in " + logStep);

			logger.log(LogStatus.INFO, failStringRedColor("Data input for : " + logStep + " - Failed."));

			imagePathRelative= testFailTakeScreenshot();
			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);
			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL,
					failStringRedColor("Cannot find " + logStep + "on Page :-" + pathForLogger));

			exceptionMsg=e.getMessage();

			throw e;
		}
	}

	public static void clickEnterKey(String locator_Type, String locator_Path, String logStep) throws IOException {

		try {

			Robot robot;
			try {
				robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				exceptionMsg = e.getMessage();
			}

			logger.log(LogStatus.PASS, passStringGreenColor(logStep + "--Passed"));
		}

		catch (Exception e) {
			Reporter.log("Value not Entered in " + locator_Path);

			logger.log(LogStatus.INFO, failStringRedColor("Data input for : " + logStep + " - Failed."));

			imagePathRelative = testFailTakeScreenshot();

			// UploadFileFromWindowUtil.uploadFile(imagePathRelative);

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL, failStringRedColor("Cannot find " + logStep + "on Page :-" + pathForLogger));

			exceptionMsg = e.getMessage();

			throw e;
		}
	}
	/**
	 * This function enters data into the field specified by the element. <br>
	 * <br>
	 * 
	 * @param times
	 *            - The number of times to switch the tabs of the OS. <br>
	 *            <br>
	 * @exception AWTException
	 *                thrown by Robot class. <br>
	 *                <br>
	 * @category Utility Class for Functions
	 */
	public static void alt_tab(int times) throws AWTException {

		Reporter.log("Switching Window");
		logger.log(LogStatus.INFO, normalInfoColor("Switching Window"));

		robo = new Robot();

		for (int i = 0; i < times; i++) {

			robo.keyPress(KeyEvent.VK_ALT);
			robo.keyPress(KeyEvent.VK_TAB);
			robo.keyPress(KeyEvent.VK_ENTER);
		}

		Reporter.log("Window Switched");

		logger.log(LogStatus.INFO, normalInfoColor("Window Switched"));
	}

	/**
	 * This function prints the result of the Test Case in the Extent Report. <br>
	 * <br>
	 *
	 * @param result
	 *            - It is an object of ITestResult class. <br>
	 *            <br>
	 * @return the result
	 * @throws Exception 
	 * @category Utility Class for Functions
	 */
	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) throws Exception {


		/**
		 * Making an object of Jira Utility
		 * class to access any method of the
		 *  same class
		 */

		JiraUtility obj=new JiraUtility();

		if (result.getStatus() == ITestResult.FAILURE) {


			/**
			 * Calling JiraUtility method to log 
			 * a bug in Jira tool if the 
			 * test case is failed
			 */


			summary = result.getName() + " - Failed.";
			obj.reportIssue(summary, exceptionMsg, imagePath);

		}  


		else if (result.getStatus() == ITestResult.SUCCESS) {
			//obj.updateJiraTestResults("11772", "Test Case Passed", "Pass");

		}

		extent.endTest(logger);
		Reporter.log("Browser Closed");
		DriverUtility.driver.quit();
	}

	/**
	 * This function prints the result of the Test Class in the Extent Report. <br>
	 * <br>
	 * 
	 * @category Utility Class for Functions
	 */
	@AfterTest(alwaysRun = true)
	public void endReport() {

		extent.flush();
	}

	/**
	 * This function closes the browser. <br>
	 * <br>
	 * 
	 * @category Utility Class for Functions
	 */
/*	@AfterClass(alwaysRun = true)
	public static void tearDown() {

		Reporter.log("Browser Closed");
		DriverUtility.driver.quit();
	}*/


}
