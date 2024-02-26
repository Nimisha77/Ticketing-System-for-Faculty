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

public class GET_user_userid_tickets_ticketid {

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
    
    //Purpose: Ensures that you get an error if the user id is nonexisting (404 error)
    @Test
    public void tsfsTest1() throws Exception {
        deleteTickets("jdoe10");
        try {
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            String missingUser = "jdoe100asdfghjkl3456zxcvbnm";
            response = getTicket(missingUser, ticketid, true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: ensures that user id is not an integer and an error is returned if it is
    @Test
    public void tsfsTest2() throws Exception {
        deleteTickets("jdoe10");
        try {
            //user id is an integer
            int missingUser = 10;
            CloseableHttpResponse response0 =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            int ticketid = Integer.parseInt(getIdFromResponse(response0));
            response0.close();
            
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + missingUser + "/tickets/" + ticketid);
            httpRequest.addHeader("accept", "application/json");
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
            }
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that you get an error if the ticket id is nonexisting (404 error)
    @Test
    public void tsfsTest3() throws Exception {
        deleteTickets("jdoe10");
        try {
            
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            int missingTicket = ticketid * 999;
            response = getTicket("jdoe10", missingTicket, true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: this ensures that ticket id is not a string and an error is returned if it is
    @Test
    public void tsfsTest4() throws Exception {
        deleteTickets("jdoe10");
        try {
       
            CloseableHttpResponse response0 =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            
            //ticket id is a string here
            String ticketid = (getIdFromResponse(response0));
            response0.close();
            
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "jdoe10" + "/tickets/" + ticketid);
            httpRequest.addHeader("accept", "application/json");
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
            }

            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }
    
    //Purpose: Ensures that unauthenticated user throws a 401 error. 
    @Test
    public void tsfsTest5() throws Exception {
        deleteTickets("jdoe10");
        try {
            
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            response.close();

            //setting user as unauthenticated 
            boolean isAuthenticated = false;
            response = getTicket("jdoe10", ticketid, isAuthenticated);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: checks for valid get ticket with proper userid and ticketid
    @Test
    public void tsfsTest6() throws Exception {
        deleteTickets("jdoe10");
        httpclient = HttpClients.createDefault();

        try {
            String userid = "jdoe10";
            
            CloseableHttpResponse response = createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"});
            int ticketid = Integer.parseInt(getIdFromResponse(response));
            // EntityUtils.consume(response.getEntity());
            response.close();

            response = getTicket(userid, ticketid, true);

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

            String expectedJson = "{\"id\":\"" + ticketid + "\",\"title\":\"My Ticket\",\"description\":\"My problem is xyz\",\"status\":\"open\",\"category\":\"miscellaneous\",\"emailAddress\":\"jdoe10@gatech.edu\", \"assignee\":\"pkim20\", \"watchers\":[\"mriley3\"]}";
	    
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    
    
    //***** MY PRIVATE METHODS ********
    private CloseableHttpResponse createTicket(String userid, String title, String description, String category, String emailAddress, String assignee, String[] watchers) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
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

    private CloseableHttpResponse getTicket(String userid, int ticketid, boolean isAuthenticated) throws IOException {
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

    private CloseableHttpResponse deleteTickets(String userid) throws IOException {
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/user/" + userid + "/tickets");
        httpDelete.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println("*** Raw response " + response + "***");
        // EntityUtils.consume(response.getEntity());
        // response.close();
        return response;
    }

    private String removeDateFieldsFromJsonStr(String str) throws JSONException {
        JSONObject object = new JSONObject(str);
        object.remove("createdDate");
        object.remove("modifiedDate");

        return object.toString();
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

}
