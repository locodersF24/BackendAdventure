package org.example.backendadventure.repository;

import org.example.backendadventure.model.LoginProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginProfileRepository extends JpaRepository<LoginProfile, Integer> {
}
