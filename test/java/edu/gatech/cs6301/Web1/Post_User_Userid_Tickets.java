package edu.gatech.cs6301.Web1;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.skyscreamer.jsonassert.JSONAssert;

public class Post_User_Userid_Tickets {

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

    // *** YOU SHOULD NOT NEED TO CHANGE ANYTHING ABOVE THIS LINE ***

    // invalidTicketIdTest
    @Test
    public void tsfsTest1() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", -1, "Professor Orso",
                    "unable to hire a student assistant", "open", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(404, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // EmptyTitleTest
    @Test
    public void tsfsTest2() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "", "unable to hire a student assistant",
                    "open", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // EmptyDescriptionTest
    @Test
    public void tsfsTest3() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "", "open",
                    "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // invalidStatusTest
    @Test
    public void tsfsTest4() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // Travel Authorization categoryTestOne
    @Test
    public void tsfsTest5() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to authorize a travel", "open", "travel authorization", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "unable to authorize a travel" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "travel authorization" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // ReimbursementcategoryTestTwo
    @Test
    public void tsfsTest6() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to reimburse an expense", "open", "reimbursement", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "unable to reimburse an expense" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "reimbursement" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Meeting OrganizationcategoryTestThree
    @Test
    public void tsfsTest7() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "need help with meeting organization", "open", "meeting organization", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "need help with meeting organization" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "meeting organization" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Student Hiring categoryTestFour
    @Test
    public void tsfsTest8() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "unable to hire a student",
                    "open", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "unable to hire a student" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "student hiring" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // Proposals categoryTestFive
    @Test
    public void tsfsTest9() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "need help with proposals",
                    "open", "proposals", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "need help with proposals" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "proposals" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // invalidCategoryTest
    @Test
    public void tsfsTest10() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "open", "heelo", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // invalidEmailTest
    @Test
    public void tsfsTest11() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "open", "student hiring", "thisIsNotAnEmail",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // invalidAssigneeTest
    @Test
    public void tsfsTest12() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "open", "student hiring", "orso@gatech.edu",
                    "thisIsNotAGTUsername", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // invalidCreateDateTest
    @Test
    public void tsfsTest13() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "open", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "thisIsNotADate",
                    "2023-02-27T15:30:28.715Z");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // invalidModifiedDateTest
    @Test
    public void tsfsTest14() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso",
                    "unable to hire a student assistant", "open", "student hiring", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "thisIsNotADate");

            int status = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, status);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // valid test: open status, Miscellaneous category, zero length watcher
    @Test
    public void tsfsTest15() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "open", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] {}, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // valid test: open status, Miscellaneous category, more than one length watcher
    @Test
    public void tsfsTest16() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "open", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "open" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // valid test: closed status, Miscellaneous category, zero length watcher
    @Test
    public void tsfsTest17() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "closed", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] {}, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "closed" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // valid test: closed status, Miscellaneous category, more than one length
    // watcher
    @Test
    public void tsfsTest18() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "closed", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "closed" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // valid test: needs-attention status, Miscellaneous category, zero length
    // watcher
    @Test
    public void tsfsTest19() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "needs-attention", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] {}, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "needs-attention" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    // valid test: needs-attention status, Miscellaneous category, more than one
    // length
    @Test
    public void tsfsTest20() throws Exception {
        try {
            CloseableHttpResponse response = createTicket("900000000", 1, "Professor Orso", "some description",
                    "needs-attention", "miscellaneous", "orso@gatech.edu",
                    "jlin123", new String[] { "Professor Orso", "John Doe" }, "2023-02-27T15:30:28.715Z",
                    "2023-02-27T15:30:28.715Z");
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;
            String strResponse;
            if (status == 201) {
                entity = response.getEntity();
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            strResponse = EntityUtils.toString(entity);
            String[] arr = new String[] { "Professor Orso", "John Doe" };
            System.out.println(
                    "*** String response " + strResponse + " (" + response.getStatusLine().getStatusCode() + ") ***");
            String expectedJson = "{\"id\":\"" + "1" + "\"," +
                    "{\"title\":\"" + "Professor Orso" + "\"," +
                    "{\"description\":\"" + "some description" + "\"," +
                    "{\"status\":\"" + "needs-attention" + "\"," +
                    "{\"category\":\"" + "miscellaneous" + "\"," +
                    "{\"emailAddress\":\"" + "orso@gatech.edu" + "\"," +
                    "{\"assignee\":\"" + "jlin123" + "\"," +
                    "{\"watchers\":\"" + "[\"Professor Orso\", \"John Doe\"]" + "\"," +
                    "{\"createdDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"," +
                    "{\"modifiedDate\":\"" + "2023-02-27T15:30:28.715Z" + "\"}";

            JSONAssert.assertEquals(expectedJson, strResponse, false);
            EntityUtils.consume(response.getEntity());
            response.close();

        } finally {
            httpclient.close();
        }
    }

    private CloseableHttpResponse createTicket(String userId, int ticketId, String title, String description,
            String status,
            String category, String emailAddress, String assignee, String[] watchers, String createdDate,
            String modifiedDate) throws IOException {
        HttpPost httpRequest = new HttpPost(baseUrl + "/user/" + userId + "/tickets");
        httpRequest.addHeader("accept", "application/json");
        StringEntity input = new StringEntity("{\"userId\":\"" + userId + "\"," +
                "\"ticketId\":\"" + ticketId + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"description\":\"" + description +
                "\"status\":\"" + status + "\"," +
                "\"category\":\"" + category + "\"," +
                "\"emailAddress\":\"" + emailAddress + "\"," +
                "\"assignee\":\"" + assignee + "\"," +
                "\"watchers\":\"" + watchers + "\"," +
                "\"createdDate\":\"" + createdDate + "\"," +
                "\"modifiedDate\":\"" + modifiedDate + "\"}");
        input.setContentType("application/json");
        httpRequest.setEntity(input);

        System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
        CloseableHttpResponse response = httpclient.execute(httpRequest);
        System.out.println("*** Raw response " + response + "***");
        return response;
    }

    // private String getIdFromResponse(CloseableHttpResponse response) throws
    // IOException, JSONException {
    // HttpEntity entity = response.getEntity();
    // String strResponse = EntityUtils.toString(entity);
    // String id = getIdFromStringResponse(strResponse);
    // return id;
    // }

    // private String getIdFromStringResponse(String strResponse) throws
    // JSONException {
    // JSONObject object = new JSONObject(strResponse);

    // String id = null;
    // Iterator<String> keyList = object.keys();
    // while (keyList.hasNext()) {
    // String key = keyList.next();
    // if (key.equals("id")) {
    // id = object.get(key).toString();
    // }
    // }
    // return id;
    // }

}
