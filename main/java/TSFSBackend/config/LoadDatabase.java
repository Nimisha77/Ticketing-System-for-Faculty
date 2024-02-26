package TSFSBackend.config;

import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import TSFSBackend.repository.TicketRepository;
import TSFSBackend.repository.UserRepository;
import TSFSBackend.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@Configuration

// This class is for testing purposes solely
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, TicketRepository ticketRepository) {
        return args -> {
            log.info("Preloading data for testing purposes");
            log.info("Preloading " + userRepository.save(new User("mjiang85",
                    "Meixuan", "mjiang85@gatech.edu", User.Affiliation.faculty)));
            log.info("Preloading " + userRepository.save(new User("mej013",
                    "Meixuan", "mej013@ucsd.edu", User.Affiliation.faculty)));
            log.info("Preloading " + userRepository.save(new User("ayp6",
                    "Ananya", "ayp6@gatech.edu", User.Affiliation.staff)));

//            User dummyUser = new User("nnarayanan33", "Naveen", "nnarayanan33@gatech.edu", User.Affiliation.faculty);
//            userRepository.save(dummyUser);
//            log.info("Dummy user: " + dummyUser.toString());

            User dummyUser1 = new User("testUser", "Naveen", "xxx@gatech.edu", User.Affiliation.faculty);
            Ticket ticket = new Ticket(dummyUser1, "title", "desc", Ticket.Status.open,
                    Ticket.Category.proposals, "mej013@ucsd.edu", "mej013", new ArrayList<String>());
            dummyUser1.addTicket(ticket);
            ticketRepository.save(ticket);
            userRepository.save(dummyUser1);
            log.info("testUser: " + dummyUser1.toString());
            log.info("Preloading " + userRepository.save(new User("naveen123",
                    "Naveen", "nnarayanan33@gatech.edu", User.Affiliation.faculty)));
            log.info("Preloading " + userRepository.save(new User("ananya123",
                    "Ananya", "ayp6@gatech.edu", User.Affiliation.faculty)));
            log.info("Preloading " + userRepository.save(new User("nimisha123",
                    "Nimisha", "nimisha@gatech.edu", User.Affiliation.faculty)));
            log.info("Preloading " + userRepository.save(new User("meixuan123",
                    "Meixuan", "meixuan123@gatech.edu", User.Affiliation.faculty)));


        };
    }
}
