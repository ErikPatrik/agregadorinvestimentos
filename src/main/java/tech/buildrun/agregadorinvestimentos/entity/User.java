package tech.buildrun.agregadorinvestimentos.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "tb_users")
public class User {
  @Version
  private Long version;

  @Id
  @Column(name = "user_id", updatable = false, nullable = false)
  private UUID user_id;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @CreationTimestamp
  private Instant createTimestamp;

  @UpdateTimestamp
  private Instant updateTimestamp;

  public User() {}

  public User(UUID user_id, String username, String email, String password, Instant createTimestamp, Instant updateTimestamp) {
    this.user_id = user_id != null ? user_id : UUID.randomUUID();
    this.username = username;
    this.email = email;
    this.password = password;
    this.createTimestamp = createTimestamp;
    this.updateTimestamp = updateTimestamp;
  }

  public UUID getUserId() {
    return user_id;
  }

  public void setUserId(UUID id) {
    this.user_id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Instant getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Instant createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public Instant getUpdateTimestamp() {
    return updateTimestamp;
  }

  public void setUpdateTimestamp(Instant updateTimestamp) {
    this.updateTimestamp = updateTimestamp;
  }
}
