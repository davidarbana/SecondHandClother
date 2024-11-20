package com.davidarbana.secondhandclother.repository;

import com.davidarbana.secondhandclother.model.Garment;
import com.davidarbana.secondhandclother.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long>, JpaSpecificationExecutor<Garment> {


    public static Specification<Garment> hasType(String type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + type.toLowerCase() + "%");
    }

    public static Specification<Garment> hasSize(String size) {
        return (root, query, criteriaBuilder) ->
                size == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("size")), "%" + size.toLowerCase() + "%");
    }

    public static Specification<Garment> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }

    List<Garment> findAll();

    Optional<Garment> findById(Long id);

}
