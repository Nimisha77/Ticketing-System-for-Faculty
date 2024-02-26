package edu.gatech.cs6301.mobile2;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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

import java.io.IOException;

/**
 * This test class tests the endpoint: POST /user/<userid>/tickets
 */
public class TSFSPostUserUserIdTicketsTests {

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
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "" + "/tickets/12345");
        httpRequest.addHeader("accept", "application/json");

        StringEntity input = new StringEntity("{\"id\":0,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

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

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "@#*ltang" + "/tickets/12345");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":0,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

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
    public void tsfsTest3() throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "     " + "/tickets/12345");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":0,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

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
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "non_existent_id_123" + "/tickets/12345");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":0,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,404);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 404");
        }
    }

    /**
     * Purpose: Tests whether the server returns the correct error code when the request body is empty
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest5() throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets/12345");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: Tests whether the server returns the correct error code when some fields in the body are missing
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest6() throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        //Missing category and description
        StringEntity input = new StringEntity("{\"id\":12345,\"title\":\"string\",\"status\"" +
                ":\"open\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: Tests whether the server returns the correct error code when the ticket id is non-integer
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest7() throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":\"abcd@^ef\",\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }


    /**
     * Purpose: Tests whether the server returns the correct error code when the ticket id already exists
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest8() throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":10000,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        httpclient.execute(httpRequest);

        HttpPost httpRequestRepeated = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity inputRepeated = new StringEntity("{\"id\":10000,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequestRepeated.setEntity(inputRepeated);

        CloseableHttpResponse response = httpclient.execute(httpRequestRepeated);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,400);
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response status: " + status + "; expected status code: 400");
        }
    }

    /**
     * Purpose: Tests whether the server returns the correct response when the request parameters and body are correct
     * @throws IOException thrown when error happens after executing a http request
     */
    @Test
    public void tsfsTest9() throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + "ltang62" + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"id\":12345,\"title\":\"string\",\"description\":\"string\",\"status\"" +
                ":\"open\",\"category\":\"travelauthorization\",\"emailAddress\":\"user@example.com\",\"assignee\":\"" +
                "string\",\"watchers\":[\"string\"],\"createdDate\":\"2023-02-28T03:03:58.460Z\",\"modifiedDate\"" +
                ":\"2023-02-28T03:03:58.460Z\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        CloseableHttpResponse response = httpclient.execute(httpRequest);

        int status = response.getStatusLine().getStatusCode();

        try{
            Assert.assertEquals(status ,200);
            // If successful, response should equal to be request
            Assert.assertEquals(input.toString(), EntityUtils.toString(response.getEntity()));
        } catch (AssertionError e) {
            throw new ClientProtocolException("Unexpected response code and/or response body. The correct" +
                    " response code should be 200 and your response body has a ticket object different from the " +
                    "request body.");
        }
    }


    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }

}
