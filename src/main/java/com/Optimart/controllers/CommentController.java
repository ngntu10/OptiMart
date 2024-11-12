package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.annotations.UnsecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Comment.AddCommentDTO;
import com.Optimart.dto.Comment.DeleteMultiCommentDTO;
import com.Optimart.dto.Comment.ReplyCommentDTO;
import com.Optimart.dto.Comment.UpdateCommentDTO;
import com.Optimart.models.Comment;
import com.Optimart.responses.APIResponse;
import com.Optimart.services.Comment.CommentService;
import com.Optimart.utils.LocalizationUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "Comment", description = "Everything about comment")
@RequestMapping(Endpoint.Comment.BASE)
public class CommentController {
    private final CommentService commentService;
    private final LocalizationUtils localizationUtils;
    @ApiResponse(responseCode = "201", description = "SUCCESS OPERATION", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Write new comment for product")
    @PostMapping
    public ResponseEntity<APIResponse<Comment>> createComment(@RequestBody AddCommentDTO addCommentDTO) {
        try {
            return ResponseEntity.ok(new APIResponse<>(commentService.createComment(addCommentDTO), localizationUtils.getLocalizedMessage(MessageKeys.CREATE_COMMENT_SUCCESS)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User update an existing comment")
    @PutMapping(Endpoint.Comment.ID_ME)
    public ResponseEntity<APIResponse<Comment>> updateMyComment(@RequestBody UpdateCommentDTO updateCommentDTO, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(new APIResponse<>(commentService.updateComment(updateCommentDTO, commentId), localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_COMMENT_SUCCESS)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.COMMENT.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Admin update an existing comment")
    @PutMapping(Endpoint.Comment.ID)
    public ResponseEntity<APIResponse<Comment>> updateComment(@RequestBody UpdateCommentDTO updateCommentDTO, @PathVariable String commentId) {
        try {
            return ResponseEntity.ok(new APIResponse<>(commentService.updateComment(updateCommentDTO, commentId), localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_COMMENT_SUCCESS)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User delete an existing comment")
    @DeleteMapping (Endpoint.Comment.ID_ME)
    public ResponseEntity<APIResponse<Boolean>> deleteMyComment(@PathVariable String commentId) {
        try {
            return ResponseEntity.ok(commentService.deleteComment(commentId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.COMMENT.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Admin delete an existing comment")
    @DeleteMapping (Endpoint.Comment.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteComment(@PathVariable String commentId) {
        try {
            return ResponseEntity.ok(commentService.deleteComment(commentId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_PRODUCT.COMMENT.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete multiple comment")
    @DeleteMapping (Endpoint.Comment.DELETE_MANY)
    public ResponseEntity<APIResponse<Boolean>> deleteMultiComment(@RequestBody DeleteMultiCommentDTO deleteMultiCommentDTO) {
        try {
            return ResponseEntity.ok(commentService.deleteMultiComment(deleteMultiCommentDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get a comment by id")
    @GetMapping (Endpoint.Comment.ID)
    public ResponseEntity<APIResponse<Comment>> getOneComment(@PathVariable String commentId) {
        try {
            return ResponseEntity.ok(commentService.getOneComment(commentId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Get all public comment")
    @GetMapping(Endpoint.Comment.PUBLIC)
    public ResponseEntity<?> getAllCommentPublic(@RequestParam Map<Object, String> filters) {
        try {
            return ResponseEntity.ok(commentService.getAllCommentPublic(filters));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @UnsecuredSwaggerOperation(summary = "Get all comment")
    @GetMapping
    public ResponseEntity<?> getAllComment(@RequestParam Map<Object, String> filters) {
        try {
            return ResponseEntity.ok(commentService.getAllComment(filters));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Reply a comment")
    @PostMapping(Endpoint.Comment.REPLY_COMMENT)
    public ResponseEntity<?> replyComment(@RequestBody ReplyCommentDTO replyCommentDTO) {
        try {
            return ResponseEntity.ok(commentService.replyComment(replyCommentDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }
}
