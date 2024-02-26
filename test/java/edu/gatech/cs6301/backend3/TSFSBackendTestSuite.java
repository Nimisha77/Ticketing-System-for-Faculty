package edu.gatech.cs6301.backend3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        POST_user_userid_tickets_ticketid_comments.class,
        POST_user_userid_tickets.class,
        PUT_user_userid_tickets_ticketid.class,
        GET_user_userid_tickets.class,
        GET_user_userid_tickets_ticketid.class,
        GET_user_userid_tickets_ticketid_comments.class,
        /* Added your classes to this list*/

})
public class TSFSBackendTestSuite {

}
