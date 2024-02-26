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

public class PUT_user_userid_tickets_ticketid {

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

    @Test
    // Purpose: Check that PUT request on a ticket fails if an invalid userid is provided.
    public void tsfsTest1() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("invalid_user", ticketid, "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if a non-integer ticketid is provided.
    public void tsfsTest2() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", -1, "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if a integer ticketid that is invalid is provided.
    public void tsfsTest3() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", 1234, "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the title is empty.
    public void tsfsTest4() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the description is empty.
    public void tsfsTest5() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the author's email address is invalid.
    public void tsfsTest6() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "invalid_email", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the assignee is missing.
    public void tsfsTest7() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the assignee provided in not valid.
    public void tsfsTest8() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "invalid_username", "pbhide6@gatech.edu", new String[0], "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the watchers list contains invalid user ids.
    public void tsfsTest9() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[]{"invalid_watcher"}, "open", true);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket fails if the request is not authenticated.
    public void tsfsTest10() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", false);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = open and watchers list is empty.
    public void tsfsTest11() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[0], "open", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"open\",\"watchers\":new String[0],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = open and watchers list has valid users.
    public void tsfsTest12() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[] {"sfazulbhoy3"}, "open", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"open\",\"watchers\":[\"sfazulbhoy3\"],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = closed and watchers list is empty.
    public void tsfsTest13() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[0], "closed", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"closed\",\"watchers\":new String[0],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = closed and watchers list has valid users.
    public void tsfsTest14() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[] {"sfazulbhoy3"}, "closed", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"closed\",\"watchers\":[\"sfazulbhoy3\"],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = needs-attention and watchers list is empty.
    public void tsfsTest15() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[0], "needs-attention", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"needs-attention\",\"watchers\":new String[0],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: Check that PUT request on a ticket succeeds when status = needs-attention and watchers list has valid users.
    public void tsfsTest16() throws Exception {
        deleteTickets("pbhide6");
        try {
            CloseableHttpResponse response = createTicket("pbhide6", "Sample title", "This is the description", "krockwell", "pbhide6@gatech.edu", new String[0]);
            Integer ticketid = getTicketIdFromResponse(response);
            response.close();

            response = updateTicket("pbhide6", ticketid, "New Title", "New description", "krockwell", "pbhide6@gatech.edu", new String[] {"sfazulbhoy3"}, "needs-attention", true);
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

            String expectedJson = "{\"id\":" + ticketid + ",\"title\":\"New Title\",\"description\":\"New description\",\"emailAddress\":\"pbhide6@gatech.edu\",\"assignee\":\"krockwell\",\"status\":\"needs-attention\",\"watchers\":[\"sfazulbhoy3\"],\"categories\":new String[0]}";
            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse createTicket(String userid, String title, String description, String assignee, String email, String[] watchers) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/api/users/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"emailAddress\":\"" + email + "\","+
                "\"watchers\":\"[" + String.join(",", watchers) + "]\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse updateTicket(String userid, Integer ticketid, String title, String description, String assignee, String email, String[] watchers, String status, boolean isAuthenticated) throws IOException {
        HttpPut httpRequest;
        if (ticketid == -1) {
            httpRequest = new HttpPut(baseUrl + "/api/users/" + userid + "/tickets/invalid_ticket");
        } else {
            httpRequest = new HttpPut(baseUrl + "/api/users/" + userid + "/tickets/" + ticketid.toString());
        }
        httpRequest.addHeader("accept", "application/json");
        if (isAuthenticated) {
            httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        }
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"emailAddress\":\"" + email + "\","+
                "\"status\":\"" + status + "\","+
                "\"watchers\":\"[" + String.join(",", watchers) + "]\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse deleteTicket(String userid, Integer ticketid) throws IOException {
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/api/users/" + userid + "/tickets/" + ticketid.toString());
        httpDelete.addHeader("accept", "application/json");
        httpDelete.addHeader("Authorization", "Bearer placeholder_token_1234");
        System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println("*** Raw response " + response + "***");
        // EntityUtils.consume(response.getEntity());
        // response.close();
        return response;
    }

    private CloseableHttpResponse deleteTickets(String userid) throws IOException {
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/api/users/" + userid);
        httpDelete.addHeader("accept", "application/json");
        httpDelete.addHeader("Authorization", "Bearer placeholder_token_1234");
        System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println("*** Raw response " + response + "***");
        // EntityUtils.consume(response.getEntity());
        // response.close();
        return response;
    }

    private Integer getTicketIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        Integer id = getTicketIdFromStringResponse(strResponse);
        return id;
    }

    private Integer getTicketIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        Integer id = -1;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()){
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.getInt(key);
            }
        }
        return id;
    }

    private String removeDateFieldsFromJsonStr(String str) throws JSONException {
        JSONObject object = new JSONObject(str);
        object.remove("createdDate");
        object.remove("modifiedDate");

        return object.toString();
    }

}
