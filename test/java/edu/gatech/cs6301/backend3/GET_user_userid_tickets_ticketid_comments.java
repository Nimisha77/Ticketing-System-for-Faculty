package edu.gatech.cs6301.backend3;

import com.google.gson.Gson;
import edu.gatech.cs6301.backend3.models.Category;
import edu.gatech.cs6301.backend3.models.Comment;
import edu.gatech.cs6301.backend3.models.Status;
import edu.gatech.cs6301.backend3.models.Ticket;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;


@FixMethodOrder(MethodSorters.DEFAULT)
public class GET_user_userid_tickets_ticketid_comments {
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
        validTicket.setEmailAddress("xchen786@gatech.edu");
        validTicket.setAssignee("xchen786");
        validTicket.setWatchers(new ArrayList<>(Arrays.asList("xchen786", "asalian3")));
        validTicket.setCreatedDate("2023-01-19 10:10:10");
        validTicket.setModifiedDate("2023-01-20 10:10:10");
        return validTicket;
    }

    private int getValidTicketId(){
        return 1;
    }
    private String getValidUserId(){
        return "xchen786";
    }

    private String getCommentJson (Comment comment){
        Gson gson = new Gson();
        String jsonString = gson.toJson(comment);
        return jsonString;
    }

    private Comment getValidComment(){
        Comment validComment = new Comment();
        validComment.setAuthor(getValidUserId()); ;
        validComment.setContent("This is valid comment");
        validComment.setCreatedDate("2023-01-19 10:10:10");
        return validComment;
    }
// ***************************************

    //        Test Case 1  		<error>
    //        Path UserId Validity :  Invalid
    @Test
    public void tsfsTest1() throws Exception {
        /*  Purpose:    Make a request with an invalid userId
         *               Expects a response with status user not found (404)*/
        String invalidUserId = "invalidUserId";
        Comment validComment = getValidComment();
        int ticketId;
        String commentJson = getCommentJson(validComment);
        String userId = getValidUserId();
        Ticket validTicket = getValidTicket();
        String ticketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(userId, ticketJson);
            ticketId = getValidTicketId();
            response.close();

            response = client.createComment(userId, Integer.toString(ticketId), commentJson);
            response.close();

            response = client.getTicketComments(invalidUserId, Integer.toString(ticketId));

            int status = response.getStatusLine().getStatusCode();
            if (status != 404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    //        Test Case 2  		<error>  (follows [if])
    //        Path UserId Access :  Unauthorized
    @Test
    public void tsfsTest2() throws Exception {
        /*  Purpose:    Make a request with an unauthorised userId
         *               Expects a response with status unauthorised user (401)*/
        String invalidUserId = "unauthUserId";
        Comment validComment = getValidComment();
        int ticketId;
        String commentJson = getCommentJson(validComment);
        String userId = getValidUserId();
        Ticket validTicket = getValidTicket();
        String ticketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(userId, ticketJson);
            ticketId = getValidTicketId();
            response.close();

            response = client.createComment(userId, Integer.toString(ticketId), commentJson);
            response.close();

            response = client.getTicketComments(invalidUserId, Integer.toString(ticketId));

            int status = response.getStatusLine().getStatusCode();
            if (status != 401){
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
    public void tsfsTest3() throws Exception {
        /*  Purpose:    Make a request with a ticketId that is ill-formed (not an integer)
         *               Expects a response with status Bad Request (400)*/
        String badticketId = "badticketId";
        Comment validComment = getValidComment();
        int ticketId;
        String commentJson = getCommentJson(validComment);
        String userId = getValidUserId();
        Ticket validTicket = getValidTicket();
        String ticketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(userId, ticketJson);
            ticketId = getValidTicketId();
            response.close();

            response = client.createComment(userId, Integer.toString(ticketId), commentJson);
            response.close();

            response = client.getTicketComments(userId, badticketId);

            int status = response.getStatusLine().getStatusCode();
            if (status != 400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    //        Test Case 4  		<error>  (follows [if])
    //        Path TicketId Validity :  Ticket  Not Present
    @Test
    public void tsfsTest4() throws Exception {
            /*  Purpose:    Make a request with a ticketId that is not actually present
                            Expects a response with status Not Found (404)
             */
        String unknownticketId = "999999999";
        Comment validComment = getValidComment();
        int ticketId;
        String commentJson = getCommentJson(validComment);
        String userId = getValidUserId();
        Ticket validTicket = getValidTicket();
        String ticketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(userId, ticketJson);
            ticketId = getValidTicketId();
            response.close();

            response = client.createComment(userId, Integer.toString(ticketId), commentJson);
            response.close();

            response = client.getTicketComments(userId, unknownticketId);

            int status = response.getStatusLine().getStatusCode();
            if (status != 404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    //        Test Case 5  		(Key = 1.1.1.1.)
    //        Path UserId Validity   :  Valid
    //        Path UserId Access     :  Authorized
    //        Path TicketId Type     :  Integer
    //        Path TicketId Validity :  Ticket Present
    @Test
    public void tsfsTest5() throws Exception {
        /*  Purpose:    Sends a well-formed request with appropriate values.
         *              Expects successful creation of comment (status 200) and
         *              response has Comment JSON that matches the request.   */
        Comment validComment = getValidComment();
        int ticketId;
        String commentJson = getCommentJson(validComment);
        String userId = getValidUserId();
        Ticket validTicket = getValidTicket();
        String ticketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(userId, ticketJson);
            ticketId = getValidTicketId();
            response.close();

            response = client.createComment(userId, Integer.toString(ticketId), commentJson);
            response.close();

            response = client.getTicketComments(userId, Integer.toString(ticketId));

            int status = response.getStatusLine().getStatusCode();
            if (status != 200){
                throw new ClientProtocolException();
            }
            response.close();

            String strResponse = EntityUtils.toString(response.getEntity());

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            EntityUtils.consume(response.getEntity());
        } finally {
            client.close();
        }
    }


}
