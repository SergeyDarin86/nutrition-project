package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "protocol")
public class Protocol {

    @Id
    @Column(name = "protocol_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID protocol_id;

    @Column(name = "protocol_title")
    private String protocolTitle;

    @ManyToMany(mappedBy = "protocols")
    private List<Person> people;

    @OneToMany(mappedBy = "protocol")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Mix>mixes;

    @Override
    public String toString() {
        return "Protocol{" +
                "protocolTitle='" + protocolTitle + '\'' +
                '}';
    }

}