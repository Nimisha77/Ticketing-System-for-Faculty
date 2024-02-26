package edu.gatech.cs6301;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;


import java.io.IOException;

public class TSFScatpart_GET_User_Userid_Tickets_Ticketid extends TestBase{
//    private String baseUrl = "http://localhost:8080";
//    private CloseableHttpClient httpclient;
//
//    public TSFScatpart_GET_User_Userid_Tickets_Ticketid() {
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//        httpclient = TSFSBackendTestSuite.httpclient;
//    }

    private CloseableHttpResponse getTicket(String userid, int ticketid) throws IOException {
        httpclient = HttpClients.createDefault();
        HttpGet httpRequest = new HttpGet(baseUrl + "/api/user/" + userid +"/tickets" + ticketid);
        httpRequest.addHeader("accept", "application/json");
        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    @Test
    public void tsfsTest1() throws IOException {
        // Test 1 checks if userid not found  then it should be an Unauthorized user.
        httpclient = HttpClients.createDefault();
        try{
            String userid = "abcabc";
            int ticketid = 123;
            CloseableHttpResponse response = getTicket(userid,ticketid);

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404,status);
            EntityUtils.consume(response.getEntity());
            response.close();


        } catch (IOException  e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

    @Test
    public void tsfsTest2() throws IOException {
        // Test 2 checks if userid length == 0 then it should be a bad request.
        httpclient = HttpClients.createDefault();
        try{
            String userid = "";
            int ticketid = 123;
            CloseableHttpResponse response = getTicket(userid,ticketid);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400,status);
            EntityUtils.consume(response.getEntity());
            response.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }

    }

    @Test
    public void tsfsTest3() throws IOException,NullPointerException {
        // Test 3 checks if ticket id is not found;
        httpclient = HttpClients.createDefault();
        try{
            String userid = "abcabc";
            int ticketid = 123;
            CloseableHttpResponse response = getTicket(userid,ticketid);
            //Let us assume that ticket id is not found.

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404,status);
            EntityUtils.consume(response.getEntity());
            response.close();


        } catch (IOException  e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

    @Test
    public void tsfsTest4() throws IOException,NullPointerException {
        // Test 4 checks if ticketid length == 0 then it should be a bad request.
        httpclient = HttpClients.createDefault();
        try{
            String userid = "abc";
            int ticketid = -1;
            CloseableHttpResponse response = getTicket(userid,ticketid);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400,status);
            EntityUtils.consume(response.getEntity());
            response.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }


    private int getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        int id = Integer.parseInt(strResponse);
        return id;
    }
    @Test
    public void tsfsTest5() throws IOException,NullPointerException {
        // Test 5 checks if both user is and ticket id are found
        httpclient = HttpClients.createDefault();
        try{
            String userid = "abc";
            int ticketid = 123;

            CloseableHttpResponse response = getTicket(userid,ticketid);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(200,status);
            int id = getIdFromResponse(response);
            Assert.assertEquals(ticketid,id);
            EntityUtils.consume(response.getEntity());
            response.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

}


