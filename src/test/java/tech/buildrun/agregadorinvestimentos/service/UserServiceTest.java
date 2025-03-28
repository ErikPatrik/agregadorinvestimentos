package tech.buildrun.agregadorinvestimentos.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
      doReturn(user).when(userRepository).save(any()); // indica para retornar algo quando a gente utiliza
      // Ou seja, quando o userRepository chamar o método save(), vai retornar o objeto que esta no doReturn


      var input = new CreateUserDTO("username", "email@email.com" , "123");

      // Act
      var output = userService.createUser(input);

      // Assert
      assertNotNull(output);
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
}
