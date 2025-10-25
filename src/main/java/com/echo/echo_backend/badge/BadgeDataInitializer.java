package com.echo.echo_backend.badge;

import com.echo.echo_backend.badge.entity.Badge;
import com.echo.echo_backend.badge.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeDataInitializer implements CommandLineRunner {
    private final BadgeRepository badgeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (badgeRepository.count() == 0) {
            List<Badge> defaultBadges = List.of(
                    new Badge("Pop Master", "Enjoys mainstream and catchy pop sounds — a true pop enthusiast.", ""),
                    new Badge("Rock Guardian", "Loves energetic guitars and powerful rock vibes that define modern rock.", ""),
                    new Badge("Hip-Hop Collector", "Drawn to deep beats, flow, and rhythm — a passionate hip-hop lover.", ""),
                    new Badge("Soul Groover", "Feels the groove of soul, funk, and R&B — always following the rhythm.", ""),
                    new Badge("Beat Maker", "Immersed in electronic beats and danceable soundscapes.", ""),
                    new Badge("Metal Head", "Enjoys loud riffs and heavy energy — a true metal spirit.", ""),
                    new Badge("Jazz Clubber", "Appreciates smooth jazz, blues, and timeless improvisation.", ""),
                    new Badge("Acoustic Dreamer", "Finds comfort in gentle acoustic and folk sounds.", ""),
                    new Badge("Classic Archive", "Enjoys timeless masterpieces and elegant classical tones.", ""),
                    new Badge("K-Pop Explorer", "Loves K-pop and Asian pop culture — curious and trendy listener.", ""),
                    new Badge("World Traveler", "Explores diverse global sounds across cultures.", ""),
                    new Badge("O.S.T Collector", "Enjoys soundtracks and cinematic scores that evoke emotion.", ""),
                    new Badge("Mood Curator", "Selects music by mood — the perfect vibe for every moment.", ""),
                    new Badge("Golden Age Guardian", "Cherishes the timeless classics from the golden era of music.", ""),
                    new Badge("90’s Rewinder", "Nostalgic for the hits and emotions of the 1990s.", ""),
                    new Badge("Millennium Traveler", "Drawn to the early 2000s’ energetic and emotional tracks.", ""),
                    new Badge("Chart Lover", "Enjoys the hottest and most popular songs on the charts.", ""),
                    new Badge("Deep Diver", "Prefers hidden gems and underrated tracks beyond the mainstream.", ""),
                    new Badge("Global Nomad", "Loves unique world music and non-mainstream international genres.", ""),
                    new Badge("All-rounder", "Has a balanced and versatile taste across all genres.", "")
            );

            badgeRepository.saveAll(defaultBadges);
            log.info("Default badges initialized!");
        } else {
            log.info("Badges already initialized!");
        }
    }
}
