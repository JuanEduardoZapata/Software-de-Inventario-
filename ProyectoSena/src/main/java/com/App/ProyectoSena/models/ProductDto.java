package com.App.ProyectoSena.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {

	@NotEmpty (message = "El nombre no puede estar vacío")
	private String nombre;
	
	@NotEmpty (message = "La marca no puede estar vacía")
	private String marca;
	
	@NotEmpty (message = "La categoria no puede estar vacía")
	private String categoria;
	
	@Min (0)
	private String precio;
	
	@Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
	@Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
	private String descripcion;
	
    private MultipartFile imageFile;
    
    

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	} 
	
    
    
    
}
