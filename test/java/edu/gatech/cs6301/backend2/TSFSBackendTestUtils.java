package edu.gatech.cs6301.backend2;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.Arrays;

public class TSFSBackendTestUtils {

    public static final String EXISTING_USER = "gburdell1";
    public static final String NONEXISTING_USER = "fakeuser1";
    public static final String EXISTING_TICKET_ID = "100";
    public static final String NONEXISTING_TICKET_ID = "0";
    public static final String INVALID_TICKET_ID = "ticket1";
    public static final String VALID_TITLE = "Request for Travel to California";
    public static final String VALID_DESCRIPTION = "I am going to California for a CS conference.";
    public static final String VALID_STATUS = "Open";
    public static final String INVALID_STATUS = "Idk";
    public static final String VALID_CATEGORY = "Travel Reimbursement";
    public static final String INVALID_CATEGORY = "Whatever category I want";
    public static final String VALID_EMAIL = "gburdell1@gatech.edu";
    public static final String INVALID_EMAIL = "notrealmail.com";
    public static final String VALID_ASSIGNEE = "assignee1";
    public static final String[] ZERO_WATCHERS = {};
    public static final String[] ONE_WATCHER = {"watcher1"};
    public static final String[] MULTIPLE_WATCHERS = {"watcher1", "watcher2"};
    public static final String VALID_COMMENT_AUTHOR = "Michael";
    public static final String VALID_COMMENT_CONTENT = "This is a valid comment.";

    public static CloseableHttpResponse getTickets(
            HttpClient httpclient,
            String baseUrl,
            String userId
    ) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse createTicket(
            HttpClient httpclient,
            String baseUrl,
            String userId,
            String title,
            String description,
            String status,
            String category,
            String emailAddress,
            String assignee,
            String[] watchers
    ) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + Arrays.toString(watchers) + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse getTicket(
            HttpClient httpclient,
            String baseUrl,
            String userId,
            String ticketId
    ) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId);
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse updateTicket(
            HttpClient httpclient,
            String baseUrl,
            String userId,
            String ticketId,
            String title,
            String description,
            String status,
            String category,
            String emailAddress,
            String assignee,
            String[] watchers
    ) throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/user/" + userId + "/tickets/" + ticketId);
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + Arrays.toString(watchers) + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse getComments(
            HttpClient httpclient,
            String baseUrl,
            String userId,
            String ticketId
    ) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/" + userId + "/tickets/" + ticketId + "/comments");
        httpRequest.addHeader("accept", "application/json");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public static CloseableHttpResponse createComment(
            HttpClient httpclient,
            String baseUrl,
            String userId,
            String ticketId,
            String author,
            String content
    ) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "/tickets/" + ticketId + "/comments");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"author\":\"" + author + "\",\"content\":\"" + content + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
}
