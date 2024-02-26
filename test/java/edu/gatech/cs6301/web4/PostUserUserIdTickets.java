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

public class PostUserUserIdTickets {

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

    // Purpose: Test POST_user_userid_tickets with an invalid request object (title is null or non-exist)
    @Test
    public void tsfsTest1() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (status is null or non-exist)
    @Test
    public void tsfsTest2() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "", "travel authorization", "yji319@gatech.edu", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (status is other string)
    @Test
    public void tsfsTest3() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "hello", "travel authorization", "yji319@gatech.edu", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (emailAddress is null or non-exist)
    @Test
    public void tsfsTest4() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (emailAddress is other string (i.e. the string doesn't match email format))
    @Test
    public void tsfsTest5() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "abcdefg", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (assignee is null)
    @Test
    public void tsfsTest6() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (assignee is non-exist)
    @Test
    public void tsfsTest7() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "xyz99xyz", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (watcher is null)
    @Test
    public void tsfsTest8() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicketWithDefinedWatchers("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", "", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (watcher is non-exist)
    @Test
    public void tsfsTest9() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicketWithDefinedWatchers("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", "xyz999xyz", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (watcher is non-exist)
    @Test
    public void tsfsTest10() throws Exception {
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicketWithDefinedWatchers("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", "xyz999xyz", curDate);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (createdDate is null or non-exist)
    @Test
    public void tsfsTest11() throws Exception {
        try {
            Date date = new Date();
            CloseableHttpResponse response = createTicketWithSelfDefinedDate("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", "", date.toString());
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (createdDate is other string)
    @Test
    public void tsfsTest12() throws Exception {
        try {
            Date date = new Date();
            CloseableHttpResponse response = createTicketWithSelfDefinedDate("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", "date", date.toString());
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (modifiedDate is null or non-exist)
    @Test
    public void tsfsTest13() throws Exception {
        try {
            Date date = new Date();
            CloseableHttpResponse response = createTicketWithSelfDefinedDate("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", date.toString(), "");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with an invalid request object (modifiedDate is other string)
    @Test
    public void tsfsTest14() throws Exception {
        try {
            Date date = new Date();
            CloseableHttpResponse response = createTicketWithSelfDefinedDate("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", date.toString(), "date");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test POST_user_userid_tickets with a valid request object
    @Test
    public void tsfsTest15() throws Exception {
        String expectedJson = "";
        try {
            Date curDate = new Date();
            CloseableHttpResponse response = createTicket("Test Ticket 1", "This is a test ticket 1", "open", "travel authorization", "yji319@gatech.edu", "yji319", curDate);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            String strResponse = EntityUtils.toString(entity);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String id = getIdFromStringResponse(strResponse);

            expectedJson += "{\"id\":\"" + id + "\",\"title\":\"Test Ticket 1\",\"description\":\"This is a test ticket 1\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"yji319@gatech.edu\",\"assignee\":\"yji319\",\"watchers\":[\"yji319\"],";
            expectedJson += "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}";
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse createTicket(String title, String description, String status, String category, String emailAddress, String assignee, Date curDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/yji319/tickets");
        httpRequest.addHeader("accept", "application/json");
        String dateString = curDate == null ? "" : curDate.toString();
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," + "\"emailAddress\":\"" + emailAddress +
                "\"assignee\":\"" + assignee + "\"watchers\":\"" + "[\"yji319\"]" +
                "\"createdDate\":\"" + dateString + "\"modifiedDate\":\"" + dateString + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createTicketWithSelfDefinedDate(String title, String description, String status, String category, String emailAddress, String assignee, String createDate, String modifyDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/yji319/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," + "\"emailAddress\":\"" + emailAddress +
                "\"assignee\":\"" + assignee + "\"watchers\":\"" + "[\"yji319\"]" +
                "\"createdDate\":\"" + createDate + "\"modifiedDate\":\"" + modifyDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createTicketWithDefinedWatchers(String title, String description, String status, String category, String emailAddress, String assignee, String watcher, Date curDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/yji319/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," + "\"emailAddress\":\"" + emailAddress +
                "\"assignee\":\"" + assignee + "\"watchers\":\"" + "[\"" + watcher + "\"]" +
                "\"createdDate\":\"" + curDate + "\"modifiedDate\":\"" + curDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);
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
