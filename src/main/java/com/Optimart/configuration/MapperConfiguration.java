package com.Optimart.configuration;
import com.Optimart.models.User;
import com.Optimart.responses.Auth.UserLoginResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(User.class, UserLoginResponse.class)
                .addMappings(mapper -> mapper.map(User::getUsername, UserLoginResponse::setUsername));

        return modelMapper;
    }
}
