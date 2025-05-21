package org.example.locatorapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginDTO login) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String username = login.getUsername();
        User user = userService.findByUsername(username);

        if (user != null && encoder.matches(login.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(login.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@RequestBody LoginDTO login) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(login.getPassword());
        try {
            userService.userSignup(login.getUsername(), hashedPassword);
            return ResponseEntity.status(201).body("User created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erorr saving user");
        }
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secure(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (jwtService.validateToken(token)) {
            System.out.println("hello from secure...");
            return ResponseEntity.ok("Hello " + jwtService.extractUsername(token));
        }
        return ResponseEntity.status(403).body("Invalid token");
    }
}
