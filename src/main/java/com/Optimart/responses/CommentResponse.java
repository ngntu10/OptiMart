package com.Optimart.responses;

import com.Optimart.dto.Product.CommentProductDTO;
import com.Optimart.dto.User.UserCommentDTO;
import com.Optimart.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String id;
    private UserCommentDTO user;
    private String parent;
    private CommentProductDTO product;
    private String content;
    private List<CommentResponse> replies;
    private LocalDateTime createAt;
}
