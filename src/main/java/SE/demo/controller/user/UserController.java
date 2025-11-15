package SE.demo.controller.user;

import SE.demo.dto.user.GetMeDto;
import SE.demo.entity.User;
import SE.demo.exception.user.PasswordNotEqualException;
import SE.demo.exception.user.UserDataAccessException;
import SE.demo.exception.user.UserNotFoundException;
import SE.demo.jwt.JwtTokenProvider;
import SE.demo.repository.User.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/signup")
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
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User userInfo = userRepository.getUserInfo(user.getEmail(), user.getPassword());
            String token = jwtTokenProvider.generateToken(userInfo);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (UserNotFoundException | PasswordNotEqualException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (UserDataAccessException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal User user) {
        GetMeDto getMeDto = new GetMeDto();
        getMeDto.setUserId(user.getUserId());
        getMeDto.setEmail(user.getEmail());
        return ResponseEntity.ok(getMeDto);
    }
}
