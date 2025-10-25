package com.echo.echo_backend.badge.service;

import com.echo.echo_backend.badge.BadgeRuleLoader;
import com.echo.echo_backend.badge.dto.BadgeDecision;
import com.echo.echo_backend.badge.entity.Badge;
import com.echo.echo_backend.badge.entity.UserBadge;
import com.echo.echo_backend.badge.repository.BadgeRepository;
import com.echo.echo_backend.badge.repository.UserBadgeRepository;
import com.echo.echo_backend.track.entity.Rating;
import com.echo.echo_backend.track.entity.Track;
import com.echo.echo_backend.track.repository.RatingRepository;
import com.echo.echo_backend.track.repository.TrackRepository;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.*;
import com.openai.models.responses.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final RatingRepository ratingRepository;
    private final TrackRepository trackRepository;
    private final BadgeRuleLoader badgeRuleLoader;
    private final OpenAIClient client;
    private final ObjectMapper objectMapper;

    @Transactional
    public void generateBadge(Long userId) {

        List<Rating> ratings = ratingRepository.findByIdUserId(userId);


        List<Map<String, Object>> ratingDtos = ratings.stream()
                .map(r -> {
                    Track track = trackRepository.findById(r.getTrackId()).orElseThrow(() -> new IllegalArgumentException("Track not found"));
                    return Map.of(
                        "title", track.getTitle(),
                            "artist", track.getArtists(),
                            "rating", r.getRate()
                );})
                .toList();

        String userTracks;
        try {
            userTracks = objectMapper.writeValueAsString(ratingDtos);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }

        String badgeRules = badgeRuleLoader.loadBadgeRules();
        String finalPrompt = """
                %s
                
                User's rated songs:
                %s
                """.formatted(badgeRules, userTracks);

        StructuredChatCompletionCreateParams<BadgeDecision> params = StructuredChatCompletionCreateParams.<BadgeDecision>builder()
                .model(ChatModel.GPT_5)
                .addUserMessage(finalPrompt)
                .responseFormat(BadgeDecision.class)
                .build();
        StructuredChatCompletion<BadgeDecision> chatCompletion = client.chat().completions().create(params);
        BadgeDecision jsonContent = chatCompletion.choices().getFirst().message().content().get();

        String badgeName = jsonContent.getBadgeName();
        String aiComment = jsonContent.getAiComment();

        log.info("뱃지 발급! " + badgeName + " " + aiComment);

        UserBadge userBadge = new UserBadge();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Badge badge = badgeRepository.findByBadgeName(badgeName).orElseThrow(() -> new IllegalArgumentException("Badge not found"));
        userBadge.setUser(user);
        userBadge.setBadge(badge);
        userBadge.setAiComment(aiComment);
        UserBadge save = userBadgeRepository.save(userBadge);
    }
}
