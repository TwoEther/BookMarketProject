package org.project.shop.custom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest {
    public static PageRequest customPageRequest() {
        return PageRequest.of(0, 6);
    }
    public static PageRequest customPageRequest(int page) {
        return PageRequest.of(page, 6);
    }

    public static PageRequest customPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }
    public static PageRequest customPageRequest(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
