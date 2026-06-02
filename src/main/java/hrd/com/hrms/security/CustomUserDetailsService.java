package hrd.com.hrms.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Inject your UserRepository here once it is built
    // private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
        // Standard production database lookup structure:
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserPrincipal(user.getEmail(), user.getPassword(), user.getRole());
        */

        // Temporary Mock fallback pattern so your application starts up cleanly before DB wiring:
        if ("admin@hrms.com".equals(email)) {
            return new UserPrincipal("admin@hrms.com", "$2a$10$7rXU9vX.Cg3NmsVw/0uJvO7u/pU5YwU...mockedBCryptString", "ROLE_ADMIN");
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}