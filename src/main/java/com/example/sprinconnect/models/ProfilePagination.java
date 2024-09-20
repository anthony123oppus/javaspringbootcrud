package com.example.sprinconnect.models;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ProfilePagination<T> {

    private List<Profile> profiles;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    @Getter
    private boolean last;

    public ProfilePagination() {

    }

    public ProfilePagination(List<Profile> profiles, int page, int size, long totalElements, int totalPages, boolean last) {
        this.profiles = profiles;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<Profile> getProfiles() {
        return profiles == null ? null : new ArrayList<>(profiles);
    }

    public void setProfiles(List<Profile> profiles) {
        if (profiles == null) {
            this.profiles = null;
        }else {
            this.profiles = Collections.unmodifiableList(profiles);
        }
    }
}
