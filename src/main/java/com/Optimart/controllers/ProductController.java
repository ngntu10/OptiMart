package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RequiredArgsConstructor
@RestController
@Tag(name = "Product", description = "Everything about product")
@RequestMapping(Endpoint.Product.BASE)
public class ProductController {

}
