package com.Optimart.services.RefreshToken;

import com.Optimart.exceptions.TokenRefreshException;
import com.Optimart.models.RefreshToken;
import com.Optimart.models.User;
import com.Optimart.repositories.RefreshTokenRepository;
import com.Optimart.repositories.AuthRepository;
import com.Optimart.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwt.jwtRefreshExpiration}")
    private Long jwtRefreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthRepository authRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshtoken(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String userEmail) {
        try {
            User user = authRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .expiryDate(new Date(System.currentTimeMillis()+jwtRefreshExpiration*1000L))
                    .refreshtoken(jwtTokenUtil.generateRefreshToken(authRepository.findByEmail(userEmail).get()))
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }catch (Exception ex){
            throw new RuntimeException("Error creating refresh token", ex);
        }
    }

    @Override
    public boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getRefreshtoken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    @Override
    @Transactional
    public void deleteByUserId(String userEmail) {
        if(!authRepository.findByEmail(userEmail).isEmpty()) {
            refreshTokenRepository.deleteByUser(authRepository.findByEmail(userEmail).get());
        }
    }

}
