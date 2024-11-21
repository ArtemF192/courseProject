package ru.urfu.courseProject.service;

import ru.urfu.courseProject.dto.UserDto;
import ru.urfu.courseProject.entity.User;

import java.util.List;

public interface UserService {

    void saveUserRole(UserDto userDTO);
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();

    UserDto mapToUserDto(User user);
}