package edu.gatech.cs6301.backend1;

import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;

import edu.gatech.cs6301.dto.Comment;
import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class POST_user_userid_tickets_ticketid_comments extends TestBaseClass {

    // Purpose: Failure to create comment when provided with invalid userID
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment();
            CloseableHttpResponse response = TicketSystemTestUtils.createComment(invalidUser, 1, comment, httpclient);
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

    // Purpose: Failure to create comment when ticketID is not an integer
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + validUser + "/tickets/" + "xyz" + "/comments");
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

    // Purpose: Failure to create comment when ticketID does not exist
    @Test
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment();
            CloseableHttpResponse response = TicketSystemTestUtils.createComment(validUser, 1000, comment,
                    httpclient);

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

    // Purpose: Failure to create comment when author length is 0
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment("", new Date(), "new comment");
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = TicketSystemTestUtils.createComment(validUser, ticketId, comment,
                    httpclient);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Failure to create comment when author userID does not exist
    @Test
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment(invalidUser, new Date(), "new comment");
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = TicketSystemTestUtils.createComment(validUser, ticketId, comment,
                    httpclient);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Failure to create comment when date is invalid or before time of
    // ticket creation
    @Test
    public void tsfsTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment(validUser, new Date(1), "new comment");
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = TicketSystemTestUtils.createComment(validUser, ticketId, comment,
                    httpclient);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Failure to create comment when content length is 0
    @Test
    public void tsfsTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment(validUser, new Date(), "");
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = TicketSystemTestUtils.createComment(validUser, ticketId, comment,
                    httpclient);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Successfully create a comment based on all valid formats of data
    @Test
    public void tsfsTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Comment comment = new Comment(validUser, new Date(), "new content");
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = TicketSystemTestUtils.createComment(validUser, ticketId, comment,
                    httpclient);

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
            String expectedJson = mapper.writeValueAsString(comment);
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }
}
