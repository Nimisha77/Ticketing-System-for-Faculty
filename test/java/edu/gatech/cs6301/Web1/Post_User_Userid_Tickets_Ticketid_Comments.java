package edu.gatech.cs6301.Web1;

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

public class Post_User_Userid_Tickets_Ticketid_Comments {

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

    private String getCurrentDateString(long hoursOffset) {
        return java.time.OffsetDateTime.now().plusHours(hoursOffset).toString();
    }
    

    // Purpose: Test invalid ticket id
    @Test
    public void tsfsTest1() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", -1, "aa1", 
                getCurrentDateString(0), "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test invalid user id ticket id relationship
    @Test
    public void tsfsTest2() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "aa2", 
                getCurrentDateString(0), "This is content");
                // assume aa1 does not own ticket 1
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test empty author
    @Test
    public void tsfsTest3() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "", 
                getCurrentDateString(0), "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test invalid date format
    @Test
    public void tsfsTest4() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "aa1", 
                "getCurrentDateString(0)", "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test too early date
    @Test
    public void tsfsTest5() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "aa1", 
                getCurrentDateString(-1), "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test too late date
    @Test
    public void tsfsTest6() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "aa1", 
                getCurrentDateString(1), "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test empty content
    @Test
    public void tsfsTest7() {
        try {
            CloseableHttpResponse response = createComment(
                "aa1", 1, "aa1", 
                getCurrentDateString(0), "");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Purpose: Test valid comment
    @Test
    public void tsfsTest8() {
        try {
            CloseableHttpResponse ticketResponse = createTicket(
                "aa1", "title", "description", "open", "travel authorization",
                "e@a.com", "", new String[]{},
                getCurrentDateString(0), getCurrentDateString(0));
            int ticketStatus = ticketResponse.getStatusLine().getStatusCode();
            Assert.assertEquals(201, ticketStatus);
            // get ticket id
            HttpEntity entity = ticketResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            JSONObject json = new JSONObject(responseString);
            int ticketId = json.getInt("id");
            CloseableHttpResponse response = createComment(
                "aa1", ticketId, "aa1", 
                getCurrentDateString(0), "This is content");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(201, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private CloseableHttpResponse createTicket(
        String userid, String title, String description, String status, String category,
        String emailAddress, String assignee, String[] watchers,
        String createdDate, String modifiedDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(
                "\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + watchers + "\"," +
                "\"createdDate\":\"" + createdDate + "\"," +
                "\"modifiedDate\":\"" + modifiedDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createComment(String userid, long ticketid, String author, String createdDate, String content) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets" + ticketid + "/comments");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(
                "\"author\":\"" + author + "\"," +
                "\"createdDate\":\"" + createdDate + "\"," +
                "\"content\":\"" + content + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
}
