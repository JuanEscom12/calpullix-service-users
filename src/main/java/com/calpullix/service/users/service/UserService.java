package com.calpullix.service.users.service;

import com.calpullix.service.users.model.RegisterUserRequest;
import com.calpullix.service.users.model.RegisterUserResponse;

public interface UserService {
	
	RegisterUserResponse saveRegister(RegisterUserRequest request) throws Exception;

}
