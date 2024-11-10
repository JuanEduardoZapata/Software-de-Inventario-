package com.App.ProyectoSena.controllers;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.App.ProyectoSena.models.Product;
import com.App.ProyectoSena.models.ProductDto;
import com.App.ProyectoSena.services.ProductsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;
	
	@GetMapping({"", "/"})
	public String showProductList(Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		return "products/index";
	}
	
	@GetMapping("/create")
	public String showCreateForm(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}
	
	@PostMapping("/create")
	public String createProduct (
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result
			) {
		
		if (productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto", "imageFile", "La imagen es requerida"));
		}
		
		if (result.hasErrors()) {
			return "products/CreateProduct";
		}
		
		
		MultipartFile imageFile = productDto.getImageFile();
		Date createdAt = new Date();
		String storageFileName = createdAt.getTime() + "-" + imageFile.getOriginalFilename();
		
		try {
		    String uploadDir = "public/images/";
		    Path uploadPath = Paths.get(uploadDir);

		    if (!Files.exists(uploadPath)) {
		        Files.createDirectories(uploadPath);
		    }

		    try (InputStream inputStream = imageFile.getInputStream()) {
		        Files.copy(inputStream, uploadPath.resolve(storageFileName),
		                StandardCopyOption.REPLACE_EXISTING);
		    }
		    
		} catch (Exception ex) {
		    System.out.println("Exception: " + ex.getMessage());
		}
		
		Product product = new Product();
		product.setNombre(productDto.getNombre());
		product.setMarca(productDto.getMarca());
		product.setCategoria(productDto.getCategoria());
		product.setPrecio(productDto.getPrecio());
		product.setDescripcion(productDto.getDescripcion());
		product.setImageFileName(storageFileName);
		product.setCreatedAt(createdAt);
		
		repo.save(product);
		
		return "redirect:/products"; 
		
		
	}
	
	@GetMapping("/edit/{id}")
	public String showEditPage(
	    Model model,
	    @PathVariable("id") int id
	) {
	    try {
	        Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
	        model.addAttribute("product", product);

	        ProductDto productDto = new ProductDto();
	        productDto.setNombre(product.getNombre());
	        productDto.setMarca(product.getMarca());
	        productDto.setCategoria(product.getCategoria());
	        productDto.setPrecio(product.getPrecio());
	        productDto.setDescripcion(product.getDescripcion());

	        model.addAttribute("productDto", productDto);

	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	        return "redirect:/products";
	    }

	    return "products/EditProduct";
	}
	
	@PostMapping("/edit")
		public String updateProduct(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result) {
		
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);
			
			if (result.hasErrors()) {
				return "products/EditProduct";
			}
			
			if (!productDto.getImageFile().isEmpty()) {
				
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
				
				try {
					Files.delete(oldImagePath);
				}
				catch (Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				
				MultipartFile image = productDto.getImageFile();
				Date createdAt = new Date();
				String storageFileName = createdAt.getTime() + "-" + image.getOriginalFilename();
				
				try (InputStream InputStream = image.getInputStream()) {
					Files.copy(InputStream,  Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
				product.setImageFileName(storageFileName);
			}
			
			product.setNombre(productDto.getNombre());
			product.setMarca(productDto.getMarca());
			product.setCategoria(productDto.getCategoria());
			product.setPrecio(productDto.getPrecio());
			product.setDescripcion(productDto.getDescripcion());
			repo.save(product);
		}
		catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/products";
	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {
		
		
		try {
			Product product = repo.findById(id).get();
			
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			
			try {
				Files.delete(imagePath);
			}
			catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			repo.delete(product);
			
		}
		catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/products";
	}
	
}
	

	
	
	

