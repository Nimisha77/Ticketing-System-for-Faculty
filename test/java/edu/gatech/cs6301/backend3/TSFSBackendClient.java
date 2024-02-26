package edu.gatech.cs6301.backend3;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public class TSFSBackendClient {
    private String baseUrl = "http://localhost:8080";
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;


    public void open() {
        if (!setupdone) {
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("localhost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 10);
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            setupdone = true;
        }
    }

    public CloseableHttpResponse createComment(String userId, String ticketId, String commentJson) throws IOException {

        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+userId+"/tickets/"+ticketId+"/comments");
        httpRequest.addHeader("accept", "application/json");

        StringEntity input = new StringEntity(commentJson);

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public CloseableHttpResponse createTicket(String userId, String ticketJson ) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+userId+"/tickets/");
        httpRequest.addHeader("accept", "application/json");

        StringEntity input = new StringEntity(ticketJson);

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public CloseableHttpResponse updateTicket(String userId, String ticketId, String ticketJson) throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/user/"+userId+"/tickets/"+ticketId);
        httpRequest.addHeader("accept", "application/json");

        StringEntity input = new StringEntity(ticketJson);

        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public CloseableHttpResponse getAllTickets(String userId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/"+userId+"/tickets/");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }
    public CloseableHttpResponse getTicket(String userId, String ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/"+userId+"/tickets/"+ticketId);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    public CloseableHttpResponse getTicketComments(String userId, String ticketId) throws IOException {
        HttpGet httpRequest = new HttpGet(baseUrl + "/user/"+userId+"/tickets/"+ticketId+"/comments");

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }






    public void close() throws IOException {
        httpclient.close();
    }
}
