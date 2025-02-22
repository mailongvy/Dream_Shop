package com.mlv.dreamshop.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
