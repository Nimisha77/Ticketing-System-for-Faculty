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

public class Post_User_UserId_Tickets {

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

    private final String EXISTING_USER = "gburdell1";

    // Purpose: Tests if a 404 error code is returned when the userId url param is empty
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    "",
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
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

    // Purpose: Tests if a 404 error code is returned when the userId doesn't exist
    @Test
    public void tsfsTest2() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    NONEXISTING_USER,
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
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

    // Purpose: Tests if a 400 error code is returned when the title is empty
    @Test
    public void tsfsTest3() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    "",
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the description is empty
    @Test
    public void tsfsTest4() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    VALID_TITLE,
                    "",
                    VALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the status is invalid
    @Test
    public void tsfsTest5() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    INVALID_STATUS,
                    VALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the category is invalid
    @Test
    public void tsfsTest6() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    INVALID_CATEGORY,
                    VALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the email address is invalid
    @Test
    public void tsfsTest7() throws Exception {
        try {
            CloseableHttpResponse response = createTicket(
                    httpclient,
                    baseUrl,
                    EXISTING_USER,
                    VALID_TITLE,
                    VALID_DESCRIPTION,
                    VALID_STATUS,
                    VALID_CATEGORY,
                    INVALID_EMAIL,
                    VALID_ASSIGNEE,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the assignee is empty
    @Test
    public void tsfsTest8() throws Exception {
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
                    "",
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when the assignee doesn't exist
    @Test
    public void tsfsTest9() throws Exception {
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
                    NONEXISTING_USER,
                    ZERO_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 400 error code is returned when at least one watcher doesn't exist
    @Test
    public void tsfsTest10() throws Exception {
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
                    new String[] {NONEXISTING_USER}
            );

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 201 code with created entity is returned when all is valid and there are no watchers
    @Test
    public void tsfsTest11() throws Exception {
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

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = "{" +
                    "\"title\":\"" + VALID_TITLE + "\"," +
                    "\"description\":\"" + VALID_DESCRIPTION + "\"," +
                    "\"status\":\"" + VALID_STATUS + "\"," +
                    "\"category\":\"" + VALID_CATEGORY + "\"," +
                    "\"emailAddress\":\"" + VALID_EMAIL + "\"," +
                    "\"assignee\":\"" + VALID_ASSIGNEE + "\"," +
                    "\"watchers\":[]" +
                    "}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 201 code with created entity is returned when all is valid and there is one watcher
    @Test
    public void tsfsTest12() throws Exception {
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
                    ONE_WATCHER
            );

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = "{" +
                    "\"title\":\"" + VALID_TITLE + "\"," +
                    "\"description\":\"" + VALID_DESCRIPTION + "\"," +
                    "\"status\":\"" + VALID_STATUS + "\"," +
                    "\"category\":\"" + VALID_CATEGORY + "\"," +
                    "\"emailAddress\":\"" + VALID_EMAIL + "\"," +
                    "\"assignee\":\"" + VALID_ASSIGNEE + "\"," +
                    "\"watchers\":[\"" + ONE_WATCHER[0] + "\"]" +
                    "}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Tests if a 201 code with created entity is returned when all is valid and there are multiple watchers
    @Test
    public void tsfsTest13() throws Exception {
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
                    MULTIPLE_WATCHERS
            );

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = "{" +
                    "\"title\":\"" + VALID_TITLE + "\"," +
                    "\"description\":\"" + VALID_DESCRIPTION + "\"," +
                    "\"status\":\"" + VALID_STATUS + "\"," +
                    "\"category\":\"" + VALID_CATEGORY + "\"," +
                    "\"emailAddress\":\"" + VALID_EMAIL + "\"," +
                    "\"assignee\":\"" + VALID_ASSIGNEE + "\"," +
                    "\"watchers\":[\"" + MULTIPLE_WATCHERS[0] + "\",\"" + MULTIPLE_WATCHERS[1] + "\"]" +
                    "}";

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }
}
