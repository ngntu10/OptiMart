package com.Optimart.services.Comment;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Comment.AddCommentDTO;
import com.Optimart.dto.Comment.DeleteMultiCommentDTO;
import com.Optimart.dto.Comment.UpdateCommentDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Comment;
import com.Optimart.models.Product;
import com.Optimart.models.User;
import com.Optimart.repositories.CommentRepository;
import com.Optimart.repositories.ProductRepository;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final LocalizationUtils localizationUtils;

    @Override
    public Comment createComment(AddCommentDTO addCommentDTO) {
        User user = userRepository.findById(UUID.fromString(addCommentDTO.getUser()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        Product product = productRepository.findById(UUID.fromString(addCommentDTO.getProduct()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        Comment comment = Comment.builder()
                .content(addCommentDTO.getContent())
                .user(user)
                .product(product)
                .build();
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment updateComment(UpdateCommentDTO updateCommentDTO, String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        comment.setContent(updateCommentDTO.getContent());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public APIResponse<Boolean> deleteComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        commentRepository.delete(comment);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESS));
    }

    @Override
    public APIResponse<Comment> getOneComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        return new APIResponse<>(comment, localizationUtils.getLocalizedMessage(MessageKeys.GET_COMMENT_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteMultiComment(DeleteMultiCommentDTO deleteMultiCommentDTO) {
        List<String> comments = deleteMultiCommentDTO.getCommentIds();
        comments.forEach(item -> {
            commentRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESS));
    }

    @Override
    public PagingResponse<Comment> getAllComment(Map<Object, String> filters) {
        return null;
    }
}
