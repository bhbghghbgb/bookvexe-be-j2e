package com.bookvexe.notificationservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PasswordResetTokenDbModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserDbModel user;

    @Column(name = "token", unique = true, nullable = false, length = 255)
    private String token;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

}
