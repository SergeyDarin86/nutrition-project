package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_protocol",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "protocol_id")
    )

    private List<Protocol> protocols;

}