package com.Optimart.services.RefreshToken;

import com.Optimart.models.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface IRefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String userEmail);
    public RefreshToken verifyExpiration(RefreshToken token);
    public void deleteByUserId(String userEmail);
    public boolean isExpired(RefreshToken token);

}
