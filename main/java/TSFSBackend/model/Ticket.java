package TSFSBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class Ticket {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Foreign Key
    @Getter
    @Setter
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "owner", referencedColumnName = "GT_username")
    private User owner;  // the author is mapped to ==> the user who created the ticket

    @Getter
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "ticketList")
    @JsonIgnore
    private Set<User> relatedUsers = new HashSet<>();

    @Getter
    @Setter
    @NotEmpty
    @Column(name = "title")
    private String title;

    @Getter
    @Setter
    @NotEmpty
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @Column(name = "status")
    private Status status;

    @Getter
    @Setter
    @Column(name = "category")
    private Category category;

    @Getter
    @Setter
    @NotEmpty
    @Column(name = "emailAddress")
    private String emailAddress;

    @Getter
    @Setter
    @Column(name = "assignee")
    private String assignee;


    @Getter
    @Setter
    @Column(name = "watchers")
    private ArrayList<String> watchers;

    @Getter
    @CreationTimestamp
    private Date createdDate;

    @Getter
    @UpdateTimestamp
    private Date modifiedDate;


    @Getter
    @Setter
    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public enum Status {
        open,
        closed,
        needs_attention
    }

    public enum Category {
//        @JsonProperty("travel authorization")
        travel_authorization,
        reimbursement,
//        @JsonProperty("meeting organization")
        meeting_organization,
//        @JsonProperty("student hiring")
        student_hiring,
        proposals,
        miscellaneous
    }

    @Builder
    public Ticket(User user, String title, String description, Status status, Category category, String emailAddress, String assignee,
                  ArrayList<String> watchers) {
        //this.owner = user;
        this.relatedUsers.add(user);
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.emailAddress = emailAddress;
        this.assignee = assignee;
        this.watchers = watchers;
    }


    public Ticket(String title, String description, Status status, Category category, String emailAddress, String assignee,
                  ArrayList<String> watchers, Date createdDate, Date modifiedDate) {
        //this.owner = user;
        //this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.emailAddress = emailAddress;
        this.assignee = assignee;
        this.watchers = watchers;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

/*
    public void addRelatedUser(User user) {
        relatedUsers.add(user);
        user.getTicketList().add(this);
    }

    public void removeRelatedUser(String GT_username) {
        User user = this.relatedUsers.stream().filter(u -> u.getGtUsername() == GT_username).findFirst().orElse(null);
        if (user != null) {
            this.relatedUsers.remove(user);
            user.getTicketList().remove(this);
        }
    }*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:" + getId()
            + ", title:" + getTitle()
            + ", description:" + getDescription()
            + ", status:" + getStatus()
            + ", category:" + getCategory()
            + ", emailAddress:" + getEmailAddress()
            + ", assignee:" + getAssignee()
            + ", watchers:" + getWatchers()
            + ", createdDate:" + getCreatedDate()
            + ", modifiedDate:" + getModifiedDate());
        return sb.toString();
    }

}
