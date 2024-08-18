package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "Everything about user")
@RequestMapping(Endpoint.User.BASE)
public class UserController {

}
