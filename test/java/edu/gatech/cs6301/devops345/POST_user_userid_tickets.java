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

public class POST_user_userid_tickets {

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
    
    //Purpose: Ensures that given user id is an existing user; if nonexistent, should return 404 error
    @Test
    public void tsfsTest1() throws Exception {
        deleteTickets("jdoe10");
        try {
            String missingUser = "jdoe100asdfghjkl3456zxcvbnm";
            CloseableHttpResponse response =
		createTicket(missingUser, "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that given title is nonzero length
    @Test
    public void tsfsTest2() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10 exists as a user
            CloseableHttpResponse response =
       createTicket("jdoe10", "", "My problem is about many problems", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
           
            EntityUtils.consume(response.getEntity());
            response.close();
       } finally {
            httpclient.close();
       }
    }

    //Purpose: Ensures that given description is nonzero length
    @Test
    public void tsfsTest3() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10 exists as a user
            CloseableHttpResponse response =
        createTicket("jdoe10", "My Title!", "", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that given category is existing category
    @Test
    public void tsfsTest4() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10 exists?
            String nonexistentCategory = "cute cats";
            CloseableHttpResponse response =
        createTicket("jdoe10", "My Title!", "My problem is about many problems", nonexistentCategory, "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that given category is formatted correctly 
    @Test
    public void tsfsTest5() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10 exists as a user
            String nonexistentCategory = "travel_authorization";
            CloseableHttpResponse response =
        createTicket("jdoe10", "My Title!", "My problem is about many problems", nonexistentCategory, "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that given email is formatted correctly 
    @Test
    public void tsfsTest6() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10, pkim20, mriley3 exists??
            String nonexistentEmail = "joeissupercool";
            CloseableHttpResponse response =
        createTicket("jdoe10", "My Title!", "My problem is about many problems", "miscellaneous", nonexistentEmail , "pkim20", new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that given assignee is existing user 
    @Test
    public void tsfsTest7() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10, pkim20, mriley3 exists??
            String nonexistentAssignee = "asdfghjksdfg93dfgh823fghgf2";
            CloseableHttpResponse response =
        createTicket("jdoe10", "My Title!", "My problem is about many problems", "miscellaneous", "jdoe10@gatech.edu", nonexistentAssignee, new String[] {"mriley3"}, true);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: ensures that watchers is not incorrect format (string instead of array)
    @Test
    public void tsfsTest8() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10, pkim20, mriley3 exists??
            
            String userid = "jdoe10";
            String title = "My Title!";
            String description = "My problem is about many problems";
            String category = "miscellaneous";
            String emailAddress = "jdoe10@gatech.edu";
            String assignee = "pkim20";
            String watchers = "mriley3";
            
            HttpPost httpRequest = new HttpPost(baseUrl + "/api/" + userid + "/tickets");
            httpRequest.addHeader("accept", "application/json");
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
            }
            StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                    "\"description\":\"" + description + "\"," +
                    "\"category\":\"" + category + "\"," +
                    "\"emailAddress\":\"" + emailAddress + "\"," + 
                    "\"assignee\":\"" + assignee + "\"," +
                    "\"watchers\":\"" + watchers + "\"}");
    
            input.setContentType("application/json");
            httpRequest.setEntity(input);
    
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: ensures that watchers items are not incorrect format (int instead of string)
    @Test
    public void tsfsTest9() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10, pkim20, mriley3 exists??
            
            String userid = "jdoe10";
            String title = "My Title!";
            String description = "My problem is about many problems";
            String category = "miscellaneous";
            String emailAddress = "jdoe10@gatech.edu";
            String assignee = "pkim20";
            int[] watchers = new int[]{3, 2343, 60};
            
            HttpPost httpRequest = new HttpPost(baseUrl + "/api/" + userid + "/tickets");
            httpRequest.addHeader("accept", "application/json");
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
            }
            StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                    "\"description\":\"" + description + "\"," +
                    "\"category\":\"" + category + "\"," +
                    "\"emailAddress\":\"" + emailAddress + "\"," + 
                    "\"assignee\":\"" + assignee + "\"," +
                    "\"watchers\":\"" + watchers + "\"}");
    
            input.setContentType("application/json");
            httpRequest.setEntity(input);
    
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: ensures that watcher items are existing users
    @Test
    public void tsfsTest10() throws Exception {
        deleteTickets("jdoe10");
        try {
            // assuming jdoe10, pkim20, mriley3 exists??
            
            String userid = "jdoe10";
            String title = "My Title!";
            String description = "My problem is about many problems";
            String category = "miscellaneous";
            String emailAddress = "jdoe10@gatech.edu";
            String assignee = "pkim20";
            String[] watchers = new String[] {"mriley3dfghjkfdfyghik65343", "569763gdhhshdfgs"};
            
            HttpPost httpRequest = new HttpPost(baseUrl + "/api/" + userid + "/tickets");
            httpRequest.addHeader("accept", "application/json");
            boolean isAuthenticated = true;
            if (isAuthenticated) {
                httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
            }

            StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                    "\"description\":\"" + description + "\"," +
                    "\"category\":\"" + category + "\"," +
                    "\"emailAddress\":\"" + emailAddress + "\"," + 
                    "\"assignee\":\"" + assignee + "\"," +
                    "\"watchers\":\"" + watchers + "\"}");
    
            input.setContentType("application/json");
            httpRequest.setEntity(input);
    
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");


            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that unauthenticated user throws a 401 error. 
    @Test
    public void tsfsTest11() throws Exception {
        deleteTickets("jdoe10");
        try {
            //setting user as unauthenticated 
            boolean isAuthenticated = false;
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {"mriley3"}, isAuthenticated);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(401, status);
            
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that watchers are not required to create ticket (length of array can be 0)
    @Test
    public void tsfsTest12() throws Exception {
        deleteTickets("jdoe10");
        try {
            
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", new String[] {}, true);

            int status = response.getStatusLine().getStatusCode();
            
            HttpEntity entity;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            String strResponse = removeDateFieldsFromJsonStr(EntityUtils.toString(entity));

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String id = getIdFromStringResponse(strResponse);

            String expectedJson = "{\"id\":\"" + id + "\",\"title\":\"My Ticket\",\"description\":\"My problem is xyz\",\"status\":\"open\",\"category\":\"miscellaneous\",\"emailAddress\":\"jdoe10@gatech.edu\", \"assignee\":\"pkim20\", \"watchers\":[\"\"]}";
	        JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ensures that multiple watchers are accepted when creating ticket (length of array can be > 0)
    @Test
    public void tsfsTest13() throws Exception {
        deleteTickets("jdoe10");
        try {
            String[] w = new String[] {"mriley3", "lmontoya2"};
            CloseableHttpResponse response =
		createTicket("jdoe10", "My Ticket", "My problem is xyz", "miscellaneous", "jdoe10@gatech.edu" , "pkim20", w, true);

            int status = response.getStatusLine().getStatusCode();
            
            HttpEntity entity;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            String strResponse = removeDateFieldsFromJsonStr(EntityUtils.toString(entity));

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String id = getIdFromStringResponse(strResponse);

            String expectedJson = "{\"id\":\"" + id + "\",\"title\":\"My Ticket\",\"description\":\"My problem is xyz\",\"status\":\"open\",\"category\":\"miscellaneous\",\"emailAddress\":\"jdoe10@gatech.edu\", \"assignee\":\"pkim20\", \"watchers\":[\"mriley3\", \"lmontoya2\"]}";
	        JSONAssert.assertEquals(expectedJson,strResponse, false);

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


 
    // MY PRIVATE METHODS ***********
    private CloseableHttpResponse createTicket(String userid, String title, String description, String category, String emailAddress, String assignee, String[] watchers, boolean isAuthenticated) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        if (isAuthenticated) {
            httpRequest.addHeader("Authorization", "Bearer placeholder_token_1234");
        }
        
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," + 
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + watchers + "\"}");

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
    
    private CloseableHttpResponse deleteTickets(String userid) throws IOException {
        HttpDelete httpDelete = new HttpDelete(baseUrl + "/user/" + userid + "/tickets");
            httpDelete.addHeader("accept", "application/json");
    
            System.out.println("*** Executing request " + httpDelete.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            System.out.println("*** Raw response " + response + "***");
            // EntityUtils.consume(response.getEntity());
            // response.close();
            return response;
    }

    private String removeDateFieldsFromJsonStr(String str) throws JSONException {
        JSONObject object = new JSONObject(str);
        object.remove("createdDate");
        object.remove("modifiedDate");

        return object.toString();
    }

    
    //END MY PRIVATE METHODS *********


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

}
