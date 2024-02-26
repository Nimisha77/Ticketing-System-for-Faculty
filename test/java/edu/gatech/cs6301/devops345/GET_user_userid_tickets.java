package edu.gatech.cs6301.devops345;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

enum Status {
    OPEN("open"),
    CLOSED("closed"),
    NEEDS_ATTENTION("needs-attention");

    private String value;

    Status(String status) {
        this.value = status;
    }

    String getValue() {
        return value;
    }
}


enum Category {
    TRAVEL_AUTHORIZATION("travel authorization"),
    REIMBURSEMENT("reimbursement"),
    STUDENT_HIRING("student hiring"),
    PROPOSALS("proposals"),
    MEETING_ORGANIZATION("meeting organization"),
    MISCELLANEOUS("miscellaneous");

    private String value;

    Category(String category) {
        this.value = category;
    }

    String getCategory() {
        return value;
    }
}

public class GET_user_userid_tickets {

    private String baseUrl = "http://localhost:8080";
    private String someValidAuthToken = "_deQvOO^77CSqlD";
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;

    public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
        public final static String METHOD_NAME = "GET";

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpGetWithEntity(String uri) throws URISyntaxException {
            this.setURI(new URI(uri));
        }
    }

    @Before
    public void runBefore() {
        if (!setupdone) {
            System.out.println("*** SETTING UP TESTS ***");
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("localhost", 8080);
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

    /**
     * Purpose: Tests that the API endpoint returns [error = 401:Unauthorized] if an unauthorized user tries to access the endpoint.
     * @throws Exception
     */
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);

        try {
            CloseableHttpResponse postResponse = createTicket(someValidAuthToken, "randomuser", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna, sfazulbhoy3")));
            postResponse.close();

            CloseableHttpResponse response = getTickets("mnigotia3", "invalid_token");
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, status);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns [error = 404:Not found] if the userid passed is an empty string.
     * @throws Exception
     */
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);

        try {
            CloseableHttpResponse postResponse = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "aorso3@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna, sfazulbhoy3")));
            postResponse.close();
            CloseableHttpResponse response = getTickets("", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns [error = 404:Not found] if the userid passed is invalid, i.e. the user does not exist.
     * @throws Exception
     */
    @Test
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);

        try {
            CloseableHttpResponse postResponse = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "aorso3@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna, sfazulbhoy3")));
            postResponse.close();
            CloseableHttpResponse response = getTickets("randomuserid", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns all valid tickets even if a GET request is made with a request body.
     * @throws Exception
     */
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);

        try {
            CloseableHttpResponse postResponse = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.TRAVEL_AUTHORIZATION, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("sfazulbhoy3")));
            String id = getIdFromResponse(postResponse);
            CloseableHttpResponse response = getTicketsWithRequestBody("mnigotia3", "{}", someValidAuthToken);

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
            strResponse = removeDateFieldsFromJsonStr(strResponse);

            String expectedJson = "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"pbhide6\",\"watchers\":[\"sfazulbhoy3\"]}]";

            JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns all tickets corresponding to the provided userid.
     * Creates 3 tickets, for which a user is the owner, assignee and a watcher respectively. On get request for the user, API should return all the 3 tickets since the user is part of all the 3 tickets.
     * @throws Exception
     */
    @Test
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);
        String id = null;
        String expectedJson = "";

        try {
            CloseableHttpResponse ticketResponse1 = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.TRAVEL_AUTHORIZATION, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse1);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"pbhide6\",\"watchers\":[\"hamna\"]}";

            CloseableHttpResponse ticketResponse2 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.TRAVEL_AUTHORIZATION, "devops345@gatech.edu", "mnigotia3", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse2);
            expectedJson += ",{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2022\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"mnigotia3\",\"watchers\":[\"hamna\"]}";

            CloseableHttpResponse ticketResponse3 = createTicket(someValidAuthToken,"hamna", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.TRAVEL_AUTHORIZATION, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("mnigotia3")));
            id = getIdFromResponse(ticketResponse3);
            expectedJson += ",{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2023\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"sfazulbhoy3\",\"watchers\":[\"mnigotia3\"]}]";

            CloseableHttpResponse response = getTickets("mnigotia3", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            strResponse = removeDateFieldsFromJsonStr(strResponse);

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            //Assert.assertTrue(strResponse.contains(expectedJson));
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns all tickets corresponding to the provided userid ONLY.
     * Creates 6 tickets, 3 corresponding to user 1 and the other three corresponding to user 2. On get request for user 1, API should return the 3 tickets user 1 is part of.
     * @throws Exception
     */
    @Ignore
    @Test
    public void tsfsTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);
        String id = null;
        String expectedJson = "";

        try {
            CloseableHttpResponse ticketResponse1 = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse1);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"pbhide6\",\"watchers\":[\"hamna\"]}";

            CloseableHttpResponse ticketResponse2 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "mnigotia3", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse2);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"mnigotia3\",\"watchers\":[\"hamna\"]}";

            CloseableHttpResponse ticketResponse3 = createTicket(someValidAuthToken,"hamna", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("mnigotia3")));
            id = getIdFromResponse(ticketResponse3);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"sfazulbhoy3\",\"watchers\":[\"mnigotia3\"]}]";

            ticketResponse3.close();
            ticketResponse2.close();
            ticketResponse1.close();

            CloseableHttpResponse ticketResponse4 = createTicket(someValidAuthToken,"sfazulbhoy3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "amosingh", new ArrayList<>(Arrays.asList("phide6")));
            CloseableHttpResponse ticketResponse5 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("hamna")));
            CloseableHttpResponse ticketResponse6 = createTicket(someValidAuthToken,"sgoenka33", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("amosingh")));

            ticketResponse6.close();
            ticketResponse5.close();
            ticketResponse4.close();


            CloseableHttpResponse response = getTickets("mnigotia3", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            strResponse = removeDateFieldsFromJsonStr(strResponse);
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns no tickets because there are no tickets in the system corresponding to the userid passed.
     * Creates 3 tickets, for which user 1 is the owner, assignee and a watcher respectively. On get request for user 2 (who has association with no tickets), API should return an empty array.
     * @throws Exception
     */
    @Test
    public void tsfsTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);
        String id = null;
        String expectedJson = "";

        try {
            CloseableHttpResponse ticketResponse1 = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse1);

            CloseableHttpResponse ticketResponse2 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "mnigotia3", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse2);

            CloseableHttpResponse ticketResponse3 = createTicket(someValidAuthToken,"hamna", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("mnigotia3")));
            id = getIdFromResponse(ticketResponse3);

            expectedJson = "[]";

            CloseableHttpResponse response = getTickets("aorso3", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            strResponse = removeDateFieldsFromJsonStr(strResponse);
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            Assert.assertTrue(strResponse.contains(expectedJson));
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    /**
     * Purpose: Tests that the API endpoint returns all tickets corresponding to the provided userid ONLY.
     * Creates 6 tickets, 3 corresponding to user 1 and the other three corresponding to user 2. On get request for user 2, API should return the 3 tickets user 2 is part of.
     * @throws Exception
     */
    @Ignore
    @Test
    public void tsfsTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        deleteTickets(someValidAuthToken);
        String id = null;
        String expectedJson = "";

        try {
            CloseableHttpResponse ticketResponse1 = createTicket(someValidAuthToken,"sfazulbhoy3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "amosingh", new ArrayList<>(Arrays.asList("phide6")));
            id = getIdFromResponse(ticketResponse1);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"pbhide6\",\"watchers\":[\"phide6\"]}";

            CloseableHttpResponse ticketResponse2 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("hamna")));
            id = getIdFromResponse(ticketResponse2);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"mnigotia3\",\"watchers\":[\"phide6\"]}";

            CloseableHttpResponse ticketResponse3 = createTicket(someValidAuthToken,"sgoenka33", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "sfazulbhoy3", new ArrayList<>(Arrays.asList("mnigotia3")));
            id = getIdFromResponse(ticketResponse3);
            expectedJson += "[{\"id\":\"" + id + "\",\"title\":\"Reimbursement request for travel for AAAI 2021\",\"description\":\"Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD\",\"status\":\"open\",\"category\":\"travel authorization\",\"emailAddress\":\"devops345@gatech.edu\",\"assignee\":\"sfazulbhoy3\",\"watchers\":[\"mnigotia3\"]}]";

            CloseableHttpResponse ticketResponse4 = createTicket(someValidAuthToken,"mnigotia3", "Reimbursement request for travel for AAAI 2021", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("amosingh")));
            CloseableHttpResponse ticketResponse5 = createTicket(someValidAuthToken,"pbhide6", "Reimbursement request for travel for AAAI 2022", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "mnigotia3", new ArrayList<>(Arrays.asList("sgoenka33")));
            CloseableHttpResponse ticketResponse6 = createTicket(someValidAuthToken,"hamna", "Reimbursement request for travel for AAAI 2023", "Travel details - Return journey from Atlanta to Washington, D.C (From 7th Feb to 14th Feb). Amount = $2700 USD", Status.OPEN, Category.REIMBURSEMENT, "devops345@gatech.edu", "pbhide6", new ArrayList<>(Arrays.asList("mnigotia3")));

            CloseableHttpResponse response = getTickets("sfazulbhoy3", someValidAuthToken);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            strResponse = removeDateFieldsFromJsonStr(strResponse);
            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            Assert.assertTrue(strResponse.contains(expectedJson));
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse getTickets(String userid, String authToken) throws IOException {
        String url = baseUrl + "/user/" + userid + "/tickets";
        HttpGet httpRequest = new HttpGet(url);
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer " + authToken);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse getTicketsWithRequestBody(String userid, String requestBody, String authToken) throws IOException, URISyntaxException {
        String url = baseUrl + "/user/" + userid + "/tickets";
        //HttpGet httpRequest = new HttpGet(url);
        HttpGetWithEntity httpRequest = new HttpGetWithEntity(url);
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer " + authToken);
        httpRequest.setEntity(new StringEntity(requestBody));

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createTicket(String authToken, String ownerUser, String title, String description, Status status, Category category, String email, String assignee, List<String> watchers) throws IOException {
        String url = baseUrl + "/user/" + ownerUser + "/tickets";
        HttpPost httpRequest = new HttpPost(baseUrl + url);
        httpRequest.addHeader("accept", "application/json");
        httpRequest.addHeader("Authorization", "Bearer " + authToken);

        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status.getValue() + "\"," +
                "\"emailAddress\":\"" + email + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + "[" + String.join(",", watchers) + "]" + "," +
                "\"createdDate\":\"" + getCurrentTime() + "\"," +
                "\"modifiedDate\":\"" + getCurrentTime() + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private CloseableHttpResponse deleteTickets(String authToken) throws IOException {
        //Assuming there is a dummy delete endpoint to delete all the tickets in the system
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/api/tickets");
        httpDelete.addHeader("accept", "application/json");
        httpDelete.addHeader("Authorization", "Bearer " + authToken);


        System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println("*** Raw response " + response + "***");
        // EntityUtils.consume(response.getEntity());
        // response.close();
        return response;
    }

    private String getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id = getIdFromStringResponse(strResponse);
        return id;
    }

    private String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()){
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.get(key).toString();
            }
        }
        return id;
    }

    private String removeDateFieldsFromJsonStr(String str) throws JSONException {
        JSONArray tickets = new JSONArray(str);
        for (int i = 0; i < tickets.length(); i++) {
            JSONObject ticketobj = tickets.getJSONObject(i);
            ticketobj.remove("createdDate");
            ticketobj.remove("modifiedDate");
        }
        return tickets.toString();
    }

}
