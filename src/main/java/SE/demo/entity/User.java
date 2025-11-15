package SE.demo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int userId; //PK
    private String email;
    private String password;
}
