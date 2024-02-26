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


@FixMethodOrder(MethodSorters.DEFAULT)
public class POST_user_userid_tickets {

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
        validTicket.setEmailAddress("sshah672@gatech.edu");
        validTicket.setAssignee("sshah672");
        validTicket.setWatchers(new ArrayList<>(Arrays.asList("sshah672", "asalian3")));
        validTicket.setCreatedDate("2023-01-19 10:10:10");
        validTicket.setModifiedDate("2023-01-20 10:10:10");
        return validTicket;
    }
    private int getValidTicketId(){
        return 1;
    }
    private String getValidUserId(){
        return "sshah672";
    }
// ***************************************


//        Test Case 1  		<error>
//        Path UserId Validity :  UserId Invalid
    @Test
    public void tsfsTest1 () throws IOException {
            /*purpose:  Make a Request with a userId that doesn't exist
                        Expects a response with status not found (404)*/

        Ticket inValidTicket = getValidTicket();
        String invalidUserId = "InvalidUser";
        String validTicketJson = getTicketJson(inValidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(invalidUserId, validTicketJson);
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
        Ticket invalidTicket = getValidTicket();
        String invalidUserId = "UnauthUser01";
        String inValidTicketJson = getTicketJson(invalidTicket);;
        try{
            CloseableHttpResponse response = client.createTicket(invalidUserId, inValidTicketJson);
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
//        Request Body Type :  Does Not Match Schema Ticket
    @Test
    public void tsfsTest3() throws IOException{
            /* Purpose: Make a request with an incorrect ticket schema
                        Expect a Bad response with code 400
             */

        String invalidTicket = "validUserId";
        String invalidTicketJson = "{ \"randomField1\": \"randomvalue1\"," +
                "  \"randomField2\": \"randomvalue2\"}";
        try{
            CloseableHttpResponse response = client.createTicket(invalidTicket, invalidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 4  		<single>
//        Ticket Field Title Length :  Empty
    @Test
    public void tsfsTest4() throws IOException{
            /* Purpose: test creating a ticket with an empty title field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setTitle("");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }

    }



//        Test Case 5  		<error>
//        Ticket Field ID Present :  Field Present
    @Test
    public void tsfsTest5() throws IOException{
            /* Purpose: Test whether a custom  ticket ID has been provided by user while creating ticket
                        Expect a response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setId(99);
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }

    }


//        Test Case 6  		<single>
//        Ticket Field Description Length :  Empty
    @Test
    public void tsfsTest6() throws IOException{
            /* Purpose: test creating a ticket with an empty description field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setCreatedDate("");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 7  		<error>
//        Ticket Field Status Values (Open, Closed, Needs Attention) :  Invalid
    @Test
    public void tsfsTest7() throws IOException{
            /* Purpose: test creating a ticket with an invalid Status field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setStatus(Status.INVALID);
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }

    }


//        Test Case 8  		<error>
//        Ticket Field Category Values (travel authorization, reimbursement, meeting organi :  Invalid
    @Test
    public void tsfsTest8() throws IOException{
        /* Purpose: test creating a ticket with an invalid Category field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setCategory(Category.INVALID);
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 9  		<single>
//        Ticket Field Email Address Format :  Empty
    @Test
    public void tsfsTest9() throws IOException{
            /* Purpose: test creating a ticket with an empty email address field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setEmailAddress("");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 10 		<error>
//        Ticket Field Email Address Format :  Invalid Email Address
    @Test
    public void tsfsTest10() throws IOException{
            /* Purpose: test creating a ticket with an invalid email field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setEmailAddress("invalidEmailFormat.com");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 11 		<error>
//        Ticket Field Assignee Length :  Assignee UserId Empty
    @Test
    public void tsfsTest11() throws IOException{
            /* Purpose: test creating a ticket with an empty assignee field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setAssignee("");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 12 		<error>
//        Ticket Field Assignee Validity :  Assignee UserId Invalid
    @Test
    public void tsfsTest12() throws IOException {
            /* Purpose: test creating a ticket with an invalid assignee field
                        Expect response with code 404
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setAssignee("123invalid");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 13 		<single>
//        Ticket Field Watchers Size :  Watchers List Empty
    @Test
    public void tsfsTest13() throws IOException{
        /* Purpose: test creating a ticket with an empty watchers field
                        Expect response with code 404
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setWatchers(new ArrayList<>());
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 14 		<error>  (follows [if])
//        Ticket Field Watchers Validity :  At least one Watcher Invalid
    @Test
    public void tsfsTest14() throws IOException{
            /* Purpose: test creating a ticket with an invalid watcher in the watchers field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setWatchers(new ArrayList<>(Arrays.asList("valid1", "inValid2")));
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 15 		<error>
//        Ticket Field Created Date Format :  Invalid Date
    @Test
    public void tsfsTest15() throws IOException{
            /* Purpose: test creating a ticket with an invalid created date field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setCreatedDate("2000-01-19 10:10:10");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 16 		<error>
//        Ticket Field Modified Date Format :  Invalid Date
    @Test
    public void tsfsTest16() throws IOException{
             /* Purpose: test creating a ticket with an invalid modified date field
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setModifiedDate("2000-01-19 10:10:10");
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


//        Test Case 17 		<error>
//        Ticket Field Modified Date Validity :  Doesn't match Created Date
    @Test
    public void tsfsTest17() throws IOException{
            /* Purpose: test creating a ticket with an invalid modified date field which does not meet the created date
                        Expect response with code 400
             */
        Ticket invalidTicket = getValidTicket();
        invalidTicket.setModifiedDate(invalidTicket.getCreatedDate());
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(invalidTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    /*
     Test Case 18 		(Key = 1.1.1.2.2.2.1.1.2.2.1.2.1.1.1.1.)
    Path UserId Validity                                                              :  UserId Valid
    Path UserId Access                                                                :  UserId Authorized
    Request Body Type                                                                 :  Matches Schema Ticket
    Ticket Field Title Length                                                         :  Not Empty
    Ticket Field ID Present                                                           :  Field Left Empty
    Ticket Field Description Length                                                   :  Not Empty
    Ticket Field Status Values (Open, Closed, Needs Attention)                        :  Valid
    Ticket Field Category Values (travel authorization, reimbursement, meeting organi :  Valid
        Ticket Field Email Address Format                                                 :  Valid Email Address
        Ticket Field Assignee Length                                                      :  Assignee UserId Not Empty
        Ticket Field Assignee Validity                                                    :  Assignee UserId Valid
        Ticket Field Watchers Size                                                        :  Watchers List Not Empty
        Ticket Field Watchers Validity                                                    :  All Watchers Are Valid
        Ticket Field Created Date Format                                                  :  Valid Date
        Ticket Field Modified Date Format                                                 :  Valid Date
        Ticket Field Modified Date Validity
    */
    @Test
    public void tsfsTest18() throws IOException{
            /* Purpose: test creating a ticket successfully
                        Expect response with code 200
             */
        Ticket validTicket = getValidTicket();
        String validUserId = "validUserId";
        String inValidTicketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, inValidTicketJson);
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
