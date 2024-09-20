package com.example.sprinconnect.service.filter;

import com.example.sprinconnect.models.Profile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

public class ProfileFilter {

    public static Specification<Profile> filterBySearch(String phoneNumber, String currentAddress, String permanentAddress) {
        return (Root<Profile> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate predicate = cb.conjunction();

            Map<String, String> filters = new HashMap<>();
            if(phoneNumber != null && !phoneNumber.isEmpty()) filters.put("phoneNumber", phoneNumber);
            if(currentAddress != null && !currentAddress.isEmpty()) filters.put("currentAddress", currentAddress);
            if(permanentAddress != null && !permanentAddress.isEmpty()) filters.put("permanentAddress", permanentAddress);

            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();
                String valueTerm = "%" + value.toLowerCase() + "%";
                Predicate fieldPredicate = cb.like(cb.lower(root.get(field)), valueTerm);
                predicate = cb.and(predicate, fieldPredicate);
            }

            return  predicate;
        };
    }
}
