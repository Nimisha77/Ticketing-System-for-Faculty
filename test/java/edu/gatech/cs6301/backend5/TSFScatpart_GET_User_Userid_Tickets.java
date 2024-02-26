package edu.gatech.cs6301.backend5;

import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Ticket;
import edu.gatech.cs6301.schemas.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TSFScatpart_GET_User_Userid_Tickets extends TestBase{

//    CloseableHttpClient httpclient;
//
//    String baseUrl;
//
//    public TSFScatpart_GET_User_Userid_Tickets() {
//        System.out.println("Constructor TSFScatpart_GET_User_Userid_Tickets ");
//        httpclient = TSFSBackendTestSuite.httpclient;
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//
//        System.out.println("[TSFScatpart_GET_User_Userid_Tickets] baseurl is: " + baseUrl);
//        System.out.println("[TSFScatpart_GET_User_Userid_Tickets] httpclient is: " + httpclient);
//    }

    // Purpose: Test for successfull api hit given valid userid

    // Write a function that generates a random 7 character alphanumeric string


    @Test
    public void tsfsTest1() throws Exception {
        System.out.println("TSFScatpart_GET_User_Userid_Tickets tsfsTest1 STARTED");
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();
            Utils utils = new Utils();
            User newUser = utils.createUser(new User(utils.generateRandomString(), "randomName", "randomemail@gmail.com", User.Affiliation.faculty));

            // valid userid
            String validuserid = newUser.getGT_username();
            String extendedUrl = "/user/" + validuserid + "/tickets";

//            --------------------------------- Creating Expected Tickets ---------------------------------
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(0));
                    add(getValidFacultyUsers().get(1));
                }
            };
            Ticket[] expectedTickets = new Ticket[2];

            Ticket ticket = new Ticket(0, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            expectedTickets[0] = utils.createTicket(validuserid, ticket);
            ticket = new Ticket(0, "title2", "ticket test2", Ticket.Status.open, Ticket.Category.miscellaneous, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            expectedTickets[1] = utils.createTicket(validuserid, ticket);

            System.out.println("hereee");
            CloseableHttpResponse response = getRequest(baseUrl + extendedUrl);

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
            Ticket[] tickets = gson.fromJson(strResponse, Ticket[].class);

            Assert.assertArrayEquals(expectedTickets, tickets);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }

    }
    // Purpose: Test api for checking if userid is valid
    @Test
    public void tsfsTest2() throws Exception {

        System.out.println("TSFScatpart_GET_User_Userid_Tickets tsfsTest2 STARTED");
        try {
            httpclient = HttpClients.createDefault();
            Gson gson = new Gson();
            // Invalid userid
            String userId = "xxxxxx";
            String extendedUrl = "/user/" + userId + "/tickets";

            CloseableHttpResponse response = getRequest(baseUrl + extendedUrl);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }

    }


    public CloseableHttpResponse getRequest(String url) throws IOException {

        HttpGet httpRequest = new HttpGet(url);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public void createSomeTickets() {

    }

//    public void func() {
//        ArrayList<String> list = new ArrayList<String>(
//                Arrays.asList("hello", "world", "foo", "bar")
//        );
//        System.out.println("list: " + list);
//        Ticket tic = new Ticket(100, "Naveen", "Desc", Ticket.Status.open, Ticket.Category.proposals, "email@gatech.edu", "assigneeID", list, "createdDate", "modifiedDate");
//
//        System.out.println(tic.toString());
//
//        Gson gson = new Gson();
//
//        User user = new User("nnarayanan33", "naveen", "naveen@gatech.edu", User.Affiliation.faculty);
//        System.out.println("Old user obj: " + user.toString());
//        user.setGT_username("naveennarayanan");
//        System.out.println("New user obj: " + user.toString());
//        String json = gson.toJson(user);
//        System.out.println("Json string" + json);
//
//        Comment comment = new Comment("naveen123", "2023-02-28T22:17:59.238Z", "first comment of the ticket");
//        String bodyJson = gson.toJson(comment);
//        System.out.println("Comment: " + bodyJson);
//        StringBuilder stringBuilder = new StringBuilder(bodyJson);
//        stringBuilder.replace(73, 102, "@@@@");
//        String editedString = stringBuilder.toString();
//        System.out.println("Comment: " + editedString);
//    }

}
