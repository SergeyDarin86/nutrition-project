package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "person_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID personId;

    @Column(name = "full_name")
    private String fullName;

}
