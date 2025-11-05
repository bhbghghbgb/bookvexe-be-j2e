package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserSessionDbModel extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;

    @Column(name = "accessToken", unique = true, nullable = false, length = 255)
    private String accessToken;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "revoked")
    private Boolean revoked = false;
}