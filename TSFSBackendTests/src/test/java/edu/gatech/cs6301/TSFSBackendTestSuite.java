package edu.gatech.cs6301;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.After;
import org.junit.Before;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TSFScatpart_GET_User_Userid_Tickets.class,
//        TSFScatpart_POST_User_Userid_Tickets_Ticketid_Comments.class,
//        TSFScatpart_GET_User_Userid_Tickets_Ticketid.class,
//        TSFScatpart_POST_User_Userid_Tickets.class,
//        TSFScatpart_PUT_User_Userid_Tickets_Ticketid.class,
//        TSFScatpart_GET_User_Userid_Tickets_Ticketid_Comments.class
})
public class TSFSBackendTestSuite {

//    public static String baseUrl;
//    public static CloseableHttpClient httpClient;

//    public TSFSBackendTestSuite(String baseUrl, CloseableHttpClient httpClient ) {

//        System.out.println("Constructor RunAlltests ");
//        TSFSBackendTestSuite.baseUrl = baseUrl;
//        TSFSBackendTestSuite.httpClient = httpClient;

//        System.out.println("[RunAllTests] baseurl is: " + TSFSBackendTestSuite.baseUrl);
//        System.out.println("[RunAllTests] httpclient is: " + TSFSBackendTestSuite.httpClient);
//    }

//    TSFScatpart_GET_User_Userid_Tickets_Ticketid_Comments.class
//    TSFScatpart_GET_User_Userid_Tickets_Ticketid.class
//    TSFScatpart_POST_User_Userid_Tickets.class
//    TSFScatpart_PUT_User_Userid_Tickets_Ticketid.class    - Meixuan

}
