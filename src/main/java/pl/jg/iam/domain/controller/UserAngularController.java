package pl.jg.iam.domain.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jg.iam.domain.model.dto.UserDto;
import pl.jg.iam.domain.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class UserAngularController {

    private final UserService userService;

    @GetMapping("/users/test")
    public List<UserDto> getAllUsersTest() {
        return userService.getAllUsers();
    }

    @PostMapping("/users/test")
    public UserDto saveUserTest(@RequestBody UserDto userDto) {
        return userService.saveUserDto(userDto);
    }

    @DeleteMapping("/users/test/{id}")
    public void deleteUserTest(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
    }
}
