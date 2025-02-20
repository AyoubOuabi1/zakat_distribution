package org.zakat.distribution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zakat.distribution.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u, " +
            "COALESCE(SUM(d.amount), 0) AS totalDonated, " +
            "COALESCE(SUM(z.amountReceived), 0) AS totalReceived " +
            "FROM User u " +
            "LEFT JOIN u.donationsHistory d " +
            "LEFT JOIN u.zakatHistory z " +
            "GROUP BY u.id")
    List<Object[]> findAllUsersWithTotals();
    boolean existsByEmail(String email);
    @Modifying
    @Query("UPDATE User u SET u.id = u.id WHERE u.id = :id")
    void detach(User user);
}
