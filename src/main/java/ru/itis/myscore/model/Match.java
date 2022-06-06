package ru.itis.myscore.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = {"homeTeam", "awayTeam"})

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homeTeam_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "awayTeam_id")
    private Team awayTeam;

    private int goalsHome;

    private int goalsAway;

    private Date date;
}
