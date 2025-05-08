package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.darin.nutrition_recommendation.model.AllergenType;

import java.util.UUID;

@Repository
public interface AllergenTypeRepository extends JpaRepository<AllergenType, UUID> {
}