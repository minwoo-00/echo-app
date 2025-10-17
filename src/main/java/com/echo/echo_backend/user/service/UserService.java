package com.echo.echo_backend.user.service;

import com.echo.echo_backend.user.dto.UserResponse;
import com.echo.echo_backend.user.dto.UserSummaryResponse;
import com.echo.echo_backend.user.entity.Follow;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.entity.UserTokens;
import com.echo.echo_backend.user.repository.FollowRepository;
import com.echo.echo_backend.user.repository.UserRepository;
import com.echo.echo_backend.user.repository.UserTokensRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserTokensRepository userTokensRepository;
    private final FollowRepository followRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public UserService(UserRepository userRepository, UserTokensRepository userTokensRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.userTokensRepository = userTokensRepository;
        this.followRepository = followRepository;
    }

    @Transactional(readOnly = true)
    public User findBySpotifyId(String spotify_id) {
        return userRepository.findBySpotifyId(spotify_id).orElse(null);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(Long userId) {
        return toResponse(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(Long myId, Long userId) {
        UserResponse response = toResponse(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
        List<Follow> followingList = followRepository.findByFollowerId(myId);
        for (Follow follow : followingList) {
            if (follow.getFollowingId().equals(userId)) {
                response.setFollowState(true);
            }
        }
        return response;
    }

    @Transactional
    public User createUser(String spotifyId, String email,String accessToken, String refreshToken, int expiresIn) {
        User user = userRepository.save(new User(spotifyId, email)); // String spotify_id, String email
        userTokensRepository.saveTokens(new UserTokens(spotifyId, accessToken, refreshToken, expiresIn));
        return user;
    }

    @Transactional
    public UserResponse completeSignup(Long id, String nickname) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setNickname(nickname);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public boolean isNicknamePossible(String nickname) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (user.getNickname().equals(nickname)) {
                return false;
            }
        }
        if (nickname.isEmpty()) {
            return false;
        }
        return true;
    }

    @Transactional
    public void updateTokens(String spotifyId, String accessToken, String refreshToken, int expiresIn) {
        userTokensRepository.updateTokens(spotifyId,accessToken,refreshToken,expiresIn);
    }

    @Transactional
    public void follow(Long id, Long followingId) {
        if (id.equals(followingId)) {
            return;
        }

        followRepository.save(id, followingId);
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")).incrementFollowing();
        userRepository.findById(followingId).orElseThrow(() -> new IllegalArgumentException("User not found")).incrementFollower();
    }

    @Transactional
    public void unfollow(Long id, Long followingId) {
        followRepository.delete(id, followingId);
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")).decrementFollowing();
        userRepository.findById(followingId).orElseThrow(() -> new IllegalArgumentException("User not found")).decrementFollower();
    }

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        List<UserSummaryResponse> followList = new ArrayList<>();
        for (Follow follow : follows) {
            User user = userRepository.findById(follow.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            followList.add(new UserSummaryResponse(user.getId(), user.getNickname()));
        }
        return followList;
    }

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> getFollowings(Long userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);
        List<UserSummaryResponse> followList = new ArrayList<>();
        for (Follow follow : follows) {
            User user = userRepository.findById(follow.getFollowingId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            followList.add(new UserSummaryResponse(user.getId(), user.getNickname()));
        }
        return followList;
    }

    @Transactional
    public UserResponse updateProfileImage(Long userId, MultipartFile file) {

        try {
            // 디렉토리 없으면 생성
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // 파일 이름 유니크하게 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(fileName);

            // 파일 저장
            file.transferTo(filePath.toFile());

            // 개발/운영에 따라 URL 계산
            String urlPath;
            if (uploadDir.contains("uploads")) {
                // 개발: 정적 리소스 → localhost:8080/uploads/...
                urlPath = "/uploads/" + fileName;
            } else {
                // 운영: 외부 경로 → Nginx 등에서 /uploads/로 매핑해줘야 함
                urlPath = "/uploads/" + fileName;
            }

            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            user.setProfileImageUrl(urlPath);

            return toResponse(user);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Transactional
    public UserResponse updateProfileMessage(Long userId, String profileMessage) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setProfileMessage(profileMessage);
        return toResponse(user);
    }



    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getProfileMessage(),
                user.getProfileImageUrl(),
                user.getFollowerCnt(),
                user.getFollowingCnt(),
                user.getRateCnt()
        );
    }
}
