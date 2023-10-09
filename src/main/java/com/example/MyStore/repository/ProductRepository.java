package com.example.MyStore.repository;

import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBySellerUsernameIsNot(String username);


    @Query("SELECT p FROM Product p WHERE p.seller.username != :username")
    List<Product> findAllBySellerUsernameIsNotByPagination(Pageable pageable, String username);
}
