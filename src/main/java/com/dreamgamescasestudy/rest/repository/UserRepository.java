package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
