package com.Optimart.services.Comment;

import com.Optimart.dto.Comment.AddCommentDTO;
import com.Optimart.models.Comment;

public interface ICommentService {
    Comment createComment(AddCommentDTO addCommentDTO);
}
