package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDbModel extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserDbModel user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String tokenType; // "ACCESS", "REFRESH", "RESET_PASSWORD"

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private Boolean revoked = false;
}
