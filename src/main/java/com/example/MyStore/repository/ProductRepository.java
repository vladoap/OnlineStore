package com.example.MyStore.repository;

import com.example.MyStore.model.entity.Category;
import com.example.MyStore.model.entity.Product;
import com.example.MyStore.model.entity.User;
import com.example.MyStore.model.enums.CategoryNameEnum;
import com.example.MyStore.model.service.ProductSummaryServiceModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBySellerUsernameIsNot(String username);

    @Query("SELECT p FROM Product p WHERE p.seller.username != :username")
    List<Product> findAllBySellerUsernameIsNotByPagination(String username, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.seller.username != :username AND p.category.name = :categoryName")
    List<Product> findAllByCategoryNameAndSellerUsernameIsNot(@Param(value = "categoryName") CategoryNameEnum catName, String username);

    @Query("SELECT p FROM Product p WHERE p.seller.username != :username AND p.category.name = :categoryName")
    List<Product> findAllByCategoryAndSellerUsernameIsNotByPagination(String username, @Param(value = "categoryName") CategoryNameEnum catName, Pageable pageable);

}
