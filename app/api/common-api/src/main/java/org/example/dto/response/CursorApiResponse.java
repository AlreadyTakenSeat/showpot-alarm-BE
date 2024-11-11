package org.example.dto.response;

import java.util.List;

public record CursorApiResponse(

    Object id,

    Object value
) {

    public static CursorApiResponse toCursorResponse(Object id, Object value) {
        return new CursorApiResponse(id, value);
    }

    public static CursorApiResponse toCursorId(Object id) {
        return new CursorApiResponse(id, null);
    }

    public static CursorApiResponse noneCursor() {
        return new CursorApiResponse(null, null);
    }

    public static <T> T getLastElement(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}
