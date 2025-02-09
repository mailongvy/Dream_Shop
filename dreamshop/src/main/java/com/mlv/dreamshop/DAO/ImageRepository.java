package com.mlv.dreamshop.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlv.dreamshop.Model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
