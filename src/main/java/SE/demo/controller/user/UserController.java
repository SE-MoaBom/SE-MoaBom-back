package SE.demo.controller.user;

import SE.demo.dto.user.GetMeDto;
import SE.demo.dto.user.LoginRequestDto;
import SE.demo.dto.user.LoginResponseDto;
import SE.demo.entity.User;
import SE.demo.exception.user.PasswordNotEqualException;
import SE.demo.exception.user.UserDataAccessException;
import SE.demo.exception.user.UserNotFoundException;
import SE.demo.jwt.JwtTokenProvider;
import SE.demo.repository.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/signup")
    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입을 진행합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = LoginRequestDto.class)))
    )
    @ApiResponse(responseCode = "201", content = @Content())
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            userRepository.saveUserInfo(user);
            return ResponseEntity.status(201).build();
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (UserDataAccessException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인을 진행합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = LoginRequestDto.class)))
    )
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(), description = "Wrong email or password")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User userInfo = userRepository.getUserInfo(user.getEmail(), user.getPassword());
            String token = jwtTokenProvider.generateToken(userInfo);
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (UserNotFoundException | PasswordNotEqualException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (UserDataAccessException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<GetMeDto> getMe(@AuthenticationPrincipal User user) {
        GetMeDto getMeDto = new GetMeDto();
        getMeDto.setUserId(user.getUserId());
        getMeDto.setEmail(user.getEmail());
        return ResponseEntity.ok(getMeDto);
    }
}
