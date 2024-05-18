package com.example.videohosting.repository;

import com.example.videohosting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT count(s.id_user_channel) FROM subscription s WHERE id_user_channel = :idUser", nativeQuery = true)
    Long getSubscriptionsCountByIdUser(Long idUser);
    User getUserByEmail(String email);


}
