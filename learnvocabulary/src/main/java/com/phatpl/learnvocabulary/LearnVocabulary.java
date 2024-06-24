package com.phatpl.learnvocabulary;


import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class LearnVocabulary {
    public static void main(String[] args) {
        SpringApplication.run(LearnVocabulary.class, args);
    }


    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin123").isEmpty()) {
                User user = new User();
                user.setUsername("admin123");
                user.setPassword(BCryptPassword.encode("Admin@123"));
                user.setIsAdmin(true);
                user.setCode(0);
                user.setActivated(true);
                user.setEmail("admin123@gmail.com");
                userRepository.save(user);
            }
        };
    }
}
