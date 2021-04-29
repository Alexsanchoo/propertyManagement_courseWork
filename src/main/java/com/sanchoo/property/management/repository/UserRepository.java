package com.sanchoo.property.management.repository;


import com.sanchoo.property.management.entity.Role;
import com.sanchoo.property.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String userName);
	List<User> findByActive(boolean active);
	List<User> findByRolesContains(Role role);
}
