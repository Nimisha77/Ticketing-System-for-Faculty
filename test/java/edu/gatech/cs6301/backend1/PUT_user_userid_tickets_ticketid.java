package edu.gatech.cs6301.backend1;

import edu.gatech.cs6301.dto.Category;
import edu.gatech.cs6301.dto.Status;
import edu.gatech.cs6301.dto.Ticket;
import edu.gatech.cs6301.utils.TicketSystemTestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PUT_user_userid_tickets_ticketid extends TestBaseClass {
    // Purpose: Failure when userid not exist.
    @Test
    public void tsfsTest1() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            // Setting use invalid user id
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(invalidUser, id, input, httpclient);
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
    public void tsfsTest2() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            HttpPut httpRequest = new HttpPut(baseUrl + "/user/" + validUser + "/tickets/" + "xyz");
            httpRequest.addHeader("accept", "application/json");
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response " + response + "***");
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

    // Purpose: Failure when ticketid not exist.
    @Test
    public void tsfsTest3() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);
            // Setting use invalid ticket id
            int invalid_id = -999999999;
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, invalid_id, input,
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

    // Purpose: Failure when title of ticket id is changed
    @Test
    public void tsfsTest4() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setTitle("Changed Title");
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when description of ticket is changed
    @Test
    public void tsfsTest5() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setDescription("Changed Description");
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when updated category is invalid
    @Test
    public void tsfsTest6() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setCategory(Category.INVALID);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when category of ticket is changed
    @Test
    public void tsfsTest7() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setCategory(Category.meeting_organization);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when Email is updated
    @Test
    public void tsfsTest8() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setEmailAddress("abcd@efgh.com");
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when changed assignee does not exist
    @Test
    public void tsfsTest9() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ticket.setAssignee(invalidUser);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when any of the watchers does not exist
    @Test
    public void tsfsTest10() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(invalidUser);
            ticket.setWatchers(watchers);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when create date changed
    @Test
    public void tsfsTest11() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // setting new create
            Date newcreatedDate = new Date();
            ticket.setCreatedDate(newcreatedDate);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Failure when modifiedDate has incorrect format
    @Test
    public void tsfsTest12() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setCreatedDate(date);
            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Pass when Status changed Assignee changed Watchers become empty,
    // everything else unchanged
    @Test
    public void tsfsTest13() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // setting new status
            ticket.setStatus(Status.closed); // default status is open

            // setting new assignee
            String assigneeUser = "assigneeUser2"; // a valid userid
            ticket.setAssignee(assigneeUser);

            // setting watchers to be empty
            ticket.getWatchers().clear();

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when Status, Assignee, Watchers Modified Data Changed
    @Test
    public void tsfsTest14() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // Changing Modified Date
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);

            // Changing Watchers
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(validUser);
            ticket.setWatchers(watchers);

            // Changing Status
            Status statusChanged = Status.closed;
            ticket.setStatus(statusChanged);

            // Changing Assignee
            ticket.setAssignee(validUser);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass when Status changed Assignee changed, everything else unchanged
    @Test
    public void tsfsTest15() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // setting new status
            ticket.setStatus(Status.closed); // default status is open

            // setting new assignee
            String assigneeUser = "assigneeUser2"; // a valid userid
            ticket.setAssignee(assigneeUser);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when Status changed and Watchers changed and are now
    // empty i.e. no watchers
    @Test
    public void tsfsTest16() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            // Changing Status
            Status statusChanged = Status.closed;
            ticket.setStatus(statusChanged);
            // Changing Watchers
            ArrayList<String> watchers = new ArrayList<String>();
            ticket.setWatchers(watchers);

            // Changing Modified Date
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass when Status changed watchers changed (nonempty), everything
    // else unchanged
    @Test
    public void tsfsTest17() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);

            // setting new status
            ticket.setStatus(Status.closed); // default status is open

            // setting new watchers
            ArrayList<String> watchers = ticket.getWatchers();
            watchers.clear();
            watchers.add("watcher3"); // watcher3 and watcher3 are valid
            watchers.add("watcher4");

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when Status and modified date changed
    @Test
    public void tsfsTest18() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);
            // Changing Status
            Status statusChanged = Status.closed;
            ticket.setStatus(statusChanged);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
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

    // Purpose: Pass when Assignee changed and watchers changed to empty, everything
    // else unchanged
    @Test
    public void tsfsTest19() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);

            // setting new assignee
            String assigneeUser = "assigneeUser2"; // a valid userid
            ticket.setAssignee(assigneeUser);

            // clear watchers
            ticket.getWatchers().clear();

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when Assignee, Watchers and Modified Date is changed
    @Test
    public void tsfsTest20() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // Changing Modified Date
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);

            // Changing Watchers
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(validUser);
            ticket.setWatchers(watchers);

            // Changing Assignee
            ticket.setAssignee(validUser);

            // Changing Assignee
            ticket.setAssignee(validUser);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass when Assignee changed, everything else unchanged
    @Test
    public void tsfsTest21() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);

            // setting new assignee
            String assigneeUser = "assigneeUser2"; // a valid userid
            ticket.setAssignee(assigneeUser);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when Watchers changed to empty
    @Test
    public void tsfsTest22() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // Changing Modified Date
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);

            // Changing Watchers
            ArrayList<String> watchers = new ArrayList<String>();
            ticket.setWatchers(watchers);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass whenwatchers changed (nonempty), everything else unchanged
    @Test
    public void tsfsTest23() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket(); // default category is travel_authorization
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);
            ticket.setId(id);

            // setting new watchers
            ArrayList<String> watchers = ticket.getWatchers();
            watchers.clear();
            watchers.add("watcher3"); // watcher3 and watcher3 are valid
            watchers.add("watcher4");

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }

    // Purpose: Pass Condition when only modified date changed
    @Test
    public void tsfsTest24() throws Exception {
        httpclient = HttpClients.createDefault();
        try {
            // Setting up the initial valid ticket before using PUT operation
            Ticket ticket = TicketSystemTestUtils.getValidTicket();
            CloseableHttpResponse responseGet = TicketSystemTestUtils.createTicket(validUser, ticket, httpclient);
            int id = TicketSystemTestUtils.getIdFromResponse(responseGet);

            ticket.setId(id);

            // Changing Modified Date
            String dateString = "2023-03-01T20:20:51.740Z";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(dateString);
            ticket.setModifiedDate(date);

            StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
            CloseableHttpResponse response = TicketSystemTestUtils.updateTicket(validUser, id, input, httpclient);
            int status = response.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            System.out.println("*** Response status code " + " (" + response.getStatusLine().getStatusCode() + ") ***");
            response.close();
        } finally {
            httpclient.close();
        }
    }
}
