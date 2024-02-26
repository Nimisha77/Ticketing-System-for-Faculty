package edu.gatech.cs6301.backend3;

import com.google.gson.Gson;
import edu.gatech.cs6301.backend3.models.Category;
import edu.gatech.cs6301.backend3.models.Status;
import edu.gatech.cs6301.backend3.models.Ticket;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@FixMethodOrder(MethodSorters.DEFAULT)
public class PUT_user_userid_tickets_ticketid {

    TSFSBackendClient client = new TSFSBackendClient();

    @Before
    public void runBefore() {
        client.open();
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }
// ***** Auxiliary functions *****
    private String getTicketJson (Ticket ticket){
        Gson gson = new Gson();
        String jsonString = gson.toJson(ticket);
        return jsonString;
    }

    private Ticket getValidTicket(){
        Ticket validTicket = new Ticket();

        validTicket.setId(getValidTicketId());
        validTicket.setTitle("Valid Title");
        validTicket.setDescription("Valid description of ticket");
        validTicket.setStatus(Status.OPEN);
        validTicket.setCategory(Category.STUDENT_HIRING);
        validTicket.setEmailAddress("tgarg40@gatech.edu");
        validTicket.setAssignee("sshah672");
        validTicket.setWatchers(new ArrayList<>(Arrays.asList("sshah672", "asalian3")));
        validTicket.setCreatedDate("2023-01-19 10:10:10");
        validTicket.setModifiedDate("2023-01-20 10:10:10");
        return validTicket;
    }
    private Ticket getUpdateTicket(Status status, String assignee, List<String> watchers, String modifiedDate){
        Ticket updateTicket = getValidTicket();

        if(status == null){
            status = updateTicket.getStatus();
        }
        if(assignee.equals("")){
            assignee = updateTicket.getAssignee();
        }
        if(watchers == null){
            watchers = updateTicket.getWatchers();
        }

        updateTicket.setStatus(status);
        updateTicket.setAssignee(assignee);
        updateTicket.setWatchers(watchers);
        updateTicket.setModifiedDate(modifiedDate);
        return updateTicket;
    }
    private int getValidTicketId(){
        return 1;
    }
    private String getValidUserId(){
        return "tgarg40";
    }
// ***************************************

//        Test Case 1  		<error>
//        Path UserId Validity :  UserId Invalid
    @Test
    public void tsfsTest1 () throws IOException {
            /*purpose:  Make a Request with a userId that doesn't exist
                        Expects a response with status not found (404)*/

        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");
        String invalidUserId = "InvalidUser";
        String validTicketJson = getTicketJson(validTicket);
        String updateTicketJson = getTicketJson(updateTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(invalidUserId, Integer.toString(ticketId), updateTicketJson);
            
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 2  		<error>  (follows [if])
//        Path UserId Access :  UserId Unauthorized
    @Test
    public void tsfsTest2() throws IOException {
        /*  Purpose:    Make a request with an unauthorised userId
        *               Expects a response with status unauthorised user (401)*/
        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");
        String invalidUserId = "unAuthUser01";
        String validTicketJson = getTicketJson(validTicket);
        String updateTicketJson = getTicketJson(updateTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(invalidUserId, Integer.toString(ticketId), updateTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=401){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

//        Test Case 3  		<error>
//        Path TicketId Type :  Not Integer
    @Test
    public void tsfsTest3() throws IOException{
            /*  Purpose:    Make a request with a ticketId that is ill-formed (not an integer)
         *               Expects a response with status Bad Request (400)*/

        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        String invalidTicketId = Integer.toString(ticketId) + "garbage";

        Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");

        String validTicketJson = getTicketJson(validTicket);
        String updateTicketJson = getTicketJson(updateTicket);

        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(validUserId, invalidTicketId, updateTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//         Test Case 4  		<error>  (follows [if])
//         Path TicketId Validity :  Ticket  Not Present
    @Test
    public void tsfsTest4() throws IOException{
            /* Purpose: Make a request with a ticketid that doesn't exist
                        Expect a Bad response with code 404
            */

        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        int invalidticketId = 2;   //should not exist

        Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");

        String validTicketJson = getTicketJson(validTicket);
        String updateTicketJson = getTicketJson(updateTicket);
    
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(validUserId, Integer.toString(invalidticketId), updateTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

//        Test Case 5  		<error>
//        Request Body Type :  Does Not Match Schema Ticket
    @Test
    public void tsfsTest5() throws IOException{
            /* Purpose: Make a request with an incorrect ticket schema
                        Expect a Bad response with code 400
            */

        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        String validTicketJson = getTicketJson(validTicket);
        String invalidTicketJson = "{ \"randomField1\": \"randomvalue1\"," +
                "  \"randomField2\": \"randomvalue2\"}";
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

//      Test Case 6  		<single>
//      Ticket Field ID Present :  Field Left Empty

    @Test
        public void tsfsTest6() throws IOException{
                /* Purpose: Test updating a ticket with an empty ticketId field
                            Expect an OK response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");
            updateTicket.setId(-1);
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);
           
            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 7  		<error>  (follows [if])
//      Ticket Field ID Value :  Doesn't match Path TicketId

    @Test
    public void tsfsTest7() throws IOException{
            /* Purpose: Test updating a ticket with a ticketId field that doesn't 
                        match the path ticketId
                        Expect a Bad response with code 400
            */

        Ticket validTicket = getValidTicket();
        Ticket updateTicket = getUpdateTicket(Status.CLOSED, "", null, "2023-01-21 10:10:10");
        updateTicket.setId(17);
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        String validTicketJson = getTicketJson(validTicket);
        String updateTicketJson = getTicketJson(updateTicket);
    
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

//      Test Case 8  		<single>
//      Ticket Field Status Present :  Not Present

    @Test
        public void tsfsTest8() throws IOException{
                /* Purpose: Test updating a ticket without a status field
                            Expect a OK response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(null,"valid123", new ArrayList<>(Arrays.asList("tgarg40")), "2023-01-21 10:10:10");
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String invalidTicketJson = getTicketJson(updateTicket);
        
            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 9  		<error>  (follows [if])
//      Ticket Field Status Values (Open, Closed, Needs Attention) :  Invalid

    @Test
        public void tsfsTest9() throws IOException{
                /* Purpose: Test updating a ticket with an invalid status field
                            Expect a Bad response with code 400
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.INVALID,"valid123", new ArrayList<>(Arrays.asList("tgarg40")), "2023-01-21 10:10:10");
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String invalidTicketJson = getTicketJson(updateTicket);
        
            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=400){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 10 		<error>  (follows [if])
//      Ticket Field Assignee Validity :  Assignee UserId Invalid

    @Test
    public void tsfsTest10() throws IOException{
            /* Purpose: Test updating a ticket with an invalid assignee field
                        Expect a Bad response with code 404
            */

        Ticket validTicket = getValidTicket();
        Ticket updateTicket = getUpdateTicket(null,"invalid123", null, "2023-01-21 10:10:10");
        String validUserId = getValidUserId();
        int ticketId = getValidTicketId();
        String validTicketJson = getTicketJson(validTicket);
        String invalidTicketJson = getTicketJson(updateTicket);

        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

//      Test Case 11 		<single>  (follows [if])
//      Ticket Field Watchers Size :  Watchers List Empty

    @Test
        public void tsfsTest11() throws IOException{
                /* Purpose: Test updating a ticket with an empty watchers field
                            Expect an OK response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", new ArrayList<>(), "2023-01-21 10:10:10");
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 12 		<error>  (follows [if])
//      Ticket Field Watchers Validity :  At least one Watcher Invalid

    @Test
        public void tsfsTest12() throws IOException{
                /* Purpose: Test updating a ticket with an invalid watcher in the watchers field
                            Expect a Bad response with code 400
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", new ArrayList<>(Arrays.asList("valid1", "inValid2")), "2023-01-21 10:10:10");
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String invalidTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=400){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 13 		<error>
//      Ticket Field Modified Date Format :  Invalid Date

    @Test
        public void tsfsTest13() throws IOException{
                /* Purpose: Test updating a ticket with an invalid modified date field
                        Expect response with code 400
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", null, "2023-21-01 10:10:10");
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String invalidTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=400){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//      Test Case 14 		<error>
//      Ticket Field Modified Date Validity :  At or Before Created Date

    @Test
        public void tsfsTest14() throws IOException{
                /* Purpose: Test updating a ticket with a modified date at or before
                            the created date
                        Expect response with code 400
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", null, validTicket.getCreatedDate());
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String invalidTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), invalidTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=400){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

//          Test Case 15 		(Key = 1.1.1.1.1.1.1.1.1.1.1.1.2.1.1.2.)
//      Path UserId Validity                                       :  UserId Valid
//      Path UserId Access                                         :  UserId Authorized
//      Path TicketId Type                                         :  Integer
//      Path TicketId Validity                                     :  Ticket Present
//      Request Body Type                                          :  Matches Schema Ticket
//      Ticket Field ID Present                                    :  Field Present
//      Ticket Field ID Value                                      :  Matches Path TicketId
//      Ticket Field Status Present                                :  Present
//      Ticket Field Status Values (Open, Closed, Needs Attention) :  Valid
//      Ticket Field Assignee Present                              :  Present
//      Ticket Field Assignee Validity                             :  Assignee UserId Valid
//      Ticket Field Watchers Present                              :  Present
//      Ticket Field Watchers Size                                 :  Watchers List Not Empty
//      Ticket Field Watchers Validity                             :  All Watchers Are Valid
//      Ticket Field Modified Date Format                          :  Valid Date
//      Ticket Field Modified Date Validity                        :  After Created Date

    @Test
        public void tsfsTest15() throws IOException{
                /* Purpose: test creating a ticket successfully
                        Expect response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"123valid", new ArrayList<>(Arrays.asList("valid1", "valid2")), "2023-01-21 10:10:10");
            
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }

        // Test Case 16 		(Key = 1.1.1.1.1.1.1.1.1.1.1.2.0.0.1.2.)
        // Path UserId Validity                                       :  UserId Valid
        // Path UserId Access                                         :  UserId Authorized
        // Path TicketId Type                                         :  Integer
        // Path TicketId Validity                                     :  Ticket Present
        // Request Body Type                                          :  Matches Schema Ticket
        // Ticket Field ID Present                                    :  Field Present
        // Ticket Field ID Value                                      :  Matches Path TicketId
        // Ticket Field Status Present                                :  Present
        // Ticket Field Status Values (Open, Closed, Needs Attention) :  Valid
        // Ticket Field Assignee Present                              :  Present
        // Ticket Field Assignee Validity                             :  Assignee UserId Valid
        // Ticket Field Watchers Present                              :  Not Present
        // Ticket Field Watchers Size                                 :  <n/a>
        // Ticket Field Watchers Validity                             :  <n/a>
        // Ticket Field Modified Date Format                          :  Valid Date
        // Ticket Field Modified Date Validity                        :  After Created Date

    @Test
        public void tsfsTest16() throws IOException{
                /* Purpose: test creating a ticket successfully
                        Expect response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"123valid", null, "2023-01-21 10:10:10");
            
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }


        // Test Case 17 		(Key = 1.1.1.1.1.1.1.1.1.2.0.1.2.1.1.2.)
        // Path UserId Validity                                       :  UserId Valid
        // Path UserId Access                                         :  UserId Authorized
        // Path TicketId Type                                         :  Integer
        // Path TicketId Validity                                     :  Ticket Present
        // Request Body Type                                          :  Matches Schema Ticket
        // Ticket Field ID Present                                    :  Field Present
        // Ticket Field ID Value                                      :  Matches Path TicketId
        // Ticket Field Status Present                                :  Present
        // Ticket Field Status Values (Open, Closed, Needs Attention) :  Valid
        // Ticket Field Assignee Present                              :  Not Present
        // Ticket Field Assignee Validity                             :  <n/a>
        // Ticket Field Watchers Present                              :  Present
        // Ticket Field Watchers Size                                 :  Watchers List Not Empty
        // Ticket Field Watchers Validity                             :  All Watchers Are Valid
        // Ticket Field Modified Date Format                          :  Valid Date
        // Ticket Field Modified Date Validity                        :  After Created Date


    @Test
        public void tsfsTest17() throws IOException{
                /* Purpose: test creating a ticket successfully
                        Expect response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", new ArrayList<>(Arrays.asList("valid1", "valid2")), "2023-01-21 10:10:10");

            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }


        // Test Case 18 		(Key = 1.1.1.1.1.1.1.1.1.2.0.2.0.0.1.2.)
        // Path UserId Validity                                       :  UserId Valid
        // Path UserId Access                                         :  UserId Authorized
        // Path TicketId Type                                         :  Integer
        // Path TicketId Validity                                     :  Ticket Present
        // Request Body Type                                          :  Matches Schema Ticket
        // Ticket Field ID Present                                    :  Field Present
        // Ticket Field ID Value                                      :  Matches Path TicketId
        // Ticket Field Status Present                                :  Present
        // Ticket Field Status Values (Open, Closed, Needs Attention) :  Valid
        // Ticket Field Assignee Present                              :  Not Present
        // Ticket Field Assignee Validity                             :  <n/a>
        // Ticket Field Watchers Present                              :  Not Present
        // Ticket Field Watchers Size                                 :  <n/a>
        // Ticket Field Watchers Validity                             :  <n/a>
        // Ticket Field Modified Date Format                          :  Valid Date
        // Ticket Field Modified Date Validity                        :  After Created Date

        @Test
        public void tsfsTest18() throws IOException{
                /* Purpose: test creating a ticket successfully
                        Expect response with code 200
                */

            Ticket validTicket = getValidTicket();
            Ticket updateTicket = getUpdateTicket(Status.CLOSED,"", null, "2023-01-21 10:10:10");
            
            String validUserId = getValidUserId();
            int ticketId = getValidTicketId();
            String validTicketJson = getTicketJson(validTicket);
            String updateTicketJson = getTicketJson(updateTicket);

            try{
                CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
                response.close();

                response = client.updateTicket(validUserId, Integer.toString(ticketId), updateTicketJson);
                int status = response.getStatusLine().getStatusCode();
                if (status!=200){
                    throw new ClientProtocolException();
                }
                response.close();
            } finally {
                client.close();
            }
        }
}