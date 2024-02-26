package edu.gatech.cs6301.mobile2;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class TSFSGetUserUserIdTicketsTicketIdCommentsTests {

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

    /**
     * Purpose: This test case verifies the behavior of the server when an empty user ID is passed as a part of the URL.
     * @throws IOException
     */
    @Test
    public void tsfsTest1() throws IOException {
        // Empty user ID
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "" + "/tickets/" + "123" + "/comments");
        httpRequest.addHeader("accept", "application/json");
        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try {
            Assert.assertEquals(status, 400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }

    }

    /**
     * Purpose: This test case verifies the behavior of the server when a user ID with special characters is passed as a part of the URL.
     * @throws IOException
     */
    @Test
    public void tsfsTest2() throws IOException {
        // User ID contains special characters
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "&^%$" + "/tickets/" + "123" + "/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try {
            Assert.assertEquals(status, 400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }

    }

    /**
     * Purpose: This test case verifies the behavior of the server when a user ID containing spaces is passed as a part of the URL.
     * @throws IOException
     */
    @Test
    public void tsfsTest3() throws IOException {
        // User contains spaces
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "user id" + "/tickets/" + "123" + "/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try {
            Assert.assertEquals(status, 400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: To test the behavior of the server when attempting to retrieve comments for a non-existent user's ticket.
     * @throws IOException
     */
    @Test
    public void tsfsTest4() throws IOException {
        // User ID does not exist in the server
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "nonexistent" + "/tickets/" + "123" + "/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try {
            Assert.assertEquals(status, 404);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 404");
        }
    }

    /**
     * Purpose: To test the behavior of the server when attempting to retrieve comments for a ticket with a non-integer ID.
     * @throws IOException
     */
    @Test
    public void tsfsTest5() throws IOException {
        // Ticket ID is not an integer
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "user" + "/tickets/" + "noninteger" + "/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try {
            Assert.assertEquals(status, 400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: To test the behavior of the server when attempting to retrieve comments for a ticket owned by a different user.
     * @throws IOException
     */
    @Test
    public void tsfsTest6() throws IOException {
        // Incorrect ticket ownership
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/user1/tickets/123/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,404);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 404");
        }
    }

    /**
     * Purpose: To test the successful retrieval of comments associated with a valid ticket owned by the logged-in user.
     * @throws IOException
     */
    @Test
    public void tsfsTest7() throws IOException {
        // Valid input
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/user1/tickets/456/comments");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,200);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 200");
        }
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }



}

