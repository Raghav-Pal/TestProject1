package com.dimagi.utility;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.rcarz.jiraclient.Issue;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import net.rcarz.jiraclient.Field;


public class JiraUtility {

	

	public static String userName = "testingxperts11@gmail.com";
	public static String password = "Damco@123";
	public static String jiraUrl = "https://testingxpert.atlassian.net";
	public static String jiraProject = "TES";
	public static String jiraProjectId = "10001";
	public static String jiraCycleId = "0001525183887463-242ac112-0001";
	public static String versionId = "-1";
	/* zephyr accessKey ,Go to settings then Ad-ons then ZAPI */
	private static String zapiAccessKey = "YmQ2YjBjM2YtMmEwZS0zNGRjLTllYTgtMWEzYmY4Mjg3NGNjIHRlc3Rpbmd4cGVydHMxMSBVU0VSX0RFRkFVTFRfTkFNRQ";
	/* zephyr accessKey ,Go to settings then Ad-ons then ZAPI */
	private static String zapiSecretKey = "_EXll5mjUioRbceX1vqhQBJAKnuLqa7lm-93-wh-a6Y";
	/*
	 * Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI Cloud
	 * Installation
	 */
	private static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	static ZFJCloudRestClient ZAPI_CLIENT;
	private static String API_UPDATE_EXECUTION = "{SERVER}/public/rest/api/1.0/execution/";
	private static String API_GET_EXECUTIONS = "{SERVER}/public/rest/api/1.0/executions/search/cycle/";
	static BasicCredentials creds;
	static JiraClient jira ;
	static Issue issue;

	/* Constructor for initializing different values */
	public JiraUtility() {
	creds = new BasicCredentials(userName, password);
	jira = new JiraClient(jiraUrl, creds);
	//ZAPI_CLIENT = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zapiAccessKey, zapiSecretKey, userName).build();
	}

	public  String reportIssue(String summary, String description, String attachmentPath) {
		
		
		Issue newIssue;
		String bugID = "No Bug Created. Please check Configuration";
		try {
			// create a issue
			System.out.println(jiraProject);
			newIssue = jira.createIssue(jiraProject, "Bug").field(Field.SUMMARY, summary)
					.field(Field.DESCRIPTION, description).field(Field.PRIORITY, "High").execute();

			// add an attachment to the issue created
			if (!attachmentPath.equalsIgnoreCase("RestAPI")) {
				File file = new File(attachmentPath);
				newIssue.addAttachment(file);
			}
			bugID = newIssue.getId();
			return bugID;

		} catch (JiraException e) {
			e.printStackTrace();
		}
		return bugID;

	}

	/**
	 * 
	 * @param ExecutionID:
	 *            ID is the number which we get when the testcase is added to a
	 *            Testcycle and do inspect element and in the html serach for
	 *            "data-issueid"
	 * @param Comments:
	 *            String
	 * @param result
	 *            : Fail
	 */
	public  void updateJiraTestResults(String ExecutionID, String Comments, String Result) throws Exception {

		Map<String, String> executionIds = getExecutionsID();
		// creating the Rest object to pass through REST API for Updating the test case
		// Result
		JSONObject statusObj = new JSONObject();
		String statusID = "1";
		if (Result.equalsIgnoreCase("Fail"))
			statusID = "2";
		statusObj.put("id", statusID);
		JSONObject executeTestsObj = new JSONObject();
		executeTestsObj.put("status", statusObj);
		executeTestsObj.put("cycleId", jiraCycleId);
		executeTestsObj.put("projectId", jiraProjectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("comment", Comments);
		final String updateExecutionUri = API_UPDATE_EXECUTION.replace("{SERVER}", zephyrBaseUrl)
				+ executionIds.get(ExecutionID);
		executeTestsObj.put("issueId", ExecutionID);
		StringEntity executeTestsJSON;
		executeTestsJSON = new StringEntity(executeTestsObj.toString());
		// call the function to update the result
		updateExecutions(updateExecutionUri, ZAPI_CLIENT, zapiAccessKey, executeTestsJSON);

	}

	private static Map<String, String> getExecutionsID() {
		Map<String, String> executionIds = new HashMap<String, String>();
		try {
			final String getExecutionsUri = API_GET_EXECUTIONS.replace("{SERVER}", zephyrBaseUrl) + jiraCycleId
					+ "?projectId=" + jiraProjectId + "&versionId=-1";
			executionIds = getExecutionsByCycleId(getExecutionsUri, ZAPI_CLIENT, zapiAccessKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionIds;
	}

	private static String updateExecutions(String uriStr, ZFJCloudRestClient client, String accessKey,StringEntity executionJSON)
	
	{
		String executionStatus = "No Test Executed";
		try {

			URI uri = new URI(uriStr);
			int expirationInSec = 360;
			JwtGenerator jwtGenerator = client.getJwtGenerator();
			String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);

			HttpResponse response=null;
			
			HttpClient restClient =new DefaultHttpClient();

			HttpPut executeTest = new HttpPut(uri);
			executeTest.addHeader("Content-Type", "application/json");
			executeTest.addHeader("Authorization", jwt);
			executeTest.addHeader("zapiAccessKey", accessKey);
			executeTest.setEntity(executionJSON);
			response = restClient.execute(executeTest);

			// get the status of the rest
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (statusCode >= 200 && statusCode < 300) {
				String string = null;
				try {
					string = EntityUtils.toString(entity);
					JSONObject executionResponseObj = new JSONObject(string);
					JSONObject descriptionResponseObj = executionResponseObj.getJSONObject("execution");
					JSONObject statusResponseObj = descriptionResponseObj.getJSONObject("status");
					executionStatus = statusResponseObj.getString("description");
					System.out.println(executionResponseObj.get("issueKey") + "--" + executionStatus);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				try {
					throw new ClientProtocolException("Unexpected response status: " + statusCode);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionStatus;
	}

	private static Map<String, String> getExecutionsByCycleId(String uriStr, ZFJCloudRestClient client,
			String accessKey) throws URISyntaxException, JSONException {
		Map<String, String> executionIds = new HashMap<String, String>();
		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		System.out.println(uri.toString());
		 System.out.println(jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", jwt);
		httpGet.setHeader("zapiAccessKey", accessKey);

		try {
			response = restClient.execute(httpGet);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);

		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity1 = response.getEntity();
			String string1 = null;

			try {
				string1 = EntityUtils.toString(entity1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// System.out.println(string1);
			JSONObject allIssues = new JSONObject(string1);
			JSONArray IssuesArray = allIssues.getJSONArray("searchObjectList");
			// System.out.println(IssuesArray.length());
			if (IssuesArray.length() == 0) {
				return executionIds;
			}
			for (int j = 0; j <= IssuesArray.length() - 1; j++) {
				JSONObject jobj = IssuesArray.getJSONObject(j);
				JSONObject jobj2 = jobj.getJSONObject("execution");
				String executionId = jobj2.getString("id");
				long IssueId = jobj2.getLong("issueId");
				// executionIds.put(executionId, String.valueOf(IssueId));

				executionIds.put(String.valueOf(IssueId), executionId);
			}
		}
		return executionIds;
	}
}
