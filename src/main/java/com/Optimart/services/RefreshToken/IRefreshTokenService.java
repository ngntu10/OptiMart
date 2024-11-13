package com.Optimart.services.RefreshToken;

import com.Optimart.models.RefreshToken;
import com.Optimart.responses.APIResponse;

import java.util.Optional;
import java.util.UUID;

public interface IRefreshTokenService {
     Optional<RefreshToken> findByToken(String token);
     RefreshToken createRefreshToken(String userEmail);
     RefreshToken verifyExpiration(RefreshToken token);
     void deleteByUserId(String userEmail);
     boolean isExpired(RefreshToken token);
     APIResponse<Boolean> removeTokenFromUser(String refreshToken);

}
