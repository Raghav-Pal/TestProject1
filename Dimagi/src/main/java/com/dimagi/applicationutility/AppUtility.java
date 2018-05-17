package com.dimagi.applicationutility;

import java.io.IOException;

import org.testng.Reporter;

import com.dimagi.locators.Locators;
import com.dimagi.utility.ExcelUtility;
import com.dimagi.utility.FunctionUtility;
import com.relevantcodes.extentreports.LogStatus;

import junit.framework.Assert;

public class AppUtility extends FunctionUtility {

	public static void login(String user, String pass, String logStep) throws Exception {

		try {

			FunctionUtility.inputData("xpath", Locators.USERNAME, user, "Username entered ");
			Thread.sleep(3000);

			FunctionUtility.inputData("xpath", Locators.PASSWORD, pass, "Password entered ");
			Thread.sleep(3000);

			FunctionUtility.clickOnElement("xpath", Locators.SIGN_IN, "Click on Signin Button");
			Thread.sleep(3000);

			Assert.assertEquals("ICDS-CAS Dashboard", getText("xpath", Locators.LOGIN_ASSERT));

		} catch (Exception e) {

			imagePathRelative = testFailTakeScreenshot();

			logger.log(LogStatus.FAIL, failStringRedColor("Test Case Failure Reason :- ") + "\n" + e);

			pathForLogger = logger.addScreenCapture(imagePathRelative);

			logger.log(LogStatus.FAIL, failStringRedColor(logStep + " in the Application :-" + pathForLogger));

			logger.log(LogStatus.INFO, failStringRedColor(logStep + " -- Test Case Failed."));

			exceptionMsg = e.getMessage();
			throw e;
		}

	}
}