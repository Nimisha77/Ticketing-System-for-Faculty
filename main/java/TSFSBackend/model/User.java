package TSFSBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "_users") //User is reserved in MySQL
public class User {
    @Id
    @Column(name = "GT_username")
    private String gtUsername;
    private String name;
    private String email;
    Affiliation affiliation;


    @JsonManagedReference
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey(name = "id")
    private final List<Ticket> ownerTicket = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name="user_tickets",
        joinColumns = {@JoinColumn(name = "GT_username")},
        inverseJoinColumns = {@JoinColumn(name = "ticket_id")})
    @JsonIgnore
    private final Set<Ticket> ticketList = new HashSet<>();


    // unidirectional many-to-many mapping
    /*
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="watcher_list", joinColumns = @JoinColumn(name="id"), inverseJoinColumns = @JoinColumn(name="GT_username"))
    private final List<Ticket> wathcerList = new ArrayList<>();
    */


    public enum Affiliation {
        faculty,
        staff
    }


    public void addTicket(Ticket ticket) {
        this.ticketList.add(ticket); // add ticket to this user
        ticket.getRelatedUsers().add(this); // add this user to the ticket
    }

    public void removeTicket(int ticketid) {
        Ticket ticket = this.ticketList.stream().filter(t -> t.getId() == ticketid).findFirst().orElse(null);
        if (ticket != null) {
            this.ticketList.remove(ticketid);
            ticket.getRelatedUsers().remove(this);
        }
    }
}
