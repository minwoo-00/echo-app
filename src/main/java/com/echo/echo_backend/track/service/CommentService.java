package com.echo.echo_backend.track.service;

import com.echo.echo_backend.track.dto.CommentDto;
import com.echo.echo_backend.track.dto.CommentRequest;
import com.echo.echo_backend.track.dto.ImageDto;
import com.echo.echo_backend.track.dto.TrackInfoDto;
import com.echo.echo_backend.track.entity.Comment;
import com.echo.echo_backend.track.entity.Rating;
import com.echo.echo_backend.track.entity.Track;
import com.echo.echo_backend.track.repository.CommentRepository;
import com.echo.echo_backend.track.repository.RatingRepository;
import com.echo.echo_backend.track.repository.TrackRepository;
import com.echo.echo_backend.user.entity.User;
import com.echo.echo_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository, TrackRepository trackRepository, RatingRepository ratingRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.ratingRepository = ratingRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public TrackInfoDto createOrUpdateComment(CommentRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        //트랙이 db에 없으면 새로 생성
        List<Track.Image> images = Track.toImage(request.getImages());
        Track track = trackRepository.findById(request.getSpotifyId())
                .orElseGet(() -> {
                    Track newTrack = Track.builder()
                            .trackId(request.getSpotifyId())
                            .title(request.getName())
                            .artists(request.getArtist())
                            .spotifyUri(request.getSpotifyUri())
                            .images(images)
                            .build();
                    return trackRepository.save(newTrack);
                });

        //기존 코멘트 있으면 수정, 없으면 새로 등록
        Comment comment = commentRepository.findByUserIdAndTrackId(userId, track.getTrackId())
                .map(existing -> {
                    existing.setContent(request.getContent());
                    return existing;
                })
                .orElseGet(() -> commentRepository.save(
                        Comment.builder()
                                .userId(userId)
                                .userNickname(user.getNickname())
                                .trackId(track.getTrackId())
                                .content(request.getContent())
                                .build()
                ));

        //트랙의 평점 계산
        List<Rating> ratings = ratingRepository.findByIdTrackId(track.getTrackId());
        int rateCnt = ratings.size();
        Double avgRate = ratings.stream().mapToDouble(Rating::getRate).average().orElse(0.0);
        Double myRate = ratings.stream()
                .filter(r -> r.getUserId().equals(userId))
                .map(Rating::getRate)
                .findFirst()
                .orElse(0.0);

        List<CommentDto> comments = commentRepository.findByTrackId(track.getTrackId())
                .stream()
                .map(CommentDto::fromEntity)
                .toList();

        return TrackInfoDto.builder()
                .name(track.getTitle())
                .spotifyId(track.getTrackId())
                .artist(track.getArtists())
                .spotifyUri(track.getSpotifyUri())
                .images(request.getImages())
                .avgRate(avgRate)
                .rateCnt(rateCnt)
                .myRate(myRate)
                .comments(comments)
                .build();
    }

    @Transactional
    public TrackInfoDto deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Track track = trackRepository.findById(comment.getTrackId())
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        commentRepository.deleteById(commentId);

        //트랙의 평점 계산
        List<Rating> ratings = ratingRepository.findByIdTrackId(track.getTrackId());
        int rateCnt = ratings.size();
        Double avgRate = ratings.stream().mapToDouble(Rating::getRate).average().orElse(0.0);
        Double myRate = ratings.stream()
                .filter(r -> r.getUserId().equals(userId))
                .map(Rating::getRate)
                .findFirst()
                .orElse(0.0);

        List<CommentDto> comments = commentRepository.findByTrackId(track.getTrackId())
                .stream()
                .map(CommentDto::fromEntity)
                .toList();

        return TrackInfoDto.builder()
                .name(track.getTitle())
                .spotifyId(track.getTrackId())
                .artist(track.getArtists())
                .spotifyUri(track.getSpotifyUri())
                .images(ImageDto.toImageDto(track.getImages()))
                .avgRate(avgRate)
                .rateCnt(rateCnt)
                .myRate(myRate)
                .comments(comments)
                .build();
    }
}
