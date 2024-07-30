package com.phatpl.learnvocabulary.configs;

import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.repositories.UserRepository;
import com.phatpl.learnvocabulary.repositories.WordRepository;
import com.phatpl.learnvocabulary.utils.BCryptPassword;
import com.phatpl.learnvocabulary.utils.CustomUserDetail;
import com.phatpl.learnvocabulary.utils.Trie.BuildTrie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var user = userRepository.findByUsername(username);
            if (user.isEmpty()) throw new UsernameNotFoundException("not found " + username);
            CustomUserDetail userDetail = new CustomUserDetail(user.get());
            return userDetail;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(8));
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
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
            var words = wordRepository.findAll();
            for (var word : words) {
                BuildTrie.addWord(word.getId(), word.getWord());
                BuildTrie.mapWords.put(word.getId(), word);
            }
            BuildTrie.merge(0);
        };
    }

}
