package com.Optimart.services.Comment;

import com.Optimart.dto.Comment.AddCommentDTO;
import com.Optimart.dto.Comment.DeleteMultiCommentDTO;
import com.Optimart.dto.Comment.ReplyCommentDTO;
import com.Optimart.dto.Comment.UpdateCommentDTO;
import com.Optimart.models.Comment;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.CommentResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;
import java.util.Map;

public interface ICommentService {
    Comment createComment(AddCommentDTO addCommentDTO);
    APIResponse<Comment>  replyComment(ReplyCommentDTO replyCommentDTO);
    Comment updateComment(UpdateCommentDTO updateCommentDTO, String commentId);
    APIResponse<Boolean> deleteComment(String commentId);
    APIResponse<Comment> getOneComment(String commentId);
    APIResponse<Boolean> deleteMultiComment(DeleteMultiCommentDTO deleteMultiCommentDTO);
    PagingResponse<List<CommentResponse>> getAllCommentPublic(Map<Object, String> filters);
    PagingResponse<List<CommentResponse>> getAllComment(Map<Object, String> filters);
}
