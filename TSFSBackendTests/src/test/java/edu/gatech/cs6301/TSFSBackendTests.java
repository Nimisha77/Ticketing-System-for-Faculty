package edu.gatech.cs6301;

import edu.gatech.cs6301.schemas.User;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;

public class TSFSBackendTests {

//    private String baseUrl = "http://localhost:8080";
//    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//    private CloseableHttpClient httpclient;
//    private boolean setupdone;

    public static void main(String[] args) throws IOException {
        // Your code goes here
        System.out.println("Starting here");



        // Create tickets for nnarayanan33



//        System.out.println("[TSFSBackendTests] baseurl is: " + httpclient);
//        System.out.println("[TSFSBackendTests] httpclient is: " + baseUrl);

        // Call testsuite
//        TSFSBackendTestSuite testSuite = new TSFSBackendTestSuite();
        // Run the test suite using JUnitCore
        Result result = JUnitCore.runClasses(TSFSBackendTestSuite.class);

        // Iterate over the test results and print any failures or successes
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }




    // *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***
    
//    @Test
//    public void startTestSuite() throws Exception {
//
//    }


}

