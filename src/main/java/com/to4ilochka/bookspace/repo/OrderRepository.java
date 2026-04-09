package com.to4ilochka.bookspace.repo;

import com.to4ilochka.bookspace.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByClientId(Long id, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE " +
            "LOWER(o.client.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(o.id AS string) LIKE CONCAT('%', :keyword, '%')")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);
}
