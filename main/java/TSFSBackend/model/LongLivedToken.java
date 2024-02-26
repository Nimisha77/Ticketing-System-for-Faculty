package TSFSBackend.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;

//"sub": "ayp6",
//        "service": "08BBD827-9DDD-4D5B-B284-F8E35BBF9FF6",
//        "auth_time": 1680728536,
//        "attributes": {},
//        "id": "ayp6",
//        "client_id": "08BBD827-9DDD-4D5B-B284-F8E35BBF9FF6"
//         "long_lived_token": ""
//          "expiration_date": " "
@Data
@NoArgsConstructor
@Setter
@Getter
public class LongLivedToken {
    String sub;
    String service;
    String auth_time;
    ArrayList attributes;
    String id;
    String client_id;
    String long_lived_token;
    @CreationTimestamp
    Date expiration_date;

    public LongLivedToken(String sub, String service, String auth_time, ArrayList attributes, String id,
                          String client_id, String long_lived_token, Date expirationDate) {
        this.sub = sub;
        this.service = service;
        this.auth_time = auth_time;
        this.attributes = attributes;
        this.id = id; //this is user id.
        this.client_id = client_id;
        this.long_lived_token = long_lived_token;
        this.expiration_date = expirationDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sub:" + getSub()
                + ", service:" + getService()
                + ", auth_time:" + getAuth_time()
                + ", attributes:" + getAttributes()
                + ", id:" + getId()
                + ", client_id:" + getClient_id()
                + ", long_lived_token:" + getLong_lived_token()
                + ", expiration_date:" + getExpiration_date());
        return sb.toString();
    }

}
