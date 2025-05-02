package ru.darin.nutrition_recommendation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.darin.nutrition_recommendation.model.Protocol;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, UUID> {
    Optional<Protocol> findByProtocolTitle(String protocolTitle);

    @Modifying
    @Query(value = "delete from person_protocol where protocol_id = ?1; " +
            "delete from protocol where protocol_id = ?1", nativeQuery = true)
    void deleteFromPersonProtocolTableAndProtocolTableByProtocolId(UUID uuid);

}