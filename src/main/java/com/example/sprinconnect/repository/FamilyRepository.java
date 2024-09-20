package com.example.sprinconnect.repository;

import com.example.sprinconnect.models.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {
//    Optional<Family> findByFullName(String firstName, String lastName);

    @Query("SELECT f FROM Family f WHERE f.firstName = :firstName AND f.lastName = :lastName AND f.user.id = :userId")
    Optional<Family> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("userId") long userId);

    @Query("SELECT f FROM Family f WHERE f.user.id = :userId")
    Page<Family> findAllByUserId(@Param("userId") long userId, Pageable pageable);
}
