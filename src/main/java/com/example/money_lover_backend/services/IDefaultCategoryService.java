package com.example.money_lover_backend.services;

import com.example.money_lover_backend.models.category.DefaultCategory;

import java.util.Optional;

public interface IDefaultCategoryService {
    Iterable<DefaultCategory> findAll();

    Optional<DefaultCategory> findById(Long id);

    DefaultCategory save(DefaultCategory defaultCategory);

    void remove(Long id);
}
