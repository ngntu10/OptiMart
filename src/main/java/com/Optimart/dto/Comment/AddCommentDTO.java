package com.Optimart.dto.Comment;

import lombok.Data;

@Data
public class AddCommentDTO {
    private String product;
    private String user;
    private String content;
}
