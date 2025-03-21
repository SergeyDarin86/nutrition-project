package ru.darin.nutrition_recommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mix")
public class Mix {

    public Mix() {
    }

    public Mix(ProductIllness complexKey, Product product, Illness illness, Resolution resolution) {
        this.complexKey = complexKey;
        this.product = product;
        this.illness = illness;
        this.resolution = resolution;
    }

    @EmbeddedId
    private ProductIllness complexKey;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @MapsId("illnessId")
    @JoinColumn(name = "illness_id")
    @JsonIgnore
    private Illness illness;

    @Column(name = "resolution", nullable = false, columnDefinition = "ENUM('РАЗРЕШЕНО','ЗАПРЕЩЕНО')")
    @Enumerated(EnumType.STRING)
    private Resolution resolution;

}