package com.dimagi.utility;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class FetchExecuteUpdate {

	

	private static String API_SEARCH_ISSUES = "{SERVER}/rest/api/2/search";
	private static String API_ADD_TESTS = "{SERVER}/public/rest/api/1.0/executions/add/cycle/";
	private static String API_GET_EXECUTIONS = "{SERVER}/public/rest/api/1.0/executions/search/cycle/";
	private static String API_UPDATE_EXECUTION = "{SERVER}/public/rest/api/1.0/execution/";

	/** Declare JIRA,Zephyr URL,access and secret Keys */

	/* JIRA Cloud URL of the instance*/
	private static String jiraBaseURL = "https://testingxpert.atlassian.net";
	/* Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI Cloud Installation*/
	private static String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	/* zephyr accessKey ,Go to settings then Ad-ons then ZAPI*/
	private static String accessKey = "YmQ2YjBjM2YtMmEwZS0zNGRjLTllYTgtMWEzYmY4Mjg3NGNjIHRlc3Rpbmd4cGVydHMxMSBVU0VSX0RFRkFVTFRfTkFNRQ";
	/* zephyr accessKey ,Go to settings then Ad-ons then ZAPI*/
	private static String secretKey = "_EXll5mjUioRbceX1vqhQBJAKnuLqa7lm-93-wh-a6Y";

	/** Declare parameter values here */
	private static String userName = "testingxperts11@gmail.com";
	private static String password = "Damco@123";

	/*After creating Test cycle inspect Test Case Cycle created an element: <li id ="cycle-0001503993710547-242ac112-0001">*/
	private static String cycleId = "0001525183887463-242ac112-0001";
	private static String versionId = "-1";

	/*Go to settings then Project ,hover the mouse on it will give Project id at the bottom*/
	private static String projectId = "10001"; 

	static ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
			.build();
	JwtGenerator jwtGenerator = client.getJwtGenerator();

	public static void main(String[] args) throws JSONException, URISyntaxException,IOException {

		final String issueSearchUri = API_SEARCH_ISSUES.replace("{SERVER}", jiraBaseURL);

		Map<String, String> executionIds = new HashMap<String, String>();
		final String getExecutionsUri = API_GET_EXECUTIONS.replace("{SERVER}", zephyrBaseUrl) + cycleId + "?projectId="
				+ projectId + "&versionId=" + versionId;

		executionIds = getExecutionsByCycleId(getExecutionsUri, client, accessKey);

		/**
		 * Bulk Update Executions with Status by Execution Id
		 * 
		 */

		JSONObject statusObj = new JSONObject();
		//id = 1 --> pass
		//id = 2 --> fail

		statusObj.put("id", "1");

		JSONObject executeTestsObj = new JSONObject();
		executeTestsObj.put("status", statusObj);
		executeTestsObj.put("cycleId", cycleId);
		executeTestsObj.put("projectId", projectId);
		executeTestsObj.put("versionId", versionId);
		executeTestsObj.put("comment", "Executed by ZAPI Cloud");

		for (String key : executionIds.keySet()) {
			final String updateExecutionUri = API_UPDATE_EXECUTION.replace("{SERVER}", zephyrBaseUrl) + key;
			// System.out.println(updateExecutionUri);
			// System.out.println(executionIds.get(key));
			executeTestsObj.put("issueId", executionIds.get(key));
			// System.out.println(executeTestsObj.toString());
			StringEntity executeTestsJSON = null;
			try {
				executeTestsJSON = new StringEntity(executeTestsObj.toString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			updateExecutions(updateExecutionUri, client, accessKey, executeTestsJSON);
		}

	}

	private static Map<String, String> getExecutionsByCycleId(String uriStr, ZFJCloudRestClient client,
			String accessKey) throws URISyntaxException, JSONException {
		Map<String, String> executionIds = new HashMap<String, String>();
		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		// System.out.println(uri.toString());
		// System.out.println(jwt);

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
			}catch (IOException e) {
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
				executionIds.put(executionId, String.valueOf(IssueId));
			}
		}
		return executionIds;
	}
	public static String updateExecutions(String uriStr, ZFJCloudRestClient client, String accessKey,
			StringEntity executionJSON) throws URISyntaxException, JSONException, IOException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);
		// System.out.println(uri.toString());
		// System.out.println(jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPut executeTest = new HttpPut(uri);
		executeTest.addHeader("Content-Type", "application/json");
		executeTest.addHeader("Authorization", jwt);
		executeTest.addHeader("zapiAccessKey", accessKey);
		executeTest.setEntity(executionJSON);

		try {
			response = restClient.execute(executeTest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// System.out.println(statusCode);
		String executionStatus = "No Test Executed";
		// System.out.println(response.toString());
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
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			try {
				String string = null;
				string = EntityUtils.toString(entity);
				JSONObject executionResponseObj = new JSONObject(string);
				cycleId = executionResponseObj.getString("clientMessage");
				// System.out.println(executionResponseObj.toString());
				throw new ClientProtocolException("Unexpected response status: " + statusCode);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return executionStatus;
	}
}
