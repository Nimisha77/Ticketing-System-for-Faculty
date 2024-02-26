package edu.gatech.cs6301.backend5;

import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Comment;
import edu.gatech.cs6301.schemas.Ticket;
import edu.gatech.cs6301.schemas.User;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.io.IOException;

public class Utils extends TestBase {

    public Ticket createTicket(String userid, Ticket ticket) throws IOException {

        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
            httpRequest.addHeader("accept", "application/json");
            Gson gson = new Gson();
            String json = gson.toJson(ticket);
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            httpRequest.setEntity(input);

            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);

            String strResponse;
            strResponse = EntityUtils.toString(response.getEntity());
            System.out.println("[utils] String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            Ticket createdTicket = gson.fromJson(strResponse, Ticket.class);
            System.out.println("[utils] createdTicket: " + createdTicket.toString());
            EntityUtils.consume(response.getEntity());
            response.close();
            return createdTicket;
        } finally {
            httpclient.close();
        }

    }


    public User createUser(User user) throws IOException {
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpRequest = new HttpPost(baseUrl + "/test/adduser");
            httpRequest.addHeader("accept", "application/json");
            Gson gson = new Gson();
            String json = gson.toJson(user);
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            httpRequest.setEntity(input);
            System.out.println("*** [createUser] Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);

//        System.out.println("[utils] String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            System.out.println("[utils] added user: " + user.toString());
            EntityUtils.consume(response.getEntity());
            response.close();
            return user;
        } finally {
            httpclient.close();
        }

    }

    public Comment createComment(String userid, int ticketid, Comment comment) throws IOException {
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets/" + ticketid + "/comments");
            httpRequest.addHeader("accept", "application/json");
            Gson gson = new Gson();
            String json = gson.toJson(comment);

            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            httpRequest.setEntity(input);

            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
//        System.out.println("*** Raw response " + response + "***");
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            String strResponse;
            strResponse = EntityUtils.toString(response.getEntity());
            System.out.println("[utils createComments] String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            Comment createdComment = gson.fromJson(strResponse, Comment.class);
            System.out.println("[utils createComments] createdComment: " + createdComment.toString());
            EntityUtils.consume(response.getEntity());
            response.close();
            return createdComment;
        } finally {
            httpclient.close();
        }
    }

//    public User fetchTicket(String userid, int ticketid) {
//
//    }

    public String generateRandomString() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}
