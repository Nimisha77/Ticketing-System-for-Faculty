package edu.gatech.cs6301.backend1;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;

public class GET_user_userid_tickets extends TestBaseClass {

    // Purpose: Failure when userID does not exist
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = getTicketResponse(invalidUser);
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

    // Purpose: Successfully retrieve all tickets given a valid userID
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket1 = TicketSystemTestUtils.getValidTicket();
            Ticket ticket2 = TicketSystemTestUtils.getValidTicket();
            TicketSystemTestUtils.createTicket(validUser, ticket1, httpclient).close();
            TicketSystemTestUtils.createTicket(validUser, ticket2, httpclient).close();

            Ticket newTicket1 = new Ticket();
            Ticket newTicket2 = new Ticket();
            ArrayList<Ticket> ticketArr = new ArrayList<Ticket>();
            ticketArr.add(newTicket1);
            ticketArr.add(newTicket2);

            CloseableHttpResponse response = getTicketResponse(validUser);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String resulting_response;

            if (statusCode == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + statusCode);
            }

            resulting_response = EntityUtils.toString(entity); // message from http request

            System.out.println(
                    "*** String response " + resulting_response + " (" + statusCode + ") ***");

            String expectedJSON = mapper.writeValueAsString(ticketArr);
            JSONAssert.assertEquals(expectedJSON, resulting_response, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    public CloseableHttpResponse getTicketResponse(String userId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
}
