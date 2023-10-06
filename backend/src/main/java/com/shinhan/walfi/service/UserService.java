package com.shinhan.walfi.service;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.TokenDto;
import com.shinhan.walfi.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUserList();

    TokenDto login(String userId, String password);

    void signup(User user);

    UserDto findUserById(String userId);
}
