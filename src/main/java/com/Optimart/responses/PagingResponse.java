package com.Optimart.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {
    private T data;
    String message;
    private int totalPage;
    private Long totalCount;
}
