package com.golab.talk.repository;

import com.golab.talk.domain.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    boolean existsByUserIdAndPlatformType(String userID,int platformType);
    boolean existsByPlatformType(int platformType);

    SocialLogin findBySocialEmail(String email);
}
