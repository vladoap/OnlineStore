package com.example.MyStore.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    private PaginationUtil() {
    }

    public static List<Integer> getPageCount(int pageSize, int totalProducts) {
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }


}
