package SE.demo.dto;

import lombok.Data;

@Data
public class GetMeDto {
    private int userId; //PK
    private String email;
}
