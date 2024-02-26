package TSFSBackend.dto;

import TSFSBackend.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    String author;
    String content;
    Date createdDate;

    public CommentDto(Comment comment) {
        this.author = comment.getAuthor();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
    }
}
