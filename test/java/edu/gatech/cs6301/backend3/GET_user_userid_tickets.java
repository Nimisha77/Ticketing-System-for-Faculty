package edu.gatech.cs6301.backend3;

import com.google.gson.Gson;
import edu.gatech.cs6301.backend3.models.Category;
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
public class GET_user_userid_tickets {
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
    // ***************************************
    // ***** Test Cases *********************
    //        Test Case 1  		<error>
    //        Path UserId Validity :  UserId Invalid
    @Test
    public void tsfsTest1() throws Exception {
        /*  Purpose:    Make a Request with a userId that doesn't exist
         *  Expects     a reponse with status not found (404)   */
        Ticket validTicket = getValidTicket();
        String invalidUserId = "InvalidUser";
        String validTicketJson = getTicketJson(validTicket);
        String validUserId = getValidUserId();
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.getAllTickets(invalidUserId);

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
    //        Path UserId Access :  UserId Unauthorized
    @Test
    public void tsfsTest2() throws Exception {
        /*  Purpose:    Make a Request with a userId that are not authorized
         *  Expects     a reponse with status unauthorised user (401)   */
        Ticket validTicket = getValidTicket();
        String invalidUserId = "UnauthUser01";
        String validTicketJson = getTicketJson(validTicket);
        String validUserId = getValidUserId();
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.getAllTickets(invalidUserId);

            int status = response.getStatusLine().getStatusCode();
            if (status != 401){
                throw new ClientProtocolException();
            }
            response.close();
        } finally {
            client.close();
        }
    }

    //        Test Case 3  		(Key = 1.1.)
    //        Path UserId Validity :  UserId Valid
    //        Path UserId Access   :  UserId Authorized
    @Test
    public void tsfsTest3() throws Exception {
        /*  Purpose:    Sends a well-formed request with appropriate values.
         *              Expects successful creation of comment (status 200) and
         *              response has Comment JSON that matches the request.   */

        Ticket validTicket = getValidTicket();
        String validUserId = getValidUserId();
        String validTicketJson = getTicketJson(validTicket);
        try{
            CloseableHttpResponse response = client.createTicket(validUserId, validTicketJson);
            response.close();

            response = client.getAllTickets(validUserId);

            int status = response.getStatusLine().getStatusCode();
            if (status != 200){
                throw new ClientProtocolException();
            }
            String strResponse = EntityUtils.toString(response.getEntity());

            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");

            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            client.close();
        }

    }

}

