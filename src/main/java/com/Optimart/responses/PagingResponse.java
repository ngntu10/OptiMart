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
    private int totalNew;

    public PagingResponse(T data, String message, int totalPage, Long totalCount) {
        this.data = data;
        this.message = message;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }
}
