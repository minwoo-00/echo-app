package com.echo.echo_backend.user.controller;

import com.echo.echo_backend.user.dto.SignupRequest;
import com.echo.echo_backend.user.dto.UserResponse;
import com.echo.echo_backend.user.dto.UserSummaryResponse;
import com.echo.echo_backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody SignupRequest request, Authentication authentication) {
        // JWT에서 userId 추출 (JwtAuthenticationFilter에서 sub를 principal로 넣어둠)
        Long userId = (Long) authentication.getPrincipal();
        return userService.completeSignup(userId, request.getNickname());
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.getUserProfile(userId);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        return userService.getUserProfile(myId, userId);
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> follow(@PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        userService.follow(myId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        userService.unfollow(myId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponse>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowers(userId));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserSummaryResponse>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowings(userId));
    }

}
