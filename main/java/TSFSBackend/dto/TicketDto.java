package TSFSBackend.dto;

import TSFSBackend.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
public class TicketDto {
    int id;
    String title;
    String description;
    Ticket.Status status;
    Ticket.Category category;
    String emailAddress;
    String assignee;
    ArrayList<String> watchers;
    Date createdDate;
    Date modifiedDate;

    public TicketDto(Ticket ticket) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus();
        this.category = ticket.getCategory();
        this.emailAddress = ticket.getEmailAddress();
        this.assignee = ticket.getAssignee();
        this.watchers = ticket.getWatchers();
        this.createdDate = ticket.getCreatedDate();
        this.modifiedDate = ticket.getModifiedDate();
    }
}
