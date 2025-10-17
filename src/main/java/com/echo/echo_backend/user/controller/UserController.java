package com.echo.echo_backend.user.controller;

import com.echo.echo_backend.user.dto.SignupRequest;
import com.echo.echo_backend.user.dto.UpdateProfileMessageRequest;
import com.echo.echo_backend.user.dto.UserResponse;
import com.echo.echo_backend.user.dto.UserSummaryResponse;
import com.echo.echo_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @Operation(summary = "Check nickname validation", description = "If nickname already exists or empty, returns false")
    @PostMapping("/check")
    public Map<String, String> checkNickname(@RequestBody SignupRequest request, Authentication authentication) {
        String nickname = request.getNickname();
        boolean nicknamePossible = userService.isNicknamePossible(nickname);
        return Map.of("possible", String.valueOf(nicknamePossible));
    }

    @Operation(summary = "Get current user profile", description = "Returns the profile of the currently authenticated user.")
    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return userService.getUserProfile(userId);
    }

    @Operation(summary = "Get user profile by ID", description = "Returns the profile of a specific user by their ID.")
    @GetMapping("/{userId}")
    public UserResponse getUser(
            @Parameter(description = "The ID of the user to fetch") @PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        return userService.getUserProfile(myId, userId);
    }

    @Operation(summary = "Follow a user", description = "Follow a specific user by their ID.")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> follow(
            @Parameter(description = "The ID of the user to follow") @PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        userService.follow(myId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unfollow a user", description = "Unfollow a specific user by their ID.")
    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<Void> unfollow(
            @Parameter(description = "The ID of the user to unfollow") @PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        userService.unfollow(myId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get followers of a user", description = "Retrieve a list of users who follow the specified user.")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserSummaryResponse>> getFollowers(
            @Parameter(description = "The ID of the user whose followers are requested") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowers(userId));
    }

    @Operation(summary = "Get followings of a user", description = "Retrieve a list of users whom the specified user is following.")
    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserSummaryResponse>> getFollowings(
            @Parameter(description = "The ID of the user whose followings are requested") @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowings(userId));
    }

    @Operation(summary = "Upload profile image", description = "Upload or update the profile image of a specific user.")
    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<UserResponse> uploadProfileImage(
            @Parameter(description = "The ID of the user uploading the profile image")
            @PathVariable Long userId,
            @Parameter(description = "The profile image file to upload")
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfileImage(userId, file));
    }

    @Operation(summary = "Update my profile message",
            description = "Update the profile message of the currently authenticated user.")
    @PatchMapping("/me/profile-message")
    public ResponseEntity<UserResponse> updateProfileMessage(@RequestBody UpdateProfileMessageRequest request, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateProfileMessage(userId, request.getProfileMessage()));
    }

}
