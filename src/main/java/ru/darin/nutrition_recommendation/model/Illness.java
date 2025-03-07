package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "illness")
public class Illness {

    @Id
    @Column(name = "illness_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID illness_id;

    @Column(name = "illness_title")
    private String illnessTitle;

    @Override
    public String toString() {
        return "Illness{" +
                "illnessTitle='" + illnessTitle + '\'' +
                '}';
    }
}
