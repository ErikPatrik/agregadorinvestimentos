package tech.buildrun.agregadorinvestimentos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.buildrun.agregadorinvestimentos.controller.CreateUserDTO;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

// Triple way: 
// 1 - Arrange (arrumar tudo o que precisa pro teste fazer);
// 2 - Act (chama o trecho que queremos testar);
// 3 - Assert (faz todas as verificacoes se chamou corretamente com os parametros corretos)

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  // Indica a dependencia do nosso service
  @Mock
  private UserRepository userRepository;

  // Indicamos aqui a classe que vamos criar, indicando para o Mockito para criar uma instancia da nossa classe de Service, injetando os mocks que usamos, no caso o UserRepository
  @InjectMocks
  private UserService userService;

  @Captor
  private ArgumentCaptor<User> userArgumentCaptor; // captura o argumento passado dentro um parametro/dentro de um método, recebe um Generics que é o tipo que eu capturar

  @Captor
  private ArgumentCaptor<UUID> uuidArgumentCaptor; // captura o argumento passado dentro um parametro/dentro de um método, recebe um Generics que é o tipo que eu captur

  // definimos uma subclasse no teste para organizar melhor os tes unitarios
  @Nested
  class createUser {
    
    @Test
    @DisplayName("Should create a user with success") // fica mais bonito
    void shoudCreateAUserWithSuccess() {
      // Arrange
      var user = new User(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        "password",
        Instant.now(),
        null
      );
      doReturn(user).when(userRepository).save(userArgumentCaptor.capture()); // indica para retornar algo quando a gente utiliza
      // Ou seja, quando o userRepository chamar o método save(), vai retornar o objeto que esta no doReturn


      var input = new CreateUserDTO("username", "email@email.com" , "123");

      // Act
      var output = userService.createUser(input);

      // Assert
      assertNotNull(output);

      var userCaptured = userArgumentCaptor.getValue();
      assertEquals(input.username(), userCaptured.getUsername());
      assertEquals(input.email(), userCaptured.getEmail());
      assertEquals(input.password(), userCaptured.getPassword());
    }

    @Test
    @DisplayName("Should throw excepetion when error occurs") // fica mais bonito
    void shouldThrowExceptionWhenErrorOccurs() {
      // Arrange
      doThrow(new RuntimeException()).when(userRepository).save(any()); // aqui, queremos que seja jogado uma exceçao
      // Ou seja, quando o userRepository chamar o método save(), vai retornar o objeto que esta no doReturn

      var input = new CreateUserDTO("username", "email@email.com" , "123");

      // Act & Assert
      assertThrows(RuntimeException.class, () -> userService.createUser(input)); // aqui indicamos que a RUnTime quando executar o metodo da service
    }
  }

  @Nested
  class getUserById {

    @Test
    @DisplayName("Should get a user by id with success when optional is present")
    void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
      //Arrange -- o que vai retornar
      var user = new User(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        "password",
        Instant.now(),
        null
      );
      doReturn(Optional.of(user))
        .when(userRepository)
        .findById(uuidArgumentCaptor.capture());

      //Act
      var output = userService.getUserById(user.getUserId().toString());

      //Assert
      assertTrue(output.isPresent()); // verifica se o usuario está presente
      assertEquals(user.getUserId(), uuidArgumentCaptor.getValue()); // user que foi passado no metodo é o mesmo que foi passado no findById
    }

    @Test
    @DisplayName("Should get a user by id with success when optional is empty")
    void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
      //Arrange -- o que vai retornar
      var userId = UUID.randomUUID();
      doReturn(Optional.empty())
        .when(userRepository)
        .findById(uuidArgumentCaptor.capture());
  
      //Act
      var output = userService.getUserById(userId.toString());
  
      //Assert
      assertTrue(output.isEmpty());
      assertEquals(userId, uuidArgumentCaptor.getValue());
    }
  }

  @Nested
  class listUsers {

    @Test
    @DisplayName("Should all users with success")
    void shouldReturnAllUsersWithSuccess() {
      //Arrange
      var user = new User(
        UUID.randomUUID(),
        "username",
        "email@email.com",
        "password",
        Instant.now(),
        null
      );
      var userList = List.of(user);

      doReturn(userList)
        .when(userRepository)
        .findAll();

      //Act
      var output = userService.listUsers();

      //Assert
      assertNotNull(output);
      assertEquals(userList.size(), output.size()); // verifica se minha lista tem o mesmo tamanho
    }
  }

  @Nested
  class deleteById {
    
    @Test
    @DisplayName("Should Delete with success")
    void shouldDeleteUserWithSuccess() {
      // Arrange
      doReturn(true)
        .when(userRepository)
        .existsById(uuidArgumentCaptor.capture()); // mockamos a funcao que busca um usuario por ID

      // quando um método é void, mockamos com o doNothing()
      doNothing()
        .when(userRepository)
        .deleteById(uuidArgumentCaptor.capture());

      var userId = UUID.randomUUID();
    
      //Act
      userService.deleteUserById(userId.toString()); // nao tem output porque o método é void

      //Assert
      var idList = uuidArgumentCaptor.getAllValues(); // aqui pegamos as ordens do uuidArgumentCaptor quando foi chamado assim, e embaixo, usamos índices para garantir que estao sendo chamados em ordem
      assertEquals(userId, idList.get(0));
      assertEquals(userId, idList.get(1));

      // Verifica se os metodos estao sendo chamados
      verify(userRepository, times(1)).existsById(idList.get(0));
      verify(userRepository, times(1)).deleteById(idList.get(1));
    }

    @Test
    @DisplayName("Should Not Delete When User Does Not Exist")
    void shouldNotDeleteWhenUserDoesNotExist() {
      doReturn(false) // Simula que o usuário não existe no banco
        .when(userRepository)
        .existsById(uuidArgumentCaptor.capture());

      var userId = UUID.randomUUID();

      // Act
      userService.deleteUserById(userId.toString());

      // Assert
      assertEquals(userId, uuidArgumentCaptor.getValue());

      verify(userRepository, times(1)).existsById(userId); // Deve chamar existsById
      verify(userRepository, never()).deleteById(any()); // Nunca deve chamar deleteById
      verify(userRepository, times(0)).deleteById(any()); // Nunca deve chamar deleteById
}
  }
}
