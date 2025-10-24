package com.echo.echo_backend.user.controller;

import com.echo.echo_backend.user.dto.*;
import com.echo.echo_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Register or update user nickname", description = "If user is a new member, register nickname and change nickname if user is not.")
    @PostMapping("/signup")
    public UserResponse signup(@RequestBody SignupRequest request, Authentication authentication) {
        // JWT에서 userId 추출 (JwtAuthenticationFilter에서 sub를 principal로 넣어둠)
        Long userId = (Long) authentication.getPrincipal();
        return userService.completeSignup(userId, request.getNickname());
    }

    @Operation(summary = "Check nickname validation", description = "If nickname already exists or empty, returns false")
    @PostMapping("/check")
    public NicknameCheckResponse checkNickname(@RequestBody SignupRequest request, Authentication authentication) {
        String nickname = request.getNickname();
        boolean nicknamePossible = userService.isNicknamePossible(nickname);
        return new NicknameCheckResponse(String.valueOf(nicknamePossible));
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "정상적으로 팔로우 한 경우"),
            @ApiResponse(responseCode = "400", description = "이미 팔로우 중인 경우"),
            @ApiResponse(responseCode = "409", description = "자기 자신 또는 없는 유저는 팔로우 불가")
    })
    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> follow(
            @Parameter(description = "The ID of the user to follow") @PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        try {
            boolean successed = userService.follow(myId, userId);
            if (successed) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 팔로우 중입니다.");
            }
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Unfollow a user", description = "Unfollow a specific user by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상적으로 팔로우 삭제"),
            @ApiResponse(responseCode = "404", description = "삭제할 팔로우 관계가 존재하지 않음"),
    })
    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<String> unfollow(
            @Parameter(description = "The ID of the user to unfollow") @PathVariable Long userId, Authentication authentication) {
        Long myId = (Long) authentication.getPrincipal();
        boolean successed = userService.unfollow(myId, userId);

        if (successed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("팔로우 관계가 존재하지 않습니다.");
        }
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
