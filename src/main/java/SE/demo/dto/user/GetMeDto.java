package SE.demo.dto.user;

import lombok.Data;

@Data
public class GetMeDto {
    private int userId; //PK
    private String email;
}
