package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DELETE_ORPHAN})
    private List<Product>products;

}