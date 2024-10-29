package com.Optimart.controllers;

import com.Optimart.annotations.UnsecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Comment.AddCommentDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Comment", description = "Everything about comment")
@RequestMapping(Endpoint.Comment.BASE)
public class CommentController {

    @ApiResponse(responseCode = "201", description = "SUCCESS OPERATION", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Write new comment for product")
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody AddCommentDTO addCommentDTO) {
        try {

            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
