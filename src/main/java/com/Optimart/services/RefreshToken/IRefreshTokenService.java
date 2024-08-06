package com.Optimart.services.RefreshToken;

import com.Optimart.models.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface IRefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(UUID userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(UUID userId);

    public boolean isRefreshTokenExpired(RefreshToken token);
}
