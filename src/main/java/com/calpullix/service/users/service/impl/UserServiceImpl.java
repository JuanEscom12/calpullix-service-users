package com.calpullix.service.users.service.impl;

import java.util.Optional;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.service.users.model.Employee;
import com.calpullix.service.users.model.RegisterUserRequest;
import com.calpullix.service.users.model.RegisterUserResponse;
import com.calpullix.service.users.model.Users;
import com.calpullix.service.users.repository.EmployeeRepository;
import com.calpullix.service.users.repository.UserRepository;
import com.calpullix.service.users.service.AsyncEmail;
import com.calpullix.service.users.service.UserService;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AsyncEmail asyncEmail;

	@Override
	@Timed(value = "calpullix.service.users.metrics", description = "Restart password operation")
	public RegisterUserResponse saveRegister(RegisterUserRequest request) throws Exception {
		log.info(":: User register service {} ", request);
		final RegisterUserResponse result = new RegisterUserResponse();
		final Employee idEmployee = new Employee();
		idEmployee.setId(request.getId());
		final Optional<Users> userEmployee = userRepository.findByIdemployee(idEmployee);
		if (userEmployee.isPresent()) {
			log.info(":: Ya existe un usuario con el mismo Id ");
			result.setIsValid(Boolean.FALSE);
		} else {
			final Users user = new Users();
			user.setCellphone(request.getPhone());
			user.setEmail(request.getEmail());
			final Optional<Employee> employee = employeeRepository.findById(request.getId());
			log.info(":: Employee {} ", employee);
			if (employee.isPresent()) {
				result.setIsValid(Boolean.TRUE);
				user.setIdemployee(employee.get());
				user.setName(request.getName());
				RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45).build();
				String password = pwdGenerator.generate(10);
				user.setPassword(password);
				try {
					userRepository.save(user);
				} catch (Exception e) {
					log.error(":: User error {} ", user);
					result.setIsValid(Boolean.FALSE);
				}
				log.info(":: Sending user register email ");
				asyncEmail.sendEmail(password, request.getName(), request.getEmail());
				log.info(":: Ends up register service {} ", user);
			} else {
				result.setIsValid(Boolean.FALSE);

			}
		}
		return result;
	}


}
