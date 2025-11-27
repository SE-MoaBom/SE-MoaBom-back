package SE.demo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDto {
    @Schema(example = "test@test.com")
    private String email;

    @Schema(example = "testtest123")
    private String password;
}
