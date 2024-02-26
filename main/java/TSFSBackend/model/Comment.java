package TSFSBackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Setter
@Getter

public class Comment {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String author;
    @CreationTimestamp
    Date createdDate;
    String content;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent", referencedColumnName = "id")
    Ticket parent;

    public Comment(String author, String content, Ticket ticket) {
        this.author = author;
        this.content = content;
        this.parent = ticket;
    }

    public Comment(String author, String content, Date createdDate) {
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("author:" + getAuthor()
                + ", createdDate:" + getCreatedDate()
                + ", content:" + getContent());
        return sb.toString();
    }
}
