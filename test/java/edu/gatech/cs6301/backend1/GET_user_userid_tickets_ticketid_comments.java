package edu.gatech.cs6301.backend1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import edu.gatech.cs6301.dto.Comment;
import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;
import org.apache.http.client.methods.*;
import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class GET_user_userid_tickets_ticketid_comments extends TestBaseClass {

    // Purpose: Failure when user id is invalid/ user doesn't exist.
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = getComments(invalidUser, 1);
            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Failure when ticket id is not an integer.
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // CloseableHttpResponse response = getTicketById("testUser", "xyz");
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + validUser + "/tickets/" + "xyz" + "/comments");
            httpRequest.addHeader("accept", "application/json");

            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");

            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Failure when ticket id is invalid/ ticket doesn't exist.
    @Test
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = getComments(validUser, 999);
            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Successfully retrieve comments for a user using a ticket id.
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            Comment comment1 = new Comment(validUser, new Date(), "Comment 1");
            Comment comment2 = new Comment("user2", new Date(), "Comment 2");

            TicketSystemTestUtils.createComment(validUser, ticketId, comment1, httpclient);
            TicketSystemTestUtils.createComment(validUser, ticketId, comment2, httpclient);
            ArrayList<Comment> comments = new ArrayList<Comment>();
            comments.add(comment1);
            comments.add(comment2);

            response = getComments(validUser, ticketId);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);

            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = mapper.writeValueAsString(comments);
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    public CloseableHttpResponse getComments(String userId, int ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId + "/comments");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
}
