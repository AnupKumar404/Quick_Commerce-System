package com.digitalmart.application.Product;

import com.digitalmart.application.Category.Category;
import com.digitalmart.application.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public String addProduct(ProductCreateDao dao) throws BadRequestException {

        if(productRepository.existsByName(dao.name())){
            throw new BadRequestException("product already exists");
        }

        Category category = categoryRepository.findByName(dao.category())
                .orElseThrow(() -> new BadRequestException("Category does not exists"));

        Product newProduct = Product.builder()
                .sku(dao.sku())
                .name(dao.name())
                .category(category)
                .description(dao.description())
                .availableStock(dao.availableStock())
                .price(dao.price())
                .imageUrl(dao.imageUrl())
                .isAvailable(true)
                .build();

        productRepository.save(newProduct);

        return "New product added successfully";
    }

    public List<AdminDao> getProducts(){

        return productRepository.getAllProductsForDashboard();

    }

    public ProductDto fetchSingleProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow();

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public List<ProductDto> displayProducts(){
        return productRepository.getAllProducts();
    }
}
