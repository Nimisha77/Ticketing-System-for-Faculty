package TSFSBackend.dto;

import TSFSBackend.model.Comment;
import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;

public class DtoHelper {
    public static Ticket ticketFromDto(TicketDto ticketDto) {
        return new Ticket(ticketDto.getTitle(), ticketDto.getDescription(), ticketDto.getStatus(),
                ticketDto.getCategory(), ticketDto.getEmailAddress(), ticketDto.getAssignee(), ticketDto.getWatchers(),
                ticketDto.getCreatedDate(), ticketDto.getModifiedDate());
    }

    public static TicketDto toTicketDto(Ticket ticket) {
         return new TicketDto(ticket);
    }

    public static Comment commentFromDto(CommentDto commentDto) {
        return new Comment(commentDto.getAuthor(), commentDto.content, commentDto.getCreatedDate());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment);
    }

    public static User userFromDto(UserDto userDto) {
        return new User(userDto.getGT_username(), userDto.getName(), userDto.getEmail(), userDto.getAffiliation());
    }
}
