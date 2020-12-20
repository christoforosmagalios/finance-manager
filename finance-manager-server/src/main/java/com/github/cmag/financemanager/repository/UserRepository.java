package com.github.cmag.financemanager.repository;

import com.github.cmag.financemanager.model.User;
import org.springframework.stereotype.Repository;

/**
 * The User Repository.
 */
@Repository
public interface UserRepository extends BaseRepository<User> {

  /**
   * Find the User by the given username or the given email.
   *
   * @param username The username.
   * @param email The email.
   * @return The User.
   */
  User findByUsernameOrEmail(String username, String email);

  /**
   * Find the User with the given username.
   *
   * @param username The username.
   * @return The User.
   */
  User findByUsername(String username);

  /**
   * Find the User with the given email.
   *
   * @param email The email.
   * @return The User.
   */
  User findByEmail(String email);
}
