package TSFSBackend.dto;

import TSFSBackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String GT_username;
    private String name;
    private String email;
    User.Affiliation affiliation;

    public UserDto(User user) {
        this.GT_username = user.getGtUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.affiliation = user.getAffiliation();
    }
}
