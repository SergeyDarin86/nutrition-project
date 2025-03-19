package ru.darin.nutrition_recommendation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "mix")
public class Mix {

    @EmbeddedId
    private ProductIllness complexKey;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("illnessId")
    @JoinColumn(name = "illness_id")
    private Illness illness;

    @Column(name = "resolution", nullable = false, columnDefinition = "ENUM('РАЗРЕШЕНО','ЗАПРЕЩЕНО')")
    @Enumerated(EnumType.STRING)
    private Resolution resolution;

    @Getter
    @Setter
    @Embeddable
    class ProductIllness implements Serializable {

        @Column(name = "product_id")
        private UUID productId;

        @Column(name = "illness_id")
        private UUID illnessId;

    }

}