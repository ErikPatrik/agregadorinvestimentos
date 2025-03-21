package tech.buildrun.agregadorinvestimentos.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }
  
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
    var userId = userService.createUser(createUserDTO);
    return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build(); // identificar no header o UUID do usuario criado
  }

  @GetMapping("{userId}")
  public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
    return null;
  }
}
