package com.sanchoo.property.management.repository;


import com.sanchoo.property.management.entity.Role;
import com.sanchoo.property.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String userName);

	List<User> findByActive(boolean active);

	List<User> findByRolesContainsAndActive(Role role, boolean active);

	@Query("select u " +
			"from User u " +
			"where u.email like %:search% " +
			"or concat(u.firstName, ' ', u.lastName) like %:search% " +
			"or u.userName like %:search%")
	List<User> findBySearch(@Param("search") String search);
}
