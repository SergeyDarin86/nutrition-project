package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.darin.nutrition_recommendation.model.Illness;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IllnessRepository extends JpaRepository<Illness, UUID> {
    Optional<Illness> findByIllnessTitle(String illnessTitle);

    @Modifying
    @Query(value = "delete from person_illness where illness_id = ?1; " +
            "delete from illness where illness_id = ?1", nativeQuery = true)
    void deleteFromPersonIllnessTableAndIllnessTableByIllnessId(UUID uuid);

}