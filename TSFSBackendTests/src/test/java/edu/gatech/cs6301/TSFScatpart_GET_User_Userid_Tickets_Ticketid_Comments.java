package edu.gatech.cs6301;

import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Comment;
import edu.gatech.cs6301.schemas.Ticket;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TSFScatpart_GET_User_Userid_Tickets_Ticketid_Comments extends TestBase{
//    CloseableHttpClient httpclient;
//    String baseUrl;
//
//    public TSFScatpart_GET_User_Userid_Tickets_Ticketid_Comments() {
//        httpclient = TSFSBackendTestSuite.httpclient;
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//    }

    @Test
    // Purpose: If the user id is not found then the test should fail.
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid does not exist and therefore is invalid
            String invaliduserid = getInvalidUser();
            String validuserid = getValidFacultyUsers().get(2);
            Integer placeholder = null;

            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            // Now let's add a comment to the valid user and valid ticket
            Comment comment = new Comment(validuserid, null, "four tests left");
            Comment createdComment = utils.createComment(validuserid, tix.getId(), comment);


            // Now let's try to get the comment with a valid ticketid and an invalid userid
            CloseableHttpResponse response = getComments(invaliduserid, tix.getId());

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: If the user id is empty then the test should fail.
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();

        try {
            // now this userid we use in the getComments method is empty
            String validuserid = getValidFacultyUsers().get(2);
            String invaliduserid = "";
            Integer placeholder = null;

            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            // Now let's add a comment to the valid user and valid ticket
            Comment comment = new Comment(validuserid, null, "three tests left");
            Comment createdComment = utils.createComment(validuserid, tix.getId(), comment);


            // Now let's try to get the comment with a valid ticketid and an empty userid
            CloseableHttpResponse response = getComments(invaliduserid, tix.getId());

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: If the ticket id is not found then the test should fail.
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();

        try {
            Integer placeholder = null;

            String validuserid = getValidFacultyUsers().get(2);
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            // Now let's add a comment to the valid user and valid ticket
            Comment comment = new Comment(validuserid, null, "two tests left");
            Comment createdComment = utils.createComment(validuserid, tix.getId(), comment);

            // Now let's try to get the comment with an invalid ticketid and a valid user id
            // Let's assume that we do not have a ticket with id + 7 in the system
            CloseableHttpResponse response = getComments(validuserid, tix.getId()+100);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: If the ticket id is empty then the test should fail.
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            String validuserid = getValidFacultyUsers().get(2);
            Integer placeholder = null;

            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            // Now let's add a comment to the valid user and valid ticket
            Comment comment = new Comment(validuserid, null, "one test left");
            Comment createdComment = utils.createComment(validuserid, tix.getId(), comment);

            // Now let's try to get the comment with an empty ticketid and a valid user id
            CloseableHttpResponse response = getComments(validuserid, placeholder);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: If the ticket id and user id are valid then the test should succeed.
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            String validuserid = getValidFacultyUsers().get(2);
            Integer placeholder = null;

            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            // Now let's add a comment to the valid user and valid ticket
            Comment[] createdComment = new Comment[2];
            Comment comment = new Comment(validuserid, null, "last test!");
            createdComment[0] = utils.createComment(validuserid, tix.getId(), comment);

            comment = new Comment(validuserid, null, "one more last test!");
            createdComment[1] = utils.createComment(validuserid, tix.getId(), comment);

            // Now let's try to get the comment with a valid user id and a valid ticket id
            CloseableHttpResponse response = getComments(validuserid, tix.getId());

            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//            String strResponse = EntityUtils.toString(response.getEntity());

//            Gson gson = new Gson();
//            Comment[] commentsRecvd = gson.fromJson(strResponse, Comment[].class);

//            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
//            Assert.assertArrayEquals(createdComment, commentsRecvd);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }



    private CloseableHttpResponse getComments(String userid, Integer ticketid) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/"+ userid + "/tickets/" + ticketid +"/comments");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }


    private CloseableHttpResponse createTicket(String userid, Ticket ticket) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(ticket);
        StringEntity input = new StringEntity(json);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private CloseableHttpResponse createComments(String userid, int ticketid, Comment comment) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets/" + ticketid + "/comments");
        httpRequest.addHeader("accept", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(comment);

        StringEntity input = new StringEntity(json);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }



    private int getTicketIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id = getTicketIdFromStringResponse(strResponse);
        return Integer.valueOf(id);
    }

    private String getTicketIdFromStringResponse(String strResponse) throws JSONException {
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
