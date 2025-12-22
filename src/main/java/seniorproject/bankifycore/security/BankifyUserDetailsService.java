package seniorproject.bankifycore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.User;
import seniorproject.bankifycore.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BankifyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException("User not found : "+ email));
       return new BankifyUserDetails(user);
    }
}
