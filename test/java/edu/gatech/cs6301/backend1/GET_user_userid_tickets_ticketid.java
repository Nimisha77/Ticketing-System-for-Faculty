package edu.gatech.cs6301.backend1;

import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

public class GET_user_userid_tickets_ticketid extends TestBaseClass {
    // Purpose: Failure when user id is invalid/ user doesn't exist.
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = getTicket(invalidUser, 1);
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
            HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + validUser + "/tickets/" + "xyz");
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
            CloseableHttpResponse response = getTicket(validUser, 999);
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

    // Purpose: Successfully retrieve a ticket for a user using a ticket id.
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int ticketId = TicketSystemTestUtils.getIdFromResponse(response);

            response = getTicket(validUser, ticketId);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            ticket.setId(ticketId);

            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            String expectedJson = mapper.writeValueAsString(ticket);
            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    public CloseableHttpResponse getTicket(String userId, int ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
}
