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
public class ProductProtocol implements Serializable {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "protocol_id")
    private UUID protocolId;

    public ProductProtocol() {
    }

    public ProductProtocol(UUID productId, UUID protocolId) {
        this.productId = productId;
        this.protocolId = protocolId;
    }

}