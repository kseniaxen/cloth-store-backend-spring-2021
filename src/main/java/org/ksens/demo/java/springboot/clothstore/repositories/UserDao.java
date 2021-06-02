package org.ksens.demo.java.springboot.clothstore.repositories;

import org.ksens.demo.java.springboot.clothstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findUserByName(String name);
}