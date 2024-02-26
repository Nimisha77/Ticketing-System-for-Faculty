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


    // Purpose: To check invalid ticketID
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse ticket = getTicket("User2", -333);
            int status = ticket.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
        } catch (Exception e) {
            Assert.fail();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: To test invalid relation to user. (Should fail if we test grabbing ticket1 as User2)
    @Test
    public void tsfsTest2() throws Exception {
        try {
            CloseableHttpResponse ticket1 = createTicket("User1", "Title1", "Description1", "open", "travel authorization", "t@aa.com",
            "Assignee1", new String[]{}, "2023-03-02T00:29:39.116Z", "2023-03-02T00:29:39.116Z");
            String id1 = getIdFromStringResponse(EntityUtils.toString(ticket1.getEntity()));
            int idInt = Integer.parseInt(id1);
            ticket1.close();
            CloseableHttpResponse ticket2 = createTicket("User2", "Title2", "Description2", "open", "travel authorization", "t2@aa.com",
            "Assignee2", new String[]{"User1"}, "2023-03-02T00:29:39.116Z", "2023-03-02T00:29:39.116Z");
            ticket2.close();

            CloseableHttpResponse isValid = getTicketComments("User2", idInt);
            int status = isValid.getStatusLine().getStatusCode();

            Assert.assertEquals(401, status);


        } catch (Exception e) {
            Assert.fail();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: To test valid relation to user. (Should pass if we test grabbing ticket2 as User1)
    @Test
    public void tsfsTest3() throws Exception {
        try {
            CloseableHttpResponse ticket1 = createTicket("User1", "Title1", "Description1", "open", "travel authorization", "t@aa.com",
            "Assignee1", new String[]{}, "2023-03-02T00:29:39.116Z", "2023-03-02T00:29:39.116Z");
            String id1 = getIdFromStringResponse(EntityUtils.toString(ticket1.getEntity()));
            int idInt = Integer.parseInt(id1);
            ticket1.close();

            CloseableHttpResponse isValid = getTicketComments("User1", idInt);
            int status = isValid.getStatusLine().getStatusCode();

            Assert.assertEquals(200, status);

            String expected = "[]";
            String actual = EntityUtils.toString(isValid.getEntity());
            JSONAssert.assertEquals(expected, actual, false);
        } catch (Exception e) {
            Assert.fail();
        } finally {
            httpclient.close();
        }
    }


    private CloseableHttpResponse createTicket(String userId,String title, String description, String status, String category,
    String emailAddress, String assignee, String[] watchers, String createdDate, String modifiedDate) throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "tickets/");
        httpRequest.addHeader("accept", "application/json");
        try {
                StringEntity input = new StringEntity("{" +
                    "\"title:\":\"" + title + "\"," +
                    "\"description:\":\"" + description + "\"," +
                    "\"status:\":\"" + status + "\"," +
                    "\"category:\":\"" + category + "\"," +
                    "\"emailAddress:\":\"" + emailAddress + "\"," +
                    "\"assignee:\":\"" + assignee + "\"," +
                    "\"watchers:\":\"" + watchers + "\"," +
                    "\"createdDate\":\"" + createdDate + "\"," +
                    "\"modifiedDate\":\"" + modifiedDate + "\"}");
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

    private CloseableHttpResponse getTicket(String userId, int ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse getTicketComments(String userId, int ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId + "/comments");
        httpRequest.addHeader("accept", "application/json");

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
}