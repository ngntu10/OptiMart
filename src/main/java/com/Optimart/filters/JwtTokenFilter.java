package com.Optimart.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
    }

    @Value("${server.servlet.contextPath}")
    private String apiPrefix;
    private boolean isByPassToken(@NonNull HttpServletRequest request){
        final List<Pair<String,String>> byPassToken = Arrays.asList(
                Pair.of(String.format("%s/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST")
        );
        for(Pair<String,String> bypasstoken: byPassToken) {
           if (request.getServletPath().contains(bypasstoken.getFirst()) &&
                   request.getServletPath().contains(bypasstoken.getSecond())){
               return true;
           }
        }
        return false;
    }
}
