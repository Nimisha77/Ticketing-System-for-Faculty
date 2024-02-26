package TSFSBackend.service;


import TSFSBackend.dto.UserDto;

import TSFSBackend.exception.UserNotFoundException;
import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import TSFSBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public void addTicket(String userid, Ticket ticket) {
        User user = userRepository.findUserByGtUsername(userid);
        user.getTicketList().add(ticket);
        ticket.getRelatedUsers().add(user);
        userRepository.save(user);
    }


    /*public void addWatcher(String GT_username, Ticket ticket) {
        User user = userRepository.findUserByGtUsername(GT_username);
        user.getWathcerList().add(ticket);
        userRepository.save(user);
    }*/


    public boolean isStaff(String GT_username) {
        User user = userRepository.findUserByGtUsername(GT_username);
        if (user != null) {
            return user.getAffiliation() == User.Affiliation.staff;
        } else {
            throw new UserNotFoundException(GT_username);
        }
    }

    public boolean isFaculty(String GT_username) {
        User user = userRepository.findUserByGtUsername(GT_username);
        if (user != null) {
            return user.getAffiliation() == User.Affiliation.faculty;
        } else {
            throw new UserNotFoundException(GT_username);
        }
    }

    public boolean addUser(UserDto user){
        try {
            User usr = new User(user.getGT_username(), user.getName(), user.getEmail(), user.getAffiliation());
            userRepository.save(usr);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

//    public User[] getAllUsers(){
//        return userRepository.findAll().toArray(new User[]);
//    }


    public User isValidUser(String GT_username){
        User user = userRepository.findUserByGtUsername(GT_username);
        if(user == null){
            return null;
        }
        return user;
    }
}
