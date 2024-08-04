package com.Optimart.services.User;

import com.Optimart.dto.UserDTO;
import com.Optimart.enums.RoleNameEnum;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.repositories.RoleRepository;
import com.Optimart.repositories.UserRepository;
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
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String email = userDTO.getMail();
        if (userRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException("Email already exists");
        }

        // CONVERT DTO => ENTITY
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getMail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .status(1)
                .build();

        List<Role> roles = new ArrayList<>() ;
        roles.add(roleRepository.findByName(RoleNameEnum.ADMIN.getValue()));
        newUser.setRoleList(roles);

        // Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
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
        return null;
    }
}
