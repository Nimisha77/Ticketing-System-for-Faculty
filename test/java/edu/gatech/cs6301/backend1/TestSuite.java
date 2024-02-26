package edu.gatech.cs6301.backend1;

//import edu.gatech.cs6301.devops345.GET_user_userid_tickets;
//import edu.gatech.cs6301.devops345.GET_user_userid_tickets_ticketid;
//import edu.gatech.cs6301.devops345.GET_user_userid_tickets_ticketid_comments;
//import edu.gatech.cs6301.devops345.POST_user_userid_tickets;
//import edu.gatech.cs6301.devops345.POST_user_userid_tickets_ticketid_comments;
//import edu.gatech.cs6301.devops345.PUT_user_userid_tickets_ticketid;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        GET_user_userid_tickets_ticketid.class,
        GET_user_userid_tickets.class,
        GET_user_userid_tickets_ticketid_comments.class,
        POST_user_userid_tickets.class,
        POST_user_userid_tickets_ticketid_comments.class,
        PUT_user_userid_tickets_ticketid.class
})

public class TestSuite {
}