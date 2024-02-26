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

public class Get_User_UserId_Tickets {

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

    // Purpose: Tests if a 404 error code is returned when the userId url param is empty
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse response = getTickets(
                    httpclient,
                    baseUrl,
                    ""
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

    // Purpose: Tests if a 200 error code and subsequent ticket list is returned when the userId exists
    @Test
    public void tsfsTest2() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );
            response.close();

            response = getTickets(
                    httpclient,
                    baseUrl,
                    EXISTING_USER
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
                    "\"title\":\"" + VALID_TITLE + "\"," +
                    "\"description\":\"" + VALID_DESCRIPTION + "\"," +
                    "\"status\":\"" + VALID_STATUS + "\"," +
                    "\"category\":\"" + VALID_CATEGORY + "\"," +
                    "\"emailAddress\":\"" + VALID_EMAIL + "\"," +
                    "\"assignee\":\"" + VALID_ASSIGNEE + "\"," +
                    "\"watchers\":[]" +
                    "}]";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 404 error code is returned when the userId doesn't exist
    @Test
    public void tsfsTest3() throws Exception {
        try {
            CloseableHttpResponse response = getTickets(
                    httpclient,
                    baseUrl,
                    NONEXISTING_USER
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
}
