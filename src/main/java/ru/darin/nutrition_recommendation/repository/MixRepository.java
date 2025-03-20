package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.darin.nutrition_recommendation.model.Mix;
import ru.darin.nutrition_recommendation.model.ProductIllness;

@Repository
public interface MixRepository extends JpaRepository<Mix, ProductIllness> {
}