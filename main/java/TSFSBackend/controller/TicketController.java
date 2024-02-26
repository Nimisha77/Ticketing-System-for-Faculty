package TSFSBackend.controller;

import TSFSBackend.dto.*;
import TSFSBackend.exception.InvalidParameterException;
import TSFSBackend.exception.UnauthorizedRequestException;
import TSFSBackend.model.Comment;
import TSFSBackend.model.LongLivedToken;
import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import TSFSBackend.service.JwtTokenService;
import TSFSBackend.service.JwtUserDetailsService;
import TSFSBackend.service.TicketService;
import TSFSBackend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TicketController {
    private final TicketService ticketService;

    private final UserService userService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtTokenService jwtTokenService;

    private CloseableHttpClient httpclient;
    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/user/{userid}/tickets")
    public ResponseEntity<List<TicketDto>> getTickets(@PathVariable String userid) {
        System.out.println("GET: /user/{userid}/tickets - Get all tickets for a user");
        List<Ticket> tickets = ticketService.getAllRelatedTicketsByUser(userid);
        if(tickets == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tickets
                .stream()
                .map(DtoHelper::toTicketDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @PostMapping("/user/{userid}/tickets")
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketDto ticketRequest, @PathVariable String userid) {
        System.out.println("POST: /user/{userid}/tickets - Creating a ticket for a user");
        Ticket ticket = DtoHelper.ticketFromDto(ticketRequest);

        System.out.println("Ticket: " + ticket.toString());
        System.out.println("ticket.status: " + ticket.getStatus());
        Ticket tx = ticketService.createTicket(userid, ticket);
        return new ResponseEntity<>(DtoHelper.toTicketDto(tx), HttpStatus.CREATED);
    }

    // User Story 4
    // TODO can the author name be anything???
    @PostMapping("/user/{userid}/tickets/{ticketid}/comments")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto comment, @PathVariable String userid , @PathVariable int ticketid) {
        String author = comment.getAuthor();
        String content = comment.getContent();
        if (!ticketService.isRelatedUser(userid, ticketid)) {
            System.out.println("[createComment] " + userid + " is not related to ticket " + ticketid);
            throw new UnauthorizedRequestException();
        } else {
            if (author == null || content == null || author.isBlank() || content.isBlank()) {
                throw new InvalidParameterException();
            } else {
                Comment createdComment = ticketService.createComment(ticketid, author, content);
                return new ResponseEntity<>(DtoHelper.toCommentDto(createdComment), HttpStatus.OK);
            }
        }
    }

    @GetMapping("/user/{userid}/tickets/{ticketid}/comments")
    public List<CommentDto> getComments(@PathVariable String userid, @PathVariable int ticketid) {
        if (ticketService.isRelatedUser(userid, ticketid)) {
            return ticketService.getCommentsByTicket(ticketid)
                    .stream()
                    .map(DtoHelper::toCommentDto)
                    .collect(Collectors.toList());
        } else {
            throw new UnauthorizedRequestException();
        }
    }

    @GetMapping("/user/{userid}/tickets/{ticketid}")
    public TicketDto getTicketByTicketId(@PathVariable String userid, @PathVariable int ticketid) {
        if (ticketService.isRelatedUser(userid, ticketid)) {
            return DtoHelper.toTicketDto(ticketService.getTicketByTicketid(ticketid));
        } else {
            throw new UnauthorizedRequestException();
        }
    }

    @PutMapping("/user/{userid}/tickets/{ticketid}")
    public TicketDto updateTicketStatus(@PathVariable String userid, @PathVariable Integer ticketid,
                                        @RequestBody TicketDto ticket) {
        System.out.println("PUT: /user/{userid}/tickets/{ticketid} - Updating a ticket's status");
        System.out.println("[PUT] userid: " + userid + " ticketid: " + ticketid + " ticket: " + ticket.toString());
        Ticket ticketobj = DtoHelper.ticketFromDto(ticket);
        //ticketService.checkTicketDetailsDuringCreation(ticketobj);
        if (ticketService.isRelatedUser(userid, ticketid)) {
            Ticket.Status status = ticket.getStatus();
            return DtoHelper.toTicketDto(ticketService.updateTicketStatus(ticketid, status));
        } else {
            throw new UnauthorizedRequestException();
        }

    }
    @PostMapping("/test/adduser")
    public void addUser(@RequestBody UserDto user){
        System.out.println("Adding user to db");
        userService.addUser(user);
    }

    @GetMapping("/test/getAllUsers")
    public void getAllUsers(){
        System.out.println("Getting all users");
//        userService.getAllUsers();
    }
  
    @GetMapping("/auth-tokengen")
    public String authenticate() {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getLogin(), authenticationRequest.getPassword()));
//        } catch (final BadCredentialsException ex) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("mej013");

        return jwtTokenService.generateToken(userDetails);

    }


    @GetMapping("/auth")
    public ResponseEntity<String> verifyToken(@RequestHeader(name = "Authorization") String token) throws IOException {

        // Write a try catch block
        try {
            this.httpclient = HttpClients.createDefault();
            HttpGet httpRequest = new HttpGet("https://sso-test.gatech.edu/cas/oidc/oidcProfile");
            httpRequest.addHeader("Authorization", token);
            System.out.println("*** Executing request " + httpRequest.getRequestLine() + "***");
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            System.out.println("*** Raw response [sso oidcProfile]" + response + "***");


            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity;

            if (status == 200) {
                entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNodeRoot = objectMapper.readTree(responseBody);
                String userid = jsonNodeRoot.get("id").asText();
                if(userService.isValidUser(userid) == null){

                    System.out.println("User needs to be created: " + userid);
                    String email_id_user, name_user;
                    try {
                        email_id_user = jsonNodeRoot.get("attributes").get("email_verified").asText();
                        name_user = jsonNodeRoot.get("attributes").get("given_name").asText();
                        System.out.println("[UserCreation] email: " + email_id_user + " name: " + name_user + " userid: " + userid);
                    } catch (Exception e) {
//                        return new ResponseEntity<String>("Unable to fetch information of user from gatech sso",HttpStatus.BAD_REQUEST);
                        email_id_user = "dummyemail";
                        name_user = "dummyname";
                        System.out.println("[UserCreationDummyDataAdded] email: " + email_id_user + " name: " + name_user + " userid: " + userid);
                    }

                    userService.addUser(new UserDto(userid, name_user, email_id_user, User.Affiliation.faculty ));
                }

                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userid);
                return new ResponseEntity<String>(jwtTokenService.generateToken(userDetails), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }
}
