package com.Optimart.annotations;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "",
        description = "",
        tags = { "" }
)
@ApiResponses({
        @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema()))
})
public @interface SwaggerDescriptionAnnotation {

    String summary() default "Retrieve an entity by Id";

    String description() default "Get an entity object by specifying its id.";

    String[] tags() default {};

    Class<?> implementation() default Object.class;
}
