package edu.gatech.cs6301.Web1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.boot.test.context.SpringBootTest;


public class GET_user_userid_tickets {

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


    // Purpose: To check invalid userid
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse ticket = getTickets("invalid");
            int status = ticket.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
        } finally {
            httpclient.close();
        }
    }


    // Purpose: To check valid ticketID
    @Test
    public void tsfsTest2() throws Exception {
        try {
            String userId = "User2";
            String title = "Title2";
            String description = "Description2";
            String ticket_status = "open";
            String category = "travel authorization";
            String emailAddress = "aa@aa.com";
            String assignee = "Assignee2";
            String[] watchers = new String[]{};
            String createdDate = "2023-03-02T00:29:39.116Z";
            String modifiedDate = "2023-03-02T00:29:39.116Z";
            CloseableHttpResponse ticket = createTicket(
                userId, title, description, ticket_status, category,
                emailAddress, assignee, watchers, createdDate, modifiedDate
            );
            ticket.close();

            ticket = getTickets("User2");

            int status = ticket.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = ticket.getEntity();
            } else {
                Assert.fail();
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + ticket.getStatusLine().getStatusCode() + ") ***");
            String ticketid = getIdFromStringResponse(strResponse);
            String expectedJson = "{\"id:\":\"" + ticketid + "\"," +
                "\"title:\":\"" + title + "\"," +
                "\"description:\":\"" + description + "\"," +
                "\"status:\":\"" + status + "\"," +
                "\"category:\":\"" + category + "\"," +
                "\"email:\":\"" + emailAddress + "\"," +
                "\"assignee:\":\"" + assignee + "\"," +
                "\"watchers:\":[]," +
                "\"createdDate\":\"" + createdDate + "\"," +
                "\"modifiedDate\":\"" + modifiedDate + "\"}";

            // Original had 3rd parameter "false", but maybe should be "true"
	        JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(ticket.getEntity());
            ticket.close();

            
        } finally {
            httpclient.close();
        }
    }


    private CloseableHttpResponse createTicket(String userId, String title, String description, String status, String category,
    String emailAddress, String assignee, String[] watchers, String createdDate, String modifiedDate) throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "tickets/");
        httpRequest.addHeader("accept", "application/json");
        try {
            StringEntity input = new StringEntity("{" +
                "\"Title:\":\"" + title + "\"," +
                "\"Description:\":\"" + description + "\"," +
                "\"Status:\":\"" + status + "\"," +
                "\"Category:\":\"" + category + "\"," +
                "\"Email:\":\"" + emailAddress + "\"," +
                "\"Assignee:\":\"" + assignee + "\"," +
                "\"Watchers:\":[]," +
                "\"created Date\":\"" + createdDate + "\"," +
                "\"modified Date\":\"" + modifiedDate + "\"}");
            input.setContentType("application/json");
            httpRequest.setEntity(input);
        } catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }


        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
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


    private CloseableHttpResponse getTickets(String userId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

}