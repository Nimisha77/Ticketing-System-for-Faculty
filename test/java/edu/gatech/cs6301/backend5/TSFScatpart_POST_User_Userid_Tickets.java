package edu.gatech.cs6301.backend5;
import java.util.ArrayList;

import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Ticket;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

public class TSFScatpart_POST_User_Userid_Tickets extends TestBase{

//    String baseUrl;
//    CloseableHttpClient httpclient;
//
//    public TSFScatpart_POST_User_Userid_Tickets() {
//        httpclient = TSFSBackendTestSuite.httpclient;
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//    }


    //Purpose: Valid UserID Found
    @Test
    public void tsfsTest1() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        System.out.println("Here1????");
        try {
            String userID = getValidFacultyUsers().get(0);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userID, ticket);
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


    //Purpose: UserID valid but not found
    @Test
    public void tsfsTest2() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userID = getInvalidUser();
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userID, ticket);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


    //Purpose: UserID is empty
    @Test
    public void tsfsTest3() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userID = "";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticekt_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userID, ticket);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ticket Title is empty
    @Test
    public void tsfsTest4() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(2);
            String title = "";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(0));
            watchers.add(getValidFacultyUsers().get(1));
            Ticket ticket = new Ticket(0, title, "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ticket title is one character
    @Test
    public void tsfsTest5() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String title = "a";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, title, "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ticket description is empty - saving it as null?
    @Test
    public void tsfsTest6() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String description = null;
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", description, Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Ticket description is one character
    @Test
    public void tsfsTest7() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String description = "a";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", description, Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Status tag is empty
    @Test
    public void tsfsTest8() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", null,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);

            Assert.assertEquals(400,  response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Status tag is not one of the options
    @Test
    public void tsfsTest9() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
//            String status = "Not an option";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.error,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Category tag is empty
    @Test
    public void tsfsTest10() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            //String status = "Not an option";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                null, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Category tag is not one of the options
    @Test
    public void tsfsTest11() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticekt_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.error, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Email Address is empty
    @Test
    public void tsfsTest12() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, null, getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Email address is invalid
    @Test
    public void tsfsTest13() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String email = "Not an email";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, email, getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: No assignee given
    @Test
    public void tsfsTest14() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String assignee = "";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu",null, watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Invalid assignee given
    @Test
    public void tsfsTest15() throws IOException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            String assignee = getInvalidUser();
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", assignee, watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: Invalid person in watchers list
    @Test
    public void tsfsTest16() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            //String assignee = "Assignee not a valid user";
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getInvalidUser());
            Ticket ticket = new Ticket(0, "ticekt_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    //Purpose: All fields are valid.
    @Test
    public void tsfsTest17() throws IOException, JSONException {
        httpclient = HttpClients.createDefault();
        try {
            String userid = getValidFacultyUsers().get(0);
            ArrayList<String> watchers = new ArrayList<String>();
            watchers.add(getValidFacultyUsers().get(1));
            watchers.add(getValidFacultyUsers().get(2));
            Ticket ticket = new Ticket(0, "ticket_title", "ticket_descr", Ticket.Status.open,
                Ticket.Category.reimbursement, "email1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            //assume valid user ID
            CloseableHttpResponse response = createTicket(userid, ticket);
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    private int getTicketIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        String strResponse = EntityUtils.toString(entity);
        String id = getTicketIdFromStringResponse(strResponse);
        return Integer.valueOf(id);
    }

    private CloseableHttpResponse createTicket(String userid, Ticket ticket) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(ticket);
        StringEntity input = new StringEntity(json);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    private String getTicketIdFromStringResponse(String strResponse) throws JSONException {
        JSONObject object = new JSONObject(strResponse);

        String id = null;
        Iterator<String> keyList = object.keys();
        while (keyList.hasNext()){
            String key = keyList.next();
            if (key.equals("id")) {
                id = object.get(key).toString();
            }
        }
        return id;
    }

}