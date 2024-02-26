package edu.gatech.cs6301.web4;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class GetUserUserIdTickets {
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

    // Purpose: Test GET_user_userid_tickets with non-existing user id
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse response = getTickets("abcdefg123");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test GET_user_userid_tickets with existing user id
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets();
        String expectedJson = "";
        try {
            // Create Ticket 1 under id yji319
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", curDate);
            String id1 = getIdFromResponse(response);
            expectedJson += "[{\"id\":\"" + id1 + "\",\"title\":\"Test Ticket 1\",\"description\":\"This is a test ticket 1\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"yji319@gatech.edu\",\"assignee\":\"yji319\",\"watchers\":[\"yji319\"],";
            expectedJson += "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}";
            response.close();
            // Create Ticket 2 under id yji319
            response = createTicket("Test Ticket 2", "This is a test ticket 2", "closed", "reimbursement", "yji319@gatech.edu", "yji319", curDate);
            String id2 = getIdFromResponse(response);
            expectedJson += ",{\"id\":\"" + id2 + "\",\"title\":\"Test Ticket 2\",\"description\":\"This is a test ticket 2\",\"status\":\"closed\",\"category\":\"reimbursement\",\"emailAddress\":\"yji319@gatech.edu\",\"assignee\":\"yji319\",\"watchers\":[\"yji319\"],";
            expectedJson += "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}]";
            response.close();

            response = getTickets("yji319");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse deleteTickets() throws IOException {
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/user/yji319/tickets");
        httpDelete.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println("*** Raw response " + response + "***");
        // EntityUtils.consume(response.getEntity());
        // response.close();
        return response;
    }

    private CloseableHttpResponse createTicket(String title, String description, String status, String category, String emailAddress, String assignee, Date curDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/yji319/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," + "\"emailAddress\":\"" + emailAddress +
                "\"assignee\":\"" + assignee + "\"watchers\":\"" + "[\"yji319\"]" +
                "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse getTickets(String userId) throws IOException {
        String requestUrl = "/user/" + userId + "/tickets";
        HttpGet httpRequest = new HttpGet(baseUrl + requestUrl);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private String getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        return getIdFromStringResponse(strResponse);
    }

    private String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()) {
            String key = keyList.next();
            if (key.equals("id")) {
                return object.get(key).toString();
            }
        }
        return null;
    }
}
