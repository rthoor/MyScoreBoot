package ru.itis.myscore.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = {"country", "team"})

public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(value = EnumType.STRING)
    private Position position;

    public enum Position{
        GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD, UNDEFINED;

        public static Position parse(String position) {
            for (Position pos : Position.values()) {
                if (pos.name().equalsIgnoreCase(position)) {
                    return pos;
                }
            }
            return UNDEFINED;
        }
    }

}


