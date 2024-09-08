package com.Optimart.responses.User;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingUserResponse<T> {
    private T data;
    String message;
    private int totalPage;
    private Long totalCount;
}
