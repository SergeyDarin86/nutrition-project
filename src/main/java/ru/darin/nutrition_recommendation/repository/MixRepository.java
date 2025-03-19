package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.darin.nutrition_recommendation.model.Mix;

import java.util.UUID;

public interface MixRepository extends JpaRepository<Mix, UUID> {
}