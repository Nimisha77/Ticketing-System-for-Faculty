package edu.gatech.cs6301.backend5;

import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Comment;
import edu.gatech.cs6301.schemas.Ticket;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments extends TestBase {

//    CloseableHttpClient httpclient;
//
//    String baseUrl;
//
//    public TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments() {
//        System.out.println("Constructor TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments");
//        httpclient = TSFSBackendTestSuite.httpclient;
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//
//        System.out.println("[TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments] baseUrl is: " + baseUrl);
//        System.out.println("[TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments] httpclient is: " + httpclient);
//    }

    public CloseableHttpResponse postRequest(String url, String body) throws IOException {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(body);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    // Purpose: Test api for checking if userid is valid
    @Test
    public void tsfsTest1() throws Exception {
        try{
            String validuserid = getValidFacultyUsers().get(0);
            String invaliduserid = getInvalidUser();

            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);


            String extendedUrl = "/user/" + invaliduserid + "/tickets/" + tix.getId() + "/comments";

            //Create dummy comment
            Comment comment = new Comment(validuserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test api for checking if ticketid is valid
    @Test
    public void tsfsTest2() throws Exception {
        try {
            String validuserid = getValidFacultyUsers().get(0);
            String invaliduserid = getInvalidUser();

            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            String extendedUrl = "/user/" + validuserid + "/tickets/" + getInvalidTicketId() + "/comments";

            //Create dummy comment
            Comment comment = new Comment(validuserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test api for checking if request body json of the request has necessary keys
    @Test
    public void tsfsTest3() throws Exception {
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            // Valid userid and ticketid
            String validuserid =getValidFacultyUsers().get(0);

            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            String extendedUrl = "/user/" + validuserid + "/tickets/" + tix.getId() + "/comments";

            //Create dummy comment
            Comment comment = new Comment(validuserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);

            // Introduce error in bodyJson string - Change key author to creator
            StringBuilder stringBuilder = new StringBuilder(bodyJson);
            stringBuilder.replace(2, 8, "creator");
            bodyJson = stringBuilder.toString();

            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


    // Purpose: Test api for checking the ID of the author of the comment is valid
    @Test
    public void tsfsTest4() throws Exception {
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            String validuserid = getValidFacultyUsers().get(0);
            String invaliduserid = getInvalidUser();
            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            String extendedUrl = "/user/" + validuserid + "/tickets/" + tix.getId() + "/comments";

            // Wrong authorId
            //Create dummy comment
            Comment comment = new Comment(invaliduserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test api for checking if createdDate timestamp of the comment is valid - This testcase is useless
    @Ignore
    @Test
    public void tsfsTest5() throws Exception {
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            String userid ="ananya123";
            String ticketid = "ABC123";
            String extendedUrl = "/user/" + userid + "/tickets/" + ticketid + "/comments";

            // Wrong createdDate format
            //Create dummy comment
            Comment comment = new Comment("naveen123", null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test api for checking if content of comment is valid
    @Ignore
    @Test
    public void tsfsTest6() throws Exception {
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();
            String validuserid = getValidFacultyUsers().get(0);


            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);

            String extendedUrl = "/user/" + validuserid + "/tickets/" + tix.getId() + "/comments";

            // Set comment content as @@@@
            //Create dummy comment
            Comment comment = new Comment(validuserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            StringBuilder stringBuilder = new StringBuilder(bodyJson);
            stringBuilder.replace(73, 102, "@@@@");
            bodyJson = stringBuilder.toString();

            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Test for successfull api hit given valid userid, ticketid, valid comment author, valid createdDate timestamp, valid content of the comment
    @Test
    public void tsfsTest7() throws Exception {

        try{
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();

            String validuserid = getValidFacultyUsers().get(0);
            // Create valid ticket for a valid user
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);


            String extendedUrl = "/user/" + validuserid + "/tickets/" + tix.getId() + "/comments";

            //Create dummy comment
            Comment comment = new Comment(validuserid, null, "first comment of the ticket");
            String bodyJson = gson.toJson(comment);
            CloseableHttpResponse response = postRequest(baseUrl + extendedUrl, bodyJson);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200, status);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }
}
