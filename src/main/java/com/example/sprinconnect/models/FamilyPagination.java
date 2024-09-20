package com.example.sprinconnect.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class FamilyPagination<T> {

    private List<Family> families;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public FamilyPagination() {

    }

    public FamilyPagination(List<Family> families, int page, int size, long totalElements, int totalPages, boolean last) {
        this.families = families;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<Family> getFamilies() {
        return families == null ? null : new ArrayList<>(families);
    }

    public void setFamilies(List<Family> families) {
        if (families == null) {
            this.families = null;
        }else {
            this.families = Collections.unmodifiableList(families);
        }
    }
}
