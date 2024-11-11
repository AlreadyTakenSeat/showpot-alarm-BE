package org.example.dto.response;

import java.util.List;
import lombok.Builder;

public record PaginationApiResponse<T>(
    int size,
    boolean hasNext,
    List<T> data,
    CursorApiResponse cursor
) {

    @Builder
    public PaginationApiResponse(
        List<T> data,
        boolean hasNext,
        CursorApiResponse cursor
    ) {
        this(data.size(), hasNext, data, cursor);
    }
}
