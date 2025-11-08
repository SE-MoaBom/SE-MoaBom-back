package SE.demo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int userNumber; //PK
    private String username;
    private String password;
}
