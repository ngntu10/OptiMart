package com.Optimart.services.Comment;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.Comment.AddCommentDTO;
import com.Optimart.dto.Comment.DeleteMultiCommentDTO;
import com.Optimart.dto.Comment.ReplyCommentDTO;
import com.Optimart.dto.Comment.UpdateCommentDTO;
import com.Optimart.dto.Product.CommentProductDTO;
import com.Optimart.dto.User.UserCommentDTO;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.Comment;
import com.Optimart.models.Product;
import com.Optimart.models.User;
import com.Optimart.repositories.CommentRepository;
import com.Optimart.repositories.ProductRepository;
import com.Optimart.repositories.Specification.CommentSpecification;
import com.Optimart.repositories.UserRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.CommentResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final LocalizationUtils localizationUtils;

    @Override
    @Transactional
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
    @Transactional
    public APIResponse<Comment> replyComment(ReplyCommentDTO replyCommentDTO) {
        User user = userRepository.findById(UUID.fromString(replyCommentDTO.getUser()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST)));
        Product product = productRepository.findById(UUID.fromString(replyCommentDTO.getProduct()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        Comment parentComment = commentRepository.findById(UUID.fromString(replyCommentDTO.getParent()))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        Comment comment = Comment.builder()
                .content(replyCommentDTO.getContent()).user(user)
                .parent(parentComment).product(product).build();
        commentRepository.save(comment);
        parentComment.getReplies().add(comment);
        return new APIResponse<>(comment, localizationUtils.getLocalizedMessage(MessageKeys.REPLY_COMMENT_SUCCESS));
    }

    @Override
    @Transactional
    public Comment updateComment(UpdateCommentDTO updateCommentDTO, String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        comment.setContent(updateCommentDTO.getContent());
        commentRepository.save(comment);
        return comment;
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        if(comment.getParent() == null){
            commentRepository.deleteAll(comment.getReplies());
        } else {
            Comment parentComment = comment.getParent();
            parentComment.getReplies().remove(comment);
        }
        commentRepository.delete(comment);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Comment> getOneComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.COMMENT_NOT_FOUND)));
        return new APIResponse<>(comment, localizationUtils.getLocalizedMessage(MessageKeys.GET_COMMENT_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMultiComment(DeleteMultiCommentDTO deleteMultiCommentDTO) {
        deleteMultiCommentDTO.getCommentIds().forEach(this::deleteComment);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESS));
    }

    @Override
    public PagingResponse<List<CommentResponse>> getAllCommentPublic(Map<Object, String> filters) {
        Product product = productRepository.findBySlug(filters.get("productId"))
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.PRODUCT_NOT_EXISTED)));
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            List<Comment> commentList = commentRepository.findAll().stream()
                    .filter(item -> item.getProduct().getId() == product.getId() && item.getParent()==null).toList();
            List<CommentResponse> commentResponseList = commentList.stream().map(
                    this::toCommentResponse
            ).toList();
            return new PagingResponse<>(commentResponseList, localizationUtils.getLocalizedMessage(MessageKeys.GET_LIST_COMMENT_SUCCESS), 1, (long) commentList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Comment> specification = CommentSpecification.filterCommentByProduct(filters.get("productId"));
        Page<Comment> commentPage = commentRepository.findAll(specification, pageable);
        List<CommentResponse> commentResponseList = commentPage.getContent().stream().filter(item -> item.getParent()==null).map(
                this::toCommentResponse
        ).toList();
        return new PagingResponse<>(commentResponseList, localizationUtils.getLocalizedMessage(MessageKeys.GET_LIST_COMMENT_SUCCESS), commentPage.getTotalPages(), commentPage.getTotalElements());
    }

    @Override
    public PagingResponse<List<CommentResponse>> getAllComment(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit", "-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1 ) {
            List<Comment> commentList = commentRepository.findAll();
            List<CommentResponse> commentResponseList = commentList.stream().map(
                    this::toCommentResponse
            ).toList();
            return new PagingResponse<>(commentResponseList, localizationUtils.getLocalizedMessage(MessageKeys.GET_LIST_COMMENT_SUCCESS), 1, (long) commentList.size());
        }
        page = Math.max(Integer.parseInt(filters.getOrDefault("page", "-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        pageable = getPageable(pageable, page, limit, order);
        Specification<Comment> specification = CommentSpecification.filterCommentByKeyWords(filters.get("search"));
        Page<Comment> commentPage = commentRepository.findAll(specification, pageable);
        List<CommentResponse> commentResponseList = commentPage.getContent().stream().map(
                this::toCommentResponse
        ).toList();
        return new PagingResponse<>(commentResponseList, localizationUtils.getLocalizedMessage(MessageKeys.GET_LIST_COMMENT_SUCCESS), commentPage.getTotalPages(), commentPage.getTotalElements());
    }

    private Pageable getPageable(Pageable pageable, int page, int limit, String order) {
        if (StringUtils.hasText(order)) {
            String[] orderParams = order.split(" ");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        return pageable;
    }

    CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId().toString())
                .user(new UserCommentDTO(
                        comment.getUser().getFirstName(),
                        comment.getUser().getLastName(),
                        comment.getUser().getMiddleName(),
                        comment.getUser().getImageUrl(),
                        comment.getUser().getId().toString()
                ))
                .parent(comment.getParent() != null ? comment.getParent().getId().toString() : null)
                .product(new CommentProductDTO(
                        comment.getProduct().getId().toString(),
                        comment.getProduct().getName()
                ))
                .replies(comment.getReplies().stream()
                        .map(this::toCommentResponse)
                        .collect(Collectors.toList()))
                .content(comment.getContent())
                .createAt(comment.getCreatedAt())
                .build();
    }
}
