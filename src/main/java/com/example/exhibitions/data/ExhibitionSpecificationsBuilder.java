package com.example.exhibitions.data;

import com.example.exhibitions.entity.Exhibition;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExhibitionSpecificationsBuilder {
    private final List<SearchCriteria> params;

    public ExhibitionSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public ExhibitionSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Exhibition> build() {
        if (params.size() == 0) return null;

        List<Specification> specs = params.stream()
                .map(ExhibitionSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
