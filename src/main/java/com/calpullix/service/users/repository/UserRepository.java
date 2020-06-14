package com.calpullix.service.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calpullix.service.users.model.Employee;
import com.calpullix.service.users.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>  {

	Optional<Users> findByIdemployee(Employee idEmployee);
	
}
