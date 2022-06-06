package ru.itis.myscore.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)

    private String email;

    private String passwordHash;

    @Column(unique=true)
    private String confirmCode;

    private Timestamp jwtCreated;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private State state;

    public enum Role {
        ADMIN, USER
    }

    public enum State {
        NOT_CONFIRMED, CONFIRMED, BANNED, DELETED
    }

}
