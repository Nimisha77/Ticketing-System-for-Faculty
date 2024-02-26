package edu.gatech.cs6301.devops345;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class GET_user_userid_tickets_ticketid_comments {

    private String baseUrl = "http://localhost:8080";
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;
    
    @Before
    public void runBefore() {
	if (!setupdone) {
	    System.out.println("*** SETTING UP TESTS ***");
	    // Increase max total connection to 100
	    cm.setMaxTotal(100);
	    // Increase default max connection per route to 20
	    cm.setDefaultMaxPerRoute(10);
	    // Increase max connections for localhost:80 to 50
	    HttpHost localhost = new HttpHost("locahost", 8080);
	    cm.setMaxPerRoute(new HttpRoute(localhost), 10);
	    httpclient = HttpClients.custom().setConnectionManager(cm).build();
	    setupdone = true;
	}
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }

    // *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***
    
    //Purpose: check if user is valid
    @Test
    public void tsfsTest1() throws Exception {

        try {
            CloseableHttpResponse response =
		createTicket("sfazulbhoy3", "Issue Ticket", "I need access to canvas", "miscellaneous", "sfazulbhoy3@gatech.edu" , "gburdell3", new String[] {"jdoe10"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            String missingUser = "sfazulbhoy3asfhfeudhs10";
            response = getTicket(missingUser, ticketid);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Check if TicketID is invalid
    public void tsfsTest2() throws Exception {

        try {
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "sfazulbhoy3" + "/tickets/" + "ticket_id_should_not_be_string");
            httpRequest.addHeader("accept", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Valid TicketID but ticketID does not exist
    @Test
    public void tsfsTest3() throws Exception {

        try {
       
            int ticketid = 1234;
            
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "sfazulbhoy3" + "/tickets/" + ticketid);
            httpRequest.addHeader("accept", "application/json");

            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode(); 
            Assert.assertEquals(404, status); 
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: checks for valid get ticket id and valid userid + authorsied user
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();

        try {
            String userid = "sfazulbhoy3";
            
            CloseableHttpResponse response = createTicket("sfazulbhoy3", "Issue Ticket", "I need access to canvas", "miscellaneous", "sfazulbhoy3@gatech.edu" , "gburdell3", new String[] {"jdoe10"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            response = createComment("sfazulbhoy3", ticketid, "This is just a first comment");

            response = getComments(userid, ticketid, true);
            int commentid = Integer.parseInt(getIdFromResponse(response));

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = removeDateFieldsFromJsonStr(EntityUtils.toString(entity));

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = "{\"id\":\"" + commentid + "\",\"content\":\"This is just a first comment\",\"author\":\"sfazulbhoy3@gatech.edu\"}";
	    
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Checks that request fauls with 401 if the user is not authenticated.
    @Test
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();

        try {
            CloseableHttpResponse response = createTicket("sfazulbhoy3", "Issue Ticket", "I need access to canvas", "miscellaneous", "sfazulbhoy3@gatech.edu" , "gburdell3", new String[] {"jdoe10"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            response = createComment("sfazulbhoy3", ticketid, "This is just a first comment");

            response = getComments("sfazulbhoy3", ticketid, false);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, status); 
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    
    
    //***** MY PRIVATE METHODS ********
    private CloseableHttpResponse createTicket(String userid, String title, String description, String category, String emailAddress, String assignee, String[] watchers) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," + 
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + watchers + "\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse getTicket(String userid, int ticketid) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userid + "/tickets/" + ticketid);
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse getComments(String userid, int ticketid, boolean isAuthenticated) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userid + "/tickets/" + ticketid);
        httpRequest.addHeader("accept", "application/json");
        if (isAuthenticated) {
            httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        }
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createComment(String userid, int ticketid, String description) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/" + userid + "/tickets/" + ticketid + "/comments");
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        StringEntity input = new StringEntity("{\"author\":\"" + userid + "\"," +
                "\"content\":\"" + description + "\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    // MY PRIVATE METHODS END ***********
    

    private String getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id = getIdFromStringResponse(strResponse);

        return id;
    }

    private String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()){
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.get(key).toString();
            }
        }
        return id;
    }

    private String removeDateFieldsFromJsonStr(String str) throws JSONException {
        JSONObject object = new JSONObject(str);
        object.remove("createdDate");

        return object.toString();
    }

}
