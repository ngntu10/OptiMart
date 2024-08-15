package com.Optimart.services.User;

import com.Optimart.dto.Auth.UserRegisterDTO;
import com.Optimart.enums.RoleNameEnum;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.repositories.UserRepository;
import com.Optimart.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    @Transactional
    public User createUser(UserRegisterDTO userRegisterDTO) throws Exception {
        String email = userRegisterDTO.getMail();
        if (userRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException("Email already exists");
        }
        Role userRole = Role.builder()
                .name(RoleNameEnum.ADMIN)
                .permissions(List.of("ADMIN.GRANTED"))
                .build();
        roleRepository.save(userRole);
        // CONVERT DTO => ENTITY
        User newUser = User.builder()
                .email(userRegisterDTO.getMail())
                .password(userRegisterDTO.getPassword())
                .facebookAccountId(userRegisterDTO.getFacebookAccountId())
                .googleAccountId(userRegisterDTO.getGoogleAccountId())
                .status(1)
                .userType(3)
                .fullName(userRegisterDTO.getMail())
                .role(userRole)
                .build();

        userRepository.save(newUser);

        // Kiểm tra nếu có accountId, không yêu cầu password
        if (userRegisterDTO.getFacebookAccountId() == 0 && userRegisterDTO.getGoogleAccountId() == 0) {
            String password = userRegisterDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new DataNotFoundException("Email does not exists");
        }
        User existingUser = user.get();

        // Check password
        if(existingUser.getFacebookAccountId() == 0 &&
              existingUser.getGoogleAccountId() == 0 ) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException("Wrong email or password");
            }
        }
        if(existingUser.getStatus() == 0) {
            throw new DataNotFoundException("This account has been locked");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).get();
    }
}
