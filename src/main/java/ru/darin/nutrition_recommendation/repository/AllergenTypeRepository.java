package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.darin.nutrition_recommendation.model.AllergenType;
import ru.darin.nutrition_recommendation.model.ProductType;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergenTypeRepository extends JpaRepository<AllergenType, UUID> {

    Optional<AllergenType> findByAllergenTitle(String allergenTitle);

}