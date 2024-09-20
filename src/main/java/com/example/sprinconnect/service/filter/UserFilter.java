package com.example.sprinconnect.service.filter;

import com.example.sprinconnect.models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class UserFilter {

    public static Specification<User> filterBySearch(String email, String firstName, String lastName, String startDate, String endDate) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction(); // Start with a true condition

            // Create a map of fields to filter
            Map<String, String> filters = new HashMap<>();
            if (email != null && !email.isEmpty()) filters.put("email", email);
            if (firstName != null && !firstName.isEmpty()) filters.put("firstName", firstName);
            if (lastName != null && !lastName.isEmpty()) filters.put("lastName", lastName);
            if (startDate != null && !startDate.isEmpty()) filters.put("startDate", startDate);

            // Apply filters
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();
                if(field.equals("startDate")) {
                    LocalDateTime strtDate = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                    LocalDateTime ndDate = endDate == null ? LocalDateTime.now() : LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                    System.out.println("startDate: " + startDate + "endDate: " + ndDate);
                    Predicate fieldDatePredicate = cb.between(root.get("createdAt"), strtDate, ndDate);

//                    ---------------------pwede ra pud kani gamiton---------------------
//                    Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("createdAt"), strtDate);
//                    Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("createdAt"), ndDate);
//                    Predicate fieldDatePredicate = cb.and(startDatePredicate, endDatePredicate);

                    predicate = cb.and(predicate, fieldDatePredicate);
                }else{
                    String valueTerm = "%" + value.toLowerCase() + "%";
                    Predicate fieldPredicate = cb.like(cb.lower(root.get(field)), valueTerm);
                    predicate = cb.and(predicate, fieldPredicate);
                }
            }

            return predicate;
        };
    }
}
