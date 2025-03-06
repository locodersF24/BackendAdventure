package org.example.backendadventure.repository.loginrepo;

import org.apache.juli.logging.Log;
import org.example.backendadventure.model.login.LoginProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginProfileRepository extends JpaRepository<LoginProfile, Integer> {

}
