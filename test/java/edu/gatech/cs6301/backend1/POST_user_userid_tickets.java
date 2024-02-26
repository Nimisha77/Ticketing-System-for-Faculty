package edu.gatech.cs6301.backend1;

import edu.gatech.cs6301.dto.Category;
import edu.gatech.cs6301.dto.Status;
import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.ArrayList;

public class POST_user_userid_tickets extends TestBaseClass{

    // Purpose: Failure with invalid userid
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();

            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(invalidUser, ticket, httpclient);
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

    // Purpose: Failure when description for a ticket is not provided.
    @Test
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting description to an empty string
            ticket.setDescription("");

            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    // Purpose: Failure when title for a ticket is not provided.
    @Test
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting Title to an empty string
            ticket.setTitle("");

            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    // Purpose: Failure wrong status.
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting invalid status
            ticket.setStatus(Status.INVALID);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    // Purpose: Failure when title for a ticket is not provided.
    @Test
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting invalid category
            Category category = Category.INVALID;
            ticket.setCategory(category);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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


    //Failure invalid email format
    @Test
    public void tsfsTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting invalid email format
            String invalid_email = "1bc.3@";
            ticket.setEmailAddress(invalid_email);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    //Failure check for an incorrect assignee
    @Test
    public void tsfsTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting Title to an empty string
            ticket.setAssignee(invalidUser);

            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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


    //Failure invalid userid in watchers
    @Test
    public void tsfsTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting invalid userid in watchers
            ArrayList<String> watchers = ticket.getWatchers();
            watchers.add(invalidUser);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    //Failure to check incorrect date format
    @Test
    public void tsfsTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting incorrect date format
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setCreatedDate(date);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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


    //Failure invalid modifiedDate format
    @Test
    public void tsfsTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting incorrect date format
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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

    //Empty assignee passed check
    @Test
    public void tsfsTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            // Setting Assignee to an empty string
            ticket.setAssignee("");
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
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



    //Pass with all correct info
    @Test
    public void tsfsTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse response = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 201) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }


}
