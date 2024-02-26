package TSFSBackend.service;

import TSFSBackend.dto.DtoHelper;
import TSFSBackend.dto.TicketDto;
import TSFSBackend.exception.*;
import TSFSBackend.model.Comment;
import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import TSFSBackend.repository.TicketRepository;
import TSFSBackend.repository.UserRepository;
import TSFSBackend.utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;



    public void checkTicketDetailsDuringCreation(Ticket ticketRequest) {

        if(ticketRequest.getTitle().equals("") || ticketRequest.getTitle() == null
        || ticketRequest.getAssignee() == null || ticketRequest.getAssignee().equals("") || (!EmailValidator.isValidEmail(ticketRequest.getEmailAddress()))
        || ticketRequest.getCategory() == null || ticketRequest.getDescription() == null ){
            System.out.println("Is this happening brudda????");
            throw new InvalidTicketException("Invalid Ticket information in the request! Pls check");
        }

        if(!userService.isStaff(ticketRequest.getAssignee())) {
            throw new NotStaffException(ticketRequest.getAssignee());
        }
        ticketRequest.getWatchers()
                .stream()
                .forEach(watcher -> {
                    if(!userRepository.existsById(watcher)) {
                        throw new UserNotFoundException(watcher);
                    }
                });
    }
    public Ticket createTicket(String GT_username, Ticket ticketRequest) {
        checkTicketDetailsDuringCreation(ticketRequest);
        if (GT_username.length() == 0) {
            throw new InvalidParameterException();
        }
        if(!userService.isFaculty(GT_username)) {
            throw new NotFacultyException(GT_username);
        }
        if(!userService.isStaff(ticketRequest.getAssignee())) {
            throw new NotStaffException(ticketRequest.getAssignee());
        }
        ticketRequest.getWatchers()
                .stream()
                .forEach(watcher -> {
                    if(!userRepository.existsById(watcher)) {
                        throw new UserNotFoundException(watcher);
                    }
                });

        User owner = userRepository.findUserByGtUsername(GT_username);
        owner.getOwnerTicket().add(ticketRequest);
        ticketRequest.setOwner(owner);
        ticketRequest.setStatus(Ticket.Status.open);
        Ticket toReturn = ticketRepository.save(ticketRequest);
        userRepository.save(owner);

        userService.addTicket(GT_username, ticketRequest);
        userService.addTicket(ticketRequest.getAssignee(), ticketRequest);
        ticketRequest.getWatchers().stream().forEach(watcher -> userService.addTicket(watcher, ticketRequest));
        return toReturn;
    }

    public boolean isRelatedUser(String userid, int ticketid) {
        if (String.valueOf(ticketid).length() == 0) {
            System.out.println("hi");
            throw new InvalidParameterException();
        }
        Ticket ticket = ticketRepository.findById(ticketid).orElse(null);
        User user = userRepository.findById(userid).orElse(null);
        System.out.println("[isRelatedUser] ticket: " + ticket + " user: " + user);
        if (user == null) {
            throw new UserNotFoundException(userid);
        } else if (ticket == null) {
            throw new TicketNotFoundException(ticketid);

        } else {
            return ticket.getRelatedUsers().contains(user);
        }
    }


    public Ticket getTicketByTicketid(int ticketid) {
        return ticketRepository.findTicketById(ticketid);
    }

    public List<Ticket> getAllRelatedTicketsByUser(String GT_username) {
        User user = userRepository.findUserByGtUsername(GT_username);
        if(user == null){
            return null;
        }
        return ticketRepository.findTicketsByRelatedUsersOrderByCreatedDate(user);
    }

    // create and save comment
    public Comment createComment(Integer ticketid, String author, String content) {
        if (!userRepository.existsById(author)) {
            throw new UserNotFoundException(author);
        } else {
            Ticket ticket = ticketRepository.findTicketById(ticketid);
            Comment comment = new Comment(author, content, ticket);
            ticket.getComments().add(comment);
            Ticket updatedTicket = ticketRepository.save(ticket);
            System.out.println("[createComment] Comment created: " + comment);
            return comment;
        }
    }

    public List<Comment> getCommentsByTicket(int ticketid) {
        Ticket ticket = ticketRepository.findTicketById(ticketid);
        List<Comment> comments = ticket.getComments();
        comments.sort(Comparator.comparing(Comment::getCreatedDate));

        return comments;
    }

    public boolean ticketExists(int ticket_id) {
        return ticketRepository.existsById(ticket_id);
    }

    public Ticket updateTicketStatus(int ticketid, Ticket.Status status) {
        if (!ticketExists(ticketid)) {
            throw new TicketNotFoundException(ticketid);
        }
        Ticket ticket = ticketRepository.findTicketById(ticketid);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return ticket;
    }

    public void naveenTestFunc() {
        User user1;

    }
}
