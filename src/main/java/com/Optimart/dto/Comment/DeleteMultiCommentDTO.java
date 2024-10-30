package com.Optimart.dto.Comment;

import lombok.Data;

import java.util.List;

@Data
public class DeleteMultiCommentDTO {
    private List<String> commentIds;
}
