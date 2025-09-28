package com.echo.echo_backend.user.service;

import com.echo.echo_backend.user.dto.UserResponse;
import com.echo.echo_backend.user.dto.UserSummaryResponse;
import com.echo.echo_backend.user.entity.Follow;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.entity.UserTokens;
import com.echo.echo_backend.user.repository.FollowRepository;
import com.echo.echo_backend.user.repository.UserRepository;
import com.echo.echo_backend.user.repository.UserTokensRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserTokensRepository userTokensRepository;
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, UserTokensRepository userTokensRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.userTokensRepository = userTokensRepository;
        this.followRepository = followRepository;
    }

    public User findBySpotifyId(String spotify_id) {
        return userRepository.findBySpotifyId(spotify_id);
    }

    public UserResponse getUserProfile(Long userId) {
        return toResponse(userRepository.findById(userId));
    }

    public UserResponse getUserProfile(Long myId, Long userId) {
        UserResponse response = toResponse(userRepository.findById(userId));
        List<Follow> followingList = followRepository.findByFollowerId(myId);
        for (Follow follow : followingList) {
            if (follow.getFollowingId().equals(userId)) {
                response.setFollowState(true);
            }
        }
        return response;
    }

    public User createUser(String spotifyId, String email,String accessToken, String refreshToken, int expiresIn) {
        User user = userRepository.saveUser(new User(spotifyId, email));
        userTokensRepository.saveTokens(new UserTokens(spotifyId, accessToken, refreshToken, expiresIn));
        return user;
    }

    public UserResponse completeSignup(Long id, String nickname) {
        User user = userRepository.updateNickname(id, nickname);
        return toResponse(user);
    }

    public void updateTokens(String spotifyId, String accessToken, String refreshToken, int expiresIn) {
        userTokensRepository.updateTokens(spotifyId,accessToken,refreshToken,expiresIn);
    }

    public void follow(Long id, Long followingId) {
        followRepository.save(id, followingId);
        userRepository.findById(id).incrementFollowing();
        userRepository.findById(followingId).incrementFollower();
    }

    public void unfollow(Long id, Long followingId) {
        followRepository.delete(id, followingId);
        userRepository.findById(id).decrementFollowing();
        userRepository.findById(followingId).decrementFollower();
    }

    public List<UserSummaryResponse> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        List<UserSummaryResponse> followList = new ArrayList<>();
        for (Follow follow : follows) {
            User user = userRepository.findById(follow.getUserId());
            followList.add(new UserSummaryResponse(user.getId(), user.getNickname()));
        }
        return followList;
    }

    public List<UserSummaryResponse> getFollowings(Long userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);
        List<UserSummaryResponse> followList = new ArrayList<>();
        for (Follow follow : follows) {
            User user = userRepository.findById(follow.getFollowingId());
            followList.add(new UserSummaryResponse(user.getId(), user.getNickname()));
        }
        return followList;
    }



    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getProfileMassage(),
                user.getFollowerCnt(),
                user.getFollowingCnt(),
                user.getRateCnt(),
                user.getAvgRate()
        );
    }
}
