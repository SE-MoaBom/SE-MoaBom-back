package SE.demo.controller;

import SE.demo.entity.User;
import SE.demo.jwt.util.JwtTokenProvider;
import SE.demo.repository.User.UserRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        userRepository.saveUserInfo(user);
        return ResponseEntity.status(201).body(user);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> userInfo = userRepository.getUserInfo(user.getUsername(), user.getPassword());
        if (userInfo.isPresent()) {
            String token = jwtTokenProvider.generateToken(user.getUsername());
            log.info(token);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @GetMapping("/api/user/test")
    public ResponseEntity<?> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = (String) authentication.getPrincipal();
            return ResponseEntity.ok("Token valid, user: " + username);
        }
        log.info("No authentication found");
        return ResponseEntity.status(401).body("Token missing or invalid");
    }
}
