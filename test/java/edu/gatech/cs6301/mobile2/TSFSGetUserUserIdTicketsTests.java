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

/**
 * This test class tests the end point: GET /user/<userid>/tickets
 */
public class TSFSGetUserUserIdTicketsTests {

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
     * Purpose: Tests whether the server returns the correct error code when a user id is missing in the url
     * @throws IOException thrown when error happens after executing a http
     */
    @Test
    public void tsfsTest1() throws IOException {
        // Empty user id
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "" + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }

    }

    /**
     * Purpose: Tests whether the server returns the correct error code when the user id contains special characters
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest2() throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "@#*ltang" + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: Tests whether the server returns the correct error code when the user id is just spaces in the url
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest3() throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "     " + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }


    /**
     * Purpose: Tests whether the server returns the correct error code when a non-existent user id is in the url
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest4() throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "non_existent_id_123" + "/tickets");
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
     * Purpose: Tests whether the server returns the correct response when a correct user id is given
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest5() throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,200);
            String strResponse = EntityUtils.toString(response.getEntity());
            Assert.assertTrue(strResponse.contains("[") && strResponse.contains("]"));
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status and/or response body! Your status code" +
                    " should be 200 and your response should be a list! ");
        }
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }



}

