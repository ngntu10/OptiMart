package com.Optimart.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
        responses = {
                @ApiResponse(
                        responseCode = "400",
                        description = "BAD_REQUEST",
                        content = @Content(
                                schema = @Schema()
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "UNAUTHORIZED",
                        content = @Content(
                                schema = @Schema()
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "NOT_FOUND",
                        content = @Content(
                                schema = @Schema()
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "ALREADY_EXIST",
                        content = @Content(
                                schema = @Schema()
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "INTERNAL_SERVER_ERROR",
                        content = @Content(
                                schema = @Schema()
                        )
                )}

)
public @interface UnsecuredSwaggerOperation {
    String summary() default "";
    String description() default "";
    String[] tags() default {};
    Class<?> implementation() default Object.class;
}
