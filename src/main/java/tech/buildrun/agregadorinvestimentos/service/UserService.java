package tech.buildrun.agregadorinvestimentos.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.buildrun.agregadorinvestimentos.controller.CreateUserDTO;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

// o que vem do controller para o que vai para o repository
@Service // annotation que indica para o Spring que uma classe de regras de negócio, onde consegue fazer o a injecao de dependencia
public class UserService {

  private UserRepository userRepository;

  // Quando o Spring criar o serviço UserService ele verifica que precisamos da implementacao do UserRepository
  // sendo assim, o Spring procura no projeto quem possui a implementacao dessa Interface, e assim que achar
  // é injetado dentro do projeto
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UUID createUser(CreateUserDTO createUserDTO) {
    // injetando dependencia, dizendo que precisamos de um reposuitory, sem saber o que é
    // Converte antes o DTO para Entity
    var entity = new User(
      UUID.randomUUID(),
      createUserDTO.username(), 
      createUserDTO.email(), 
      createUserDTO.password(),
      Instant.now(),
      null
    );

    var userSaved = userRepository.save(entity);

    return userSaved.getUserId();
  }
}
