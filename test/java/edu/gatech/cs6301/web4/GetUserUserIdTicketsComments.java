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

public class GetUserUserIdTicketsComments {
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

    // // Purpose: Test GET_user_userid_tickets with non-existing user id
    // @Test
    // public void tsfsTest1() throws Exception {
    //     try {
    //         CloseableHttpResponse response = getTickets("abcdefg123");

    //         int status = response.getStatusLine().getStatusCode();
    //         Assert.assertEquals(404, status);
    //         response.close();
    //     } finally {
    //         httpclient.close();
    //     }
    // }

    // Purpose: Test GET_user_userid_tickets with existing user id
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets();
        String expectedJson = "";
        try {
            // Create Ticket 1
            Date curDate = new Date();
            String title = "Ticket Title";
            String desc = "Ticket desc";
            String status = "open";
            String category ="travel authorization";
            String gtid = "gtid";
            String email = gtid+"@gatech.edu";
            CloseableHttpResponse response = createTicket(title, desc, status, category, email, gtid, curDate);
            String ticketid = getIdFromResponse(response);
            response.close();

            CloseableHttpResponse response2 = createTicketComment(gtid, ticketid,"{\"author\":\""+gtid+"\",\"createdDate\":\""+curDate+",\"content\":\"comment1\"}");
            Assert.assertEquals(404, response2.getStatusLine().getStatusCode());
            response2.close();

            CloseableHttpResponse response3 = createTicketComment(gtid, ticketid,"{\"author\":\""+gtid+"\",\"createdDate\":\""+curDate+",\"content\":\"comment2\"}");
            Assert.assertEquals(404, response3.getStatusLine().getStatusCode());
            response3.close();

            expectedJson += "[{\"author\":\""+gtid+"\",\"createdDate\":\""+curDate+",\"content\":\"comment1\"},{\"author\":\""+gtid+"\",\"createdDate\":\""+curDate+",\"content\":\"comment2\"}]";
            CloseableHttpResponse response4 = getTicketsComments(gtid, ticketid);
            if (response.getStatusLine().getStatusCode() == 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            HttpEntity entity = response4.getEntity();
            String strResponse = EntityUtils.toString(entity);
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response4.getEntity());

            response4.close();
        } finally {
            httpclient.close();
        }
    }

     private CloseableHttpResponse deleteTickets() throws IOException {
         HttpDelete httpDelete = new HttpDelete(baseUrl + "/user/gtid/tickets");
         httpDelete.addHeader("accept", "application/json");

         System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
         CloseableHttpResponse response = httpclient.execute(httpDelete);
         System.out.println("*** Raw response " + response + "***");
         // EntityUtils.consume(response.getEntity());
         // response.close();
         return response;
     }

    private CloseableHttpResponse createTicket(String title, String description, String status, String category, String emailAddress, String assignee, Date curDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+assignee+"/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," + "\"emailAddress\":\"" + emailAddress +
                "\"assignee\":\"" + assignee + "\"watchers\":\"" + "[\""+assignee+"\"]" +
                "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createTicketComment(String assignee, String ticketid, String payload) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+assignee+"/tickets/"+ticketid + "/comments");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(payload);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    // private CloseableHttpResponse getTickets(String userId) throws IOException {
    //     String requestUrl = "/user/" + userId + "/tickets";
    //     HttpGet httpRequest = new HttpGet(baseUrl + requestUrl);
    //     httpRequest.addHeader("accept", "application/json");

    //     System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
    //     CloseableHttpResponse response = httpclient.execute(httpRequest);
    //     System.out.println("*** Raw response " + response + "***");
    //     return response;
    // }

    private CloseableHttpResponse getTicketsComments(String userId, String ticketid) throws IOException {
        String requestUrl = "/user/" + userId + "/tickets/"+ticketid+"/comments";
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
