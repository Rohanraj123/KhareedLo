package org.khareedlo.admin.user;

import org.junit.jupiter.api.Test;
import org.khareedlo.common.entity.Role;
import static org.assertj.core.api.Assertions.assertThat;
import org.khareedlo.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    /*
    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userRohan = new User(
                "rajrohan88293@gmail.com",
                "suidyfe8r78",
                "Rohan",
                "Raj");

        userRohan.addRole(roleAdmin);

        User savedUser = userRepository.save(userRohan);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userRohan = new User("rraj882993@gmail.com", "skjdf", "Raj", "Rohan");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRohan.addRole(roleEditor);
        userRohan.addRole(roleAssistant);

        User savedUser = userRepository.save(userRohan);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userRohan = userRepository.findById(1).get();
        System.out.println(userRohan);
        assertThat(userRohan).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userRohan = userRepository.findById(1).get();
        userRohan.setEnabled(true);
        userRohan.setEmail("shivamraj@gmail.com");

        userRepository.save(userRohan);
    }


     */

    /*
    @Test
    public void testUpdateUserRole() {
        User userRohan = userRepository.findById(1).get();
        Role roleEditor = new Role(5);
        Role roleSalesperson = new Role(3);

        userRohan.getRoles().remove(roleEditor);
        userRohan.getRoles().remove(roleEditor);

        userRohan.addRole(roleSalesperson);

        userRepository.save(userRohan);
    }

     */

    /*
    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

     */

    /*
    @Test
    public void testGetUserByEmail() {
        String email = "rajrohan88293@gmail.com";
        User user = userRepository.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountUserById() {
        Integer id = 100;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void DisableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void EnableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, true);
    }

     */

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(4);
    }
}
