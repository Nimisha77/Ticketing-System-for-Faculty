package edu.gatech.cs6301.backend5;

import edu.gatech.cs6301.schemas.User;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;

public class TestBase {

    public  String baseUrl = "http://localhost:8080";
    public PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    public  CloseableHttpClient httpclient;
    public boolean setupdone;

    private ArrayList<String> validFacultyUsers;
    private ArrayList<String> validStaffUsers;

    @Before
    public void runBefore() throws IOException {
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
        createUsers();
        System.out.println("*** STARTING TEST ***");
    }

    @After
    public void runAfter() {
        initDB();
        System.out.println("*** ENDING TEST ***");
    }

    public void initDB(){
        validFacultyUsers = new ArrayList<>();
        validStaffUsers = new ArrayList<>();
    }


    public void createUsers() throws IOException {
        // Create users
        initDB();
        Utils utils = new Utils();
        utils.createUser(new User("nnarayanan33", "Naveen", "nnarayanan33@gatech.edu", User.Affiliation.faculty));
        validFacultyUsers.add("nnarayanan33");
        utils.createUser(new User("mjiang85", "Meixuan", "mjiang85@gatech.edu", User.Affiliation.faculty));
        validFacultyUsers.add("mjiang85");
        utils.createUser(new User("mej013", "Meixuan", "mej013@ucsd.edu", User.Affiliation.faculty));
        validFacultyUsers.add("mej013");
        utils.createUser(new User("ayp6", "Ananya", "ayp6@gatech.edu", User.Affiliation.staff));
        validStaffUsers.add("ayp6");
        utils.createUser(new User("nimisha123", "Nimisha", "nimisha123@gatech.edu", User.Affiliation.staff));
        validStaffUsers.add("nimisha123");
        System.out.println("Created all users");
    }

    public String getInvalidUser(){
        return "xxxxx";
    }

    public int getInvalidTicketId(){
        return 10101;
    }
    public ArrayList<String> getValidFacultyUsers(){
        return new ArrayList<>(validFacultyUsers);
    }

    public ArrayList<String> getValidStaffUsers(){
        return new ArrayList<>(validStaffUsers);
    }

//    public void createDummyTicketGivenUser(String validuserid, Ticket ticket) {
//        Utils utils = new Utils();
//        utils.createTicket()
//    }




}
