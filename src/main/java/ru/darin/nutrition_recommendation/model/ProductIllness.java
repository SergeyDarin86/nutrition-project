package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class ProductIllness implements Serializable {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "illness_id")
    private UUID illnessId;

    public ProductIllness() {
    }

    public ProductIllness(UUID productId, UUID illnessId) {
        this.productId = productId;
        this.illnessId = illnessId;
    }
}