package com.Optimart.configuration.Security;
import com.Optimart.constants.MessageKeys;
import com.Optimart.services.RefreshToken.RefreshTokenService;
import com.Optimart.utils.LocalizationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.core.Authentication;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final LocalizationUtils localizationUtils;
    private final RefreshTokenService refreshTokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken != null) {
            refreshToken = refreshToken.replace("Bearer ", "");
            refreshTokenService.removeTokenFromUser(refreshToken);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(localizationUtils.getLocalizedMessage(MessageKeys.LOGOUT_SUCCESS));
    }
}

