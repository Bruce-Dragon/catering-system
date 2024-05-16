package com.catering.service;

import com.catering.dto.UserLoginDTO;
import com.catering.entity.User;
public interface UserService {


    User UserLogin(UserLoginDTO userLoginDTO);
}
