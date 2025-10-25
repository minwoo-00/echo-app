package com.echo.echo_backend.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "badges")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @Column(nullable = false)
    private String badgeName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String iconUrl;

    public Badge(String badgeName, String description, String iconUrl) {
        this.badgeName = badgeName;
        this.description = description;
        this.iconUrl = iconUrl;
    }
}
