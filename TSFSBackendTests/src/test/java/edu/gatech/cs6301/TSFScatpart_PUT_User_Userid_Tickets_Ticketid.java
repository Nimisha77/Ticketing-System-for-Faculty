package edu.gatech.cs6301;


import com.google.gson.Gson;
import edu.gatech.cs6301.schemas.Ticket;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Iterator;

// It's not reasonable to update the id/title/description of a ticket after being submitted.
// Therefore, these parameters are not considered in the test cases below.
public class TSFScatpart_PUT_User_Userid_Tickets_Ticketid extends  TestBase{
//    CloseableHttpClient httpclient;
//
//    String baseUrl;
//
//    public TSFScatpart_PUT_User_Userid_Tickets_Ticketid() {
//        httpclient = TSFSBackendTestSuite.httpclient;
//        baseUrl = TSFSBackendTestSuite.baseUrl;
//    }

    @Test
    // Purpose: to test that if a user id does not exist then the test will fail
    public void tsfsTest1() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            System.out.println("[TSFScatpart_PUT_User_Userid_Tickets_Ticketid tsfsTest1] test that if a user id does not exist then the test will fail");
            // assume this userid does not exist and therefore is invalid
            String validuserid = getValidFacultyUsers().get(0);
            String invaliduserid = getInvalidUser();
            Integer placeholder = null;

            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };

            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "test1@gatech.edu", getValidStaffUsers().get(0),
                    watchers, null, null);
            System.out.println("[tsfsTest1] ticket info for creation: " + ticket.toString());
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);
            System.out.println("[tsfsTest1] tix after creation: " + tix.toString());

            // update ticket
            tix.setStatus(Ticket.Status.closed);
            System.out.println("[tsfsTest1] tix updated properly: " + tix.toString());
            CloseableHttpResponse response = updateUserTicket(invaliduserid, tix.getId(), tix);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }

    }

    @Test
    // Purpose: to test that if a user id is empty then the test will fail
    public void tsfsTest2() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            System.out.println("[TSFScatpart_PUT_User_Userid_Tickets_Ticketid tsfsTest2] test that if a user id is empty then the test will fail");
            // assume this userid does not exist and therefore is invalid
            String validuserid = getValidFacultyUsers().get(0);
            String userid = "";
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(validuserid, ticket);
            tix.setStatus(Ticket.Status.closed);

            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }

    }

    @Test
    // Purpose: to test that if a ticket id does not exist then the test will fail
    public void tsfsTest3() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();

            Ticket tix = utils.createTicket(userid, ticket);
            tix.setStatus(Ticket.Status.closed);

            // assume that we do not have a ticket with id 100 in the system
            CloseableHttpResponse response = updateUserTicket(userid, getInvalidTicketId(), tix);
            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }

    }


    @Test
    // Purpose: to test that if a ticket id is empty then the test will fail
    public void tsfsTest4() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);
            tix.setStatus(Ticket.Status.closed);

            // the ticket id is null/empty here
            CloseableHttpResponse response = updateUserTicket(userid, placeholder, tix);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if a status tag is empty or invalid then the test will fail
    public void tsfsTest5() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);

            tix.setStatus(null);


            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


    @Test
    // Purpose: to test that if a category tag is empty or invalid then the test will fail
    public void tsfsTest6() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);
            tix.setCategory(null);

            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);
            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if the assignee field is empty then the test will fail
    public void tsfsTest7() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);

            // the assignee field is empty
            tix.setAssignee("");

            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            Assert.assertEquals(400, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if the assignee field is invalid then the test will fail
    public void tsfsTest8() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            String invaliduserid = getInvalidUser();
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);
            tix.setAssignee(invaliduserid);


            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);


            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if the watcher list contains any invalid user  then the test will fail
    public void tsfsTest9() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String invaliduserid = getInvalidUser();
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };
            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);


            // assume the watcher list will contain invalid user
            ArrayList<String> watchers_test = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                    add(invaliduserid);
                }
            };

            tix.setWatchers(watchers_test);

            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            Assert.assertEquals(404, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if the watcher list is empty then the ticket will be updated successfully
    public void tsfsTest10() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid =  getValidFacultyUsers().get(0);

            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };

            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);

            tix.setWatchers(new ArrayList<String>());

            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            int status;
            HttpEntity entity;
            String strResponse;
            status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
//            strResponse = EntityUtils.toString(entity);
//
//            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
//            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }

    @Test
    // Purpose: to test that if the watcher list is valid and not empty then the ticket will be updated successfully
    public void tsfsTest11() throws Exception{
        httpclient = HttpClients.createDefault();
        try {
            // assume this userid is valid and exist in the system
            String userid = getValidFacultyUsers().get(0);
            Integer placeholder = null;
            ArrayList<String> watchers = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                }
            };

            Ticket ticket = new Ticket(placeholder, "title", "ticket test", Ticket.Status.open, Ticket.Category.proposals, "" +
                    "test1@gatech.edu", getValidStaffUsers().get(0), watchers, null, null);
            // assume now we have a valid ticket in the system with valid username
            Utils utils = new Utils();
            Ticket tix = utils.createTicket(userid, ticket);

            ArrayList<String> watchers_update = new ArrayList<String>() {
                {
                    add(getValidFacultyUsers().get(1));
                    add(getValidFacultyUsers().get(2));
                    add(getValidStaffUsers().get(0));
                }
            };
            tix.setWatchers(watchers_update);
            CloseableHttpResponse response = updateUserTicket(userid, tix.getId(), tix);

            int status;
            HttpEntity entity;
            String strResponse;
            status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
//            strResponse = EntityUtils.toString(entity);

//            System.out.println("*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
//            JSONAssert.assertEquals(expectedJson,strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();
        } finally {
            httpclient.close();
        }
    }


    private CloseableHttpResponse updateUserTicket(String userId, Integer ticketid, Ticket ticket) throws IOException {
        HttpPut httpRequest = new HttpPut(baseUrl + "/user/" + userId + "/tickets/" + ticketid);
        httpRequest.addHeader("accept", "application/json");

        // EMERGENCY SETUP - remove timestamps due to conversion issues
        ticket.setCreatedDate(null);
        ticket.setModifiedDate(null);
        Gson gson = new Gson();
        String json = gson.toJson(ticket);
        System.out.println("[updateUserTicket] *** JSON " + json + "***");
        StringEntity input = new StringEntity(json);
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

//    private CloseableHttpResponse createTicket(String userid, Ticket ticket) throws IOException {
//        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userid + "/tickets");
//        httpRequest.addHeader("accept", "application/json");
//        Gson gson = new Gson();
//        String json = gson.toJson(ticket);
//        StringEntity input = new StringEntity(json);
//        input.setContentType("application/json");
//        httpRequest.setEntity(input);
//
//        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
//        CloseableHttpResponse response = httpclient.execute(httpRequest);
//        System.out.println("*** Raw response " + response + "***");
//        return response;
//    }

//    private int getTicketIdFromResponse(CloseableHttpResponse response) throws IOException, JSONException {
//        HttpEntity entity = response.getEntity();
//        String strResponse = EntityUtils.toString(entity);
//        String id = getTicketIdFromStringResponse(strResponse);
//        return Integer.valueOf(id);
//    }

//    private String getTicketIdFromStringResponse(String strResponse) throws JSONException {
//        JSONObject object = new JSONObject(strResponse);
//
//        String id = null;
//        Iterator<String> keyList = object.keys();
//        while (keyList.hasNext()){
//            String key = keyList.next();
//            if (key.equals("id")) {
//                id = object.get(key).toString();
//            }
//        }
//        return id;
//    }


}
