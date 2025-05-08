package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "allergen_type")
public class AllergenType {

    @Id
    @Column(name = "allergen_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID allergenId;

    @Column(name = "allergen_title")
    private String allergenTitle;

    @Column(name = "title_color")
    private String titleColor;

    @ManyToMany(mappedBy = "allergenTypes")

    private List<Product> products;

}