package com.bubalex.test.specifications.car;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import javax.persistence.criteria.*;

public record BetweenSpecification<T, E extends Comparable<? super E>>(String columnName, Pair<E, E> rangePair) implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(root.get(columnName), rangePair.getFirst(), rangePair.getSecond());
    }
}
