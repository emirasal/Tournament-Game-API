package com.dreamgamescasestudy.rest.Repo;

import com.dreamgamescasestudy.rest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends  JpaRepository<User, Long> {
}
