package com.example.sprinconnect.models;

import lombok.Getter;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
public class UserPagination<T> {

    private List<User> users;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    @Getter
    private boolean last;

    public UserPagination() {

    }

    public UserPagination(List<User> users, int page, int size, long totalElements, int totalPages, boolean last) {
        this.users = users;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<User> getUsers() {
        return users == null ? null : new ArrayList<>(users);
    }

    public void setUsers(List<User> users) {
        if (users == null) {
            this.users = null;
        }else {
            this.users = Collections.unmodifiableList(users);
        }
    }

//    public boolean isLast() {
//        return last;
//    }
}
