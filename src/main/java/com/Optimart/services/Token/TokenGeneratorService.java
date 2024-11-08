package com.Optimart.services.Token;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.User;
import com.Optimart.repositories.UserRepository;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenGeneratorService {
    @Value("${time.expire.password}")
    private long expireTimeInSeconds;

    private final UserRepository userRepository;
    private final LocalizationUtils localizationUtils;
    private final SecureRandom random = new SecureRandom();

    public String generateResetToken(String email) {
        String token = generateRandomString() + generateRandomString();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        user.setResetToken(token);
        Date expirationDate = Date.from(Instant.now().plusSeconds(expireTimeInSeconds));
        user.setResetTokenExpiration(expirationDate);
        userRepository.save(user);
        return token;
    }

    private String generateRandomString() {
        String randomString = Long.toString(Math.abs(random.nextLong()), 36);
        if (randomString.length() >= 12) {
            return randomString.substring(0, 12);
        } else {
            StringBuilder sb = new StringBuilder(randomString);
            while (sb.length() < 12) {
                sb.append(Long.toString(Math.abs(random.nextLong()), 36));
            }
            return sb.substring(0, 12);
        }

    }
}