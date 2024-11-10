package com.App.ProyectoSena.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.App.ProyectoSena.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

}
