package edu.gatech.cs6301.backend2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static edu.gatech.cs6301.backend2.TSFSBackendTestUtils.*;

public class Get_User_UserId_Tickets_TicketId_Comments {
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
            cm.setDefaultMaxPerRoute(20);
            // Increase max connections for localhost:8080 to 50
            HttpHost localhost = new HttpHost("locahost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 50);
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

    // Purpose: Tests if a 404 error code is returned when the userId url param is empty
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse response = getComments(
                    httpclient,
                    baseUrl,
                    "",
                    EXISTING_TICKET_ID
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 404 error code is returned when the userId doesn't exist.
    @Test
    public void tsfsTest2() throws Exception {
        try {
            CloseableHttpResponse response = getComments(
                    httpclient,
                    baseUrl,
                    NONEXISTING_USER,
                    EXISTING_TICKET_ID
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 404 error code is returned when userId exist but ticketId is a non-integer string
    @Test
    public void tsfsTest3() throws Exception {
        try {
            CloseableHttpResponse response = getComments(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    INVALID_TICKET_ID
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 404 error code is returned when userId exist but ticketId doesn't exist
    @Test
    public void tsfsTest4() throws Exception {
        try {
            CloseableHttpResponse response = getComments(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    NONEXISTING_TICKET_ID
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 200 success code and the proper comment is returned when the userId and ticketId exists, and ticketId
    // belongs to the userId
    @Test
    public void tsfsTest5() throws Exception {
        try {
            CloseableHttpResponse response = createComment(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    EXISTING_TICKET_ID,
                    VALID_COMMENT_AUTHOR,
                    VALID_COMMENT_CONTENT
            );
            response.close();

            response = getComments(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    EXISTING_TICKET_ID
            );

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

            String expectedJson = "[{" +
                    "\"author\":\"" + VALID_COMMENT_AUTHOR + "\"," +
                    "\"content\":\"" + VALID_COMMENT_CONTENT + "}]";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

}