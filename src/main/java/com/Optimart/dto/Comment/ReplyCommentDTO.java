package com.Optimart.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDTO {
    private String product;
    private String user;
    private String content;
    private String parent;
}
