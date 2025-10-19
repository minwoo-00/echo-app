package com.echo.echo_backend.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FollowId  implements Serializable {
    
    private Long userId;     // 팔로우 하는 사람
    private Long followingId;   // 팔로우 대상
}
