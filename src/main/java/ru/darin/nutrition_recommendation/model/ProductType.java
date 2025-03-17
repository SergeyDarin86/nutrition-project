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
@Table(name = "product_type")
public class ProductType {

    @Id
    @Column(name = "product_type_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productTypeId;

    @Column(name = "product_type")
    private String productType;

    @OneToMany(mappedBy = "productType")
    @Cascade(value = {
            org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.REFRESH,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<Product>products;

}