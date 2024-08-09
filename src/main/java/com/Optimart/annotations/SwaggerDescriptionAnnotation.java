package com.Optimart.annotations;

import com.Optimart.responses.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "",
        description = "",
        tags = { "" },
        responses = {@ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                        schema = @Schema(implementation = Object.class),
                        mediaType = "application/json"
                )
        ),
                @ApiResponse(
                        responseCode = "400",
                        description = "INVALID"
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "FORBIDDEN"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "NOT_FOUND",
                        content = @Content(
                                schema = @Schema(implementation = Object.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "ALREADY_EXIST",
                        content = @Content(
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "INTERNAL_SERVER_ERROR"
                )}

)
public @interface SwaggerDescriptionAnnotation {
    String summary() default "";
    String description() default "";
    String[] tags() default {};
    Class<?> implementation() default Object.class;
    Class<?> response200() default Object.class;
}
