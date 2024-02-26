package edu.gatech.cs6301.web4;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class PostUserUserIdTicketsComments {
    private String baseUrl = "http://localhost:8080";
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private CloseableHttpClient httpclient;
    private boolean setupdone;

    @Before
    public void runBefore() {
        if (!setupdone) {
            System.out.println("*** SETTING UP TESTS ***");
            // Increase max total connection to 100
            cm.setMaxTotal(100);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(10);
            // Increase max connections for localhost:80 to 50
            HttpHost localhost = new HttpHost("locahost", 8080);
            cm.setMaxPerRoute(new HttpRoute(localhost), 10);
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            setupdone = true;
        }
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        System.out.println("*** ENDING TEST ***");
    }


    // Purpose: PostUserUserIdTicketsComments with an invalid Request body (author is None )
    @Test
    public void tsfsTest1() throws Exception {
        try {
            Date curDate = new Date();
            String ticketid = "001"; 
            CloseableHttpResponse response1 = createTicketComment("jfeng319", ticketid,"{\"author\":\""+""+"\",\"createdDate\":\""+curDate+",\"content\":\"comment1\"}");
            int status = response1.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: PostUserUserIdTicketsComments with an invalid Request body (createdDate is None )
    @Test
    public void tsfsTest2() throws Exception {
        try {
            Date curDate = new Date();
            String ticketid = "002"; 

            CloseableHttpResponse response1 = createTicketComment("jfeng319", ticketid,"{\"author\":\"" + "jfeng319" + "\",\"createdDate\":\"" + "" + ",\"content\":\"comment1\"}");
            int status = response1.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    // Purpose: PostUserUserIdTicketsComments with an invalid Request body (comments is None )
    @Test
    public void tsfsTest3() throws Exception {
        try {
            Date curDate = new Date();
            String ticketid = "003"; 

            CloseableHttpResponse response1 = createTicketComment("jfeng319", ticketid,"{\"author\":\"" + "jfeng319" + "\",\"createdDate\":\""+curDate+",\"content\":\"" + "" + "}");
            int status = response1.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse createTicketComment(String assignee, String ticketid, String payload) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/"+assignee+"/tickets/"+ticketid + "/comments");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity(payload);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private String getIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        return getIdFromStringResponse(strResponse);
    }

    private String getIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()) {
            String key = keyList.next();
            if (key.equals("id")) {
                return object.get(key).toString();
            }
        }
        return null;
    }
}
