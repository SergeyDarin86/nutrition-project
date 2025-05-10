package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    public Product() {
    }

    public Product(UUID product_id, String product, ProductType productType, List<Mix> mixes) {
        this.product_id = product_id;
        this.product = product;
        this.productType = productType;
        this.mixes = mixes;
    }

    @Id
    @Column(name = "product_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID product_id;

    @Column(name = "product")
    private String product;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @OneToMany(mappedBy = "product")
    private List<Mix>mixes;

    @ManyToMany(cascade = {CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "allergen_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<AllergenType> allergenTypes;

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", product='" + product + '\'' +
                ", productType=" + productType +
                ", allergenTypes=" + allergenTypes +
                '}';
    }
}