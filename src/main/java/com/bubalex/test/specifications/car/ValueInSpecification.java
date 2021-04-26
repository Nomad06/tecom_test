package com.bubalex.test.specifications.car;


import com.bubalex.test.entities.CarEntity;
import com.bubalex.test.entities.CarEntity_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public record ValueInSpecification<T, E>(String columnName, List<E> values) implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return root.get(columnName).in(values);
    }
}
