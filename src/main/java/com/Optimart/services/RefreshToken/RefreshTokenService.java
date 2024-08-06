package com.Optimart.services.RefreshToken;

import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.User;
import com.Optimart.repositories.RefreshTokenRepository;
import com.Optimart.repositories.UserRepository;
import com.Optimart.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwt.jwtRefreshExpiration}")
    private Long jwtRefreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshtoken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .expiryDate(new Date(System.currentTimeMillis()+jwtRefreshExpiration*1000L))
                    .refreshtoken(jwtTokenUtil.generateToken(userRepository.findByEmail(userEmail).get()))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }catch (Exception ex){
            throw new RuntimeException("Error creating refresh token", ex);
        }
    }

    @Override
    public boolean verifyExpiration(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    @Override
    @Transactional
    public int deleteByUserId(UUID userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
