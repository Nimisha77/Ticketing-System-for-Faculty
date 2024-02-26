package edu.gatech.cs6301.Web1;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PUT_user_userid_tickets_ticketid {

    private String baseUrl = "http://localhost:8080";
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupisdone;

    @Before
    public void runBefore() throws IOException {
        if (!setupisdone) {
            System.out.println("*** SETTING UP TESTS ***");
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("locahost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 10);
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            // Creating a valid ticket
            /*
            The values of user, assignee, watcher and emailIDs must be replaced with actual values later.
            For now, userId123, userId456 are valid users
            assignee1@gatech.edu is valid. assigneeWrong@gatech.edu is invalid.
            watcher1, watcher2 are valid. watcherWrong is invalid.
             */
            createTicket(1, "userId123", "Test title", "Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z");
            setupisdone = true;
        }
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }
    // *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***

    // since delete function is not in API definition,  it is assumed for the tests that the valid tickets as defined exist in the backend.
    // If needed to add, the create and delete functions are commented at the end of this class




    @Test //TF-1
    public void tsfsTest1() throws Exception {
        //Purpose: <check TicketId Exists;  Ticket ID exists+ UserId is valid, path should be accessible>;  currently also checked in TF 3,4
        // 200 - successfully changed, no change in content.
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[]{"watcher1", "watcher2"};
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Test title" + "\"," +
                    "{\"description\":\"" + "Some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "reimbursement" + "\"," +
                    "{\"emailAddress\":\"" + "userId123@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "assignee1@gatech.edu" + "\"," +
                    "{\"watchers\":\"" + arr + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + "2022-10-31T09:00:00.594Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2022-11-31T10:23:00.594Z" + "\"}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-2
    public void tsfsTest2() throws Exception {
        //Purpose: <check Ticket Id Not Exists; TicketID on the path does not exist>
        //400 - Bad request
        try{
            CloseableHttpResponse response = updateTicket(24, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-3
    public void tsfsTest3() throws Exception {
        //Purpose: <user Owns Ticket; User should be able to modify Ticket>
        //200 - changed successfully, no change in content
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[]{"watcher1", "watcher2"};
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Test title" + "\"," +
                    "{\"description\":\"" + "Some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "reimbursement" + "\"," +
                    "{\"emailAddress\":\"" + "userId123@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "assignee1@gatech.edu" + "\"," +
                    "{\"watchers\":\"" + arr + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + "2022-10-31T09:00:00.594Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2022-11-31T10:23:00.594Z" + "\"}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-4
    public void tsfsTest4() throws Exception {
        //Purpose: <userId that Does Not Own this TicketId are used together to access Ticket; User should not be able to modify>
        //401 - Unauthorized user
        try{
            CloseableHttpResponse response = updateTicket(1, "userId456", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(401, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-5
    public void tsfsTest5() throws Exception {
        //Purpose: <change Ticket Description>
        // 200; content: everything same but description
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Modified description", "open", "proposals", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[]{"watcher1", "watcher2"};
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Test title" + "\"," +
                    "{\"description\":\"" + "Modified description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "proposals" + "\"," +
                    "{\"emailAddress\":\"" + "userId123@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "assignee1@gatech.edu" + "\"," +
                    "{\"watchers\":\"" + arr + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + "2022-10-31T09:00:00.594Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2022-11-31T10:23:00.594Z" + "\"}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-6
    public void tsfsTest6() throws Exception {
        //Purpose: <change Category Description with a Valid value; Valid values : travel authorization, reimbursement, meeting organization, student hiring, proposals, miscellaneous>
        // 200; content: everything same but category
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "proposals", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[]{"watcher1", "watcher2"};
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Test title" + "\"," +
                    "{\"description\":\"" + "Some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "proposals" + "\"," +
                    "{\"emailAddress\":\"" + "userId123@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "assignee1@gatech.edu" + "\"," +
                    "{\"watchers\":\"" + arr + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + "2022-10-31T09:00:00.594Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2022-11-31T10:23:00.594Z" + "\"}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-7
    public void tsfsTest7() throws Exception {
        //Purpose: <change Status with Invalid value; Valid values are : open, closed, needs attention>
        //400 - Bad request

        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "Just opened", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-8
    public void tsfsTest8() throws Exception {
        //Purpose: <change Assignee with an Invalid value>
        //400 - Bad request
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assigneeWrong@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-9
    public void tsfsTest9() throws Exception {
        //Purpose: <change Watcher List by entering Invalid value(s)>
        //400 - Bad request
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2", "watcherWrong"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-10
    public void tsfsTest10() throws Exception {
        //Purpose: <date Change when entering an Invalid Format>
        //400- Bad request
        try{
            // wrong format for createdDate
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "10A-2023-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-12
    public void tsfsTest11() throws Exception {
        //Purpose: <check Ticket Validity when TicketId is Invalid>
        // 400 - Bad request
        try{
            CloseableHttpResponse response = updateTicket(-1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();

            Assert.assertEquals(400, status);

            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    @Test //TF-12
    public void tsfsTest12() throws Exception {
        //Purpose: <check Ticket Validity when TicketId is Valid>
        // 200 ; no change in content
        try{
            CloseableHttpResponse response = updateTicket(1, "userId123", "Test title", " Some description", "open", "reimbursement", "userId123@gatech.edu", "assignee1@gatech.edu", new String[]{"watcher1", "watcher2"}, "2022-10-31T09:00:00.594Z", "2022-11-31T10:23:00.594Z" );
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[]{"watcher1", "watcher2"};
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Test title" + "\"," +
                    "{\"description\":\"" + "Some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "reimbursement" + "\"," +
                    "{\"emailAddress\":\"" + "userId123@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "assignee1@gatech.edu" + "\"," +
                    "{\"watchers\":\"" + arr + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + "2022-10-31T09:00:00.594Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2022-11-31T10:23:00.594Z" + "\"}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        }finally{
            httpclient.close();
        }
    }

    private CloseableHttpResponse updateTicket(long ticketId, String userId, String title, String description, String status, String category, String emailAddress, String assignee, String[] watchers, String createdDate, String modifiedDate) {
        HttpPut httpRequest = new HttpPut(baseUrl + "/user/" + userId + "/tickets/" + String.valueOf(ticketId));
        httpRequest.addHeader("accept", "application/json");

        StringEntity input;
        try {
            input = new StringEntity("{\"id\":\"" + String.valueOf(ticketId) + "\"," +
                    "{\"title\":\"" + title + "\"," +
                    "{\"description\":\"" + description + "\"," +
                    "{\"status\":\"" + status + "\"," +
                    "{\"category\":\"" + category + "\"," +
                    "{\"emailAddress\":\"" + emailAddress + "\"," +
                    "{\"assignee\":\"" + assignee + "\"," +
                    "{\"watchers\":\"" + watchers + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + createdDate + "\"," +
                    "{\"modifiedDate\":\"" + modifiedDate + "\"}");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createTicket(long ticketId, String userId, String title, String description, String status, String category, String emailAddress, String assignee, String[] watchers, String createdDate, String modifiedDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+userId+"tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input;
        try {
            input = new StringEntity("{\"id\":\"" + String.valueOf(ticketId) + "\"," +
                    "{\"title\":\"" + title + "\"," +
                    "{\"description\":\"" + description + "\"," +
                    "{\"status\":\"" + status + "\"," +
                    "{\"category\":\"" + category + "\"," +
                    "{\"emailAddress\":\"" + emailAddress + "\"," +
                    "{\"assignee\":\"" + assignee + "\"," +
                    "{\"watchers\":\"" + watchers + "\"," +  // incorrect format?
                    "{\"createdDate\":\"" + createdDate + "\"," +
                    "{\"modifiedDate\":\"" + modifiedDate + "\"}");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

}


