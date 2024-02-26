package edu.gatech.cs6301.backend3;

import com.google.gson.Gson;
import edu.gatech.cs6301.backend3.models.Comment;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

@FixMethodOrder(MethodSorters.DEFAULT)
//@DisplayName("Test POST Mapping of /user/{userid}/tickets/{ticketid}/comments")
public class POST_user_userid_tickets_ticketid_comments {

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

    //        Test Case 1  		<error>
//        Path UserId Validity :  UserId Invalid
    //@Order(1)
    @Test
//    @DisplayName("Test Case 1: Invalid User Id")
    public void tsfsTest1 () throws IOException {
            /*purpose:  Make a Request with a userId that doesn't exist
                        Expects a reponse with status not found (404)*/
        Comment validComment = getValidComment();
        int validTicketId = getValidTicketId();
        String invalidUserId = "InvalidUser";
        String validCommentJson = getCommentJson(validComment);
        try{
            CloseableHttpResponse response = client.createComment(invalidUserId,Integer.toString(validTicketId) ,validCommentJson);
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
    //@Order(2)
    @Test
    //@DisplayName("Test Case 2: Unauthorized User Id")
    public void tsfsTest2() throws IOException {
        /*  Purpose:    Make a request with an unauthorised userId
         *               Expects a response with status unauthorised user (401)*/
        Comment validComment = getValidComment();
        int validTicketId = getValidTicketId();
        String invalidUserId = "UnauthUser01";
        String validCommentJson = getCommentJson(validComment);
        try{
            CloseableHttpResponse response = client.createComment(invalidUserId,Integer.toString(validTicketId) ,validCommentJson);
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
    //@DisplayName("Test Case 3: Invalid Ticket Id")
    //@Order(3)
    public void tsfsTest3() throws IOException {
        /*  Purpose:    Make a request with a ticketId that is ill-formed (not an integer)
         *               Expects a response with status Bad Request (400)*/
        Comment validComment = getValidComment();
        int validTicketId = getValidTicketId();
        String invalidTicketId = Integer.toString(validTicketId) + "garbage";
        String validUserId = getValidUserId();
        String validCommentJson = getCommentJson(validComment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, invalidTicketId ,validCommentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
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
    //@DisplayName("Test Case 4: Ticket Not Present")
    //@Order(4)
    public void tsfsTest4() throws IOException {
            /*  Purpose:    Make a request with a ticketId that is not actually present
                            Expects a response with status Not Found (404)
             */
        Comment validComment = getValidComment();
        int unknownTicketId = 300; // ticket shouldn't exist
        String validUserId = getValidUserId();
        String validCommentJson = getCommentJson(validComment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(unknownTicketId) ,validCommentJson);
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
//        Request Body Type :  Does Not Match Schema Comment
    @Test
    //@DisplayName("Test Case 5: Request Body is ill formed")
    //@Order(5)
    public void tsfsTest5() throws IOException {
            /*  Purpose:    Send a request with an ill formed body that doesnt match Comment schema.
                            Expects a reponse with status Bad Request (400)
             */
        int validTicketId = getValidTicketId();
        String validUserId = getValidUserId();
        String invalidCommentJson = "{ \"randomField1\": \"randomvalue1\"," +
                "  \"randomField2\": \"randomvalue2\"}";
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,invalidCommentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    //        Test Case 6  		<error>
//        Comment Field AuthorPresent :  Field Left Empty
    @Test
    //@DisplayName("Test Case 6: Comment has empty author string")
    //@Order(6)
    public void tsfsTest6() throws IOException {
            /* Purpose:     Sends a comment that has no author field filled
                            Expects response with status Bad Request (404)
             */
        Comment comment = getValidComment();
        comment.setAuthor("");

        int validTicketId = getValidTicketId(); // ticket shouldn't exist
        String validUserId = getValidUserId();
        String commentJson = getCommentJson(comment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,commentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=404){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

    //        Test Case 7  		<error>  (follows [if])
//        Comment Field Author Value :  Doesn't match Path UserId
    @Test
    //@DisplayName("Test Case 7: The author of the comment is unauthorised")
    //@Order(7)
    public void tsfsTest7() throws IOException {
            /* Purpose :    Sends a comment that has an unauthorised author
                            Expects a response with status unauthorised user (401)
             */
        Comment comment = getValidComment();
        comment.setAuthor("InvalidUser");

        int validTicketId = getValidTicketId(); // ticket shouldn't exist
        String validUserId = getValidUserId();
        String commentJson = getCommentJson(comment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,commentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=401){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

    //        Test Case 8  		<error>
//        Comment Field Created Date Format :  Invalid Date
    @Test
    //@DisplayName("Test Case 8: Comment has a an invalid date")
    //@Order(8)
    public void tsfsTest8() throws IOException {
            /* Purpose :    Sends a comment that has an ill formated date string
                            Expects a response that has status Bad request (400)
             */
        Comment comment = getValidComment();
        comment.setCreatedDate("this is not a date string");

        int validTicketId = getValidTicketId(); // ticket shouldn't exist
        String validUserId = getValidUserId();
        String commentJson = getCommentJson(comment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,commentJson);
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
//        Comment Field content Length :  Empty
    @Test
    //@DisplayName("Test Case 9: Comment has no content")
    //@Order(9)
    public void tsfsTest9() throws IOException {
            /* Purpose:     Sends a comment that has no content
                            Expects a response that has status Bad Request (400)
             */
        Comment comment = getValidComment();
        comment.setContent("");

        int validTicketId = getValidTicketId(); // ticket shouldn't exist
        String validUserId = getValidUserId();
        String commentJson = getCommentJson(comment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,commentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=400){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }


    //        Test Case 10 		(Key = 1.1.1.1.1.1.1.1.2.)
//        Path UserId Validity              :  UserId Valid
//        Path UserId Access                :  UserId Authorized
//        Path TicketId Type                :  Integer
//        Path TicketId Validity            :  Ticket Present
//        Request Body Type                 :  Matches Schema Comment
//        Comment Field AuthorPresent       :  Field Present
//        Comment Field Author Value        :  Matches Path UserId
//        Comment Field Created Date Format :  Valid Date
//        Comment Field content Length      :  Not Empty
    @Test
    //@DisplayName("Test Case 10: Happy Path")
    //@Order(10)
    public void tsfsTest10() throws IOException, JSONException {
            /* Purpose :    Sends a well-formed request with appropriate values.
                            Expects successful creation of comment (status 200) and
                            response has Comment JSON that matches the request.
             */
        Comment comment = getValidComment();
        int validTicketId = getValidTicketId(); // ticket shouldn't exist
        String validUserId = getValidUserId();
        String commentJson = getCommentJson(comment);
        try{
            CloseableHttpResponse response = client.createComment(validUserId, Integer.toString(validTicketId) ,commentJson);
            int status = response.getStatusLine().getStatusCode();
            if (status!=200){
                throw new ClientProtocolException();
            }
            HttpEntity entity = response.getEntity();
            String responseCommentJson = EntityUtils.toString(entity);
            JSONAssert.assertEquals(commentJson,responseCommentJson,false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            client.close();
        }
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
    private int getValidTicketId(){
        return 1;
    }
    private String getValidUserId(){
        return "validUser";
    }
}
