package com.example.sprinconnect.repository;

import com.example.sprinconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//JpaSpecificationExecutor<User> need para sa filtering
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);


//    -----------Kinahanglan pareho si -------- :profileId ug profileId sa @Param("profileId")-------------------------
    @Query("SELECT u FROM User u WHERE u.userProfile.id = :profileId") //query para sa pagkuha sa user gamit ang profileId
    Optional<User> findByProfileId(@Param("profileId") long profileId);  //para sa pagkuha sa user gamit ang profileId
}
