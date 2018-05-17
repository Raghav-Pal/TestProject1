package com.dimagi.tests;

import java.io.IOException;

import org.testng.annotations.Test;

import com.dimagi.applicationutility.AppUtility;
import com.dimagi.locators.Locators;
import com.dimagi.utility.ExcelUtility;
import com.dimagi.utility.FunctionUtility;
import com.dimagi.utility.PropertiesUtility;
import com.relevantcodes.extentreports.LogStatus;

public class AdminLogin extends FunctionUtility{
	
	
	/** The this class. */
	@SuppressWarnings("rawtypes")
	static Class thisClass = AdminLogin.class;
	
	
	/** The test case ID. */
	static String testCaseID = thisClass.getSimpleName();
	
	@Test
	public void loginAsAdmin() throws Exception 
	{
		
		/**
		 * Taking Class name in the testCaseID 
		 * & passing the parameter in the extent report
		 * along with the description by calling extent report 
		 * methods of Functions Utility Class
		 * 
		 */
		
		FunctionUtility.className=testCaseID;
		logger = extent.startTest(testCaseID);
		logger.setDescription("This Test method is used to login ICDS-CAS Application ");
		logger.getDescription();
		
		
		String userName=ExcelUtility.readFromExcel(
				System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "ExcelInputFilePath"), "Login", 2, 1);
		
		Thread.sleep(2000);
		
		String password=ExcelUtility.readFromExcel(
				System.getProperty("user.dir") + PropertiesUtility.readStringFromProperties(System.getProperty("user.dir") + "\\Resources\\Framework.properties", "ExcelInputFilePath"), "Login", 2, 2);
		
		Thread.sleep(2000);
	
		/**
		 * AppUtility includes Application specific
		 * functions & calling login function from the 
		 * same Class
		 * 
		 */
		
		AppUtility.login(userName,password,"Login with valid credentials");
		
		/**
		 * Writing Output result 
		 * into Extent Report
		 * 
		 */
		logger.log(LogStatus.PASS, passStringGreenColor("Test to login into the Application as an Admin is working fine"));
	}
	
	
	


}
