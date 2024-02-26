package edu.gatech.cs6301.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gatech.cs6301.dto.Category;
import edu.gatech.cs6301.dto.Comment;
import edu.gatech.cs6301.dto.Status;
import edu.gatech.cs6301.dto.Ticket;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class TicketSystemTestUtils {

    private static String baseUrl = "http://localhost:8080";
    private static ObjectMapper mapper = new ObjectMapper();

    public static Ticket getValidTicket() {
        ArrayList<String> watchers = new ArrayList<>();
        watchers.add("watcher1");
        watchers.add("watcher2");
        Date createdDate = new Date();
        // Modified Date == Created Date, when ticket is created
        Ticket ticket = new Ticket("Ticket title", "Ticket Description", Status.open, Category.travel_authorization,
                "testUser@test.com",
                "assigneeUser", watchers, createdDate, createdDate);
        return ticket;
    }

    public static CloseableHttpResponse createComment(String userId, int ticketId, Comment comment,
            CloseableHttpClient httpclient) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "/tickets/" + ticketId + "/comments");
        httpRequest.addHeader("accept", "application/json");
        // StringEntity input = new StringEntity("{\"author\":\"" + author + "\"," +
        // "\"createdDate\":\"" + createdDate + "\"," +
        // "\"comment\":\"" + comment + "\"}");
        StringEntity input = new StringEntity(mapper.writeValueAsString(comment));
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse createTicket(String userId, Ticket ticket, CloseableHttpClient httpclient)
            throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        // StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
        // "\"description\":\"" + description + "\"," +
        // "\"status\":\"" + status + "\"," +
        // "\"category\":\"" + category + "\"," +
        // "\"assignee\":\"" + assignee + "\"," +
        // "\"watchers\":\"" + watchers + "\"," +
        // "\"createdDate\":\"" + createdDate + "\"," +
        // "\"modifiedDate\":\"" + modifiedDate + "\"," +
        // "\"email\":\"" + email + "\"}");
        StringEntity input = new StringEntity(mapper.writeValueAsString(ticket));
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse updateTicket(String userId, int ticketId, StringEntity input, CloseableHttpClient httpclient) throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/user/" + userId + "/tickets/" + ticketId);
        httpRequest.addHeader("accept", "application/json");
//        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
//                "\"description\":\"" + description + "\"," +
//                "\"status\":\"" + status + "\"," +
//                "\"category\":\"" + category + "\"," +
//                "\"assignee\":\"" + assignee + "\"," +
//                "\"watchers\":\"" + watchers + "\"," +
//                "\"createdDate\":\"" + createdDate + "\"," +
//                "\"modifiedDate\":\"" + modifiedDate + "\"," +
//                "\"email\":\"" + email + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static Integer getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id = getIdFromStringResponse(strResponse);
        return Integer.valueOf(id);
    }

    private static String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()) {
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.get(key).toString();
            }
        }
        return id;
    }

}
