package com.mlv.dreamshop.Data;

import com.mlv.dreamshop.DAO.RoleRepository;
import com.mlv.dreamshop.DAO.UserRepository;
import com.mlv.dreamshop.Model.Role;
import com.mlv.dreamshop.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        createDefaultUserIfNotExist();
        createDefaultRoleIfNotExist(defaultRoles);
        createDefaultAdminIfNotExist();
    }

    // default user
    public void createDefaultUserIfNotExist() {
        Role role = roleRepository.findByName("ROLE_USER");
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue; // nếu tìm thấy email tượng tự sẽ bỏ qua
            }
            User user = new User();

            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(role));

            userRepository.save(user);

            System.out.println("Default vet user " + i + " created successfully");



        }
    }

    // default admin
    public void createDefaultAdminIfNotExist() {
        Role role = roleRepository.findByName("ROLE_ADMIN");
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue; // nếu tìm thấy email tượng tự sẽ bỏ qua
            }
            User user = new User();

            user.setFirstName("The Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(role));

            userRepository.save(user);

            System.out.println("Default admin  " + i + " created successfully");



        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    public void createDefaultRoleIfNotExist(Set<String> roles) {
        // TODO Auto-generated method stub
         roles.stream()
                 .filter(role -> !roleRepository.existsByName(role))
                 .map(Role::new).forEach(roleRepository::save);
    }
}
