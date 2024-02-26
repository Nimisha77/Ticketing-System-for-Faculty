package TSFSBackend.service;
import TSFSBackend.model.User;
import TSFSBackend.repository.UserRepository;
import TSFSBackend.utils.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.security.MessageDigest;



@Service
public class JwtUserDetailsService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    @Autowired
    UserRepository userRepository;
    // ...

    @Override
    public UserDetails loadUserByUsername(final String username) {
//        final Client client = userRepository.f(username).orElseThrow(
//                () -> new UsernameNotFoundException("User " + username + " not found"));

        final User user = userRepository.findUserByGtUsername(username);
        if(user == null) {
            return null;
        }
        long id = hashStringToLong(user.getGtUsername());
        System.out.println("id of username: "+ username + " is: " + id);
//        return new JwtUserDetails("129424", username, user.hashCode(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
        return new JwtUserDetails(id, user.getGtUsername(), "",Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
    }

    public static long hashStringToLong(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte[] hashBytes = md.digest();
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash <<= 8;
                hash |= (hashBytes[i] & 0xFF);
            }
            return hash;
        } catch (Exception e) {
            System.out.println("Error generating hash: " + e.getMessage());
            return 0; // Return a default value if an error occurs
        }
    }

}