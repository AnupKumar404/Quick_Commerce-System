package com.digitalmart.application.Product;

import com.digitalmart.application.Category.Category;
import com.digitalmart.application.Category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductCreateDao addProduct(ProductCreateDao dao) throws BadRequestException {

        if(productRepository.existsByName(dao.name())){
            throw new BadRequestException("product already exists");
        }

        Category category = categoryRepository.findByName(dao.categoryName())
                .orElseThrow(() -> new BadRequestException("Category does not exists"));

        Product newProduct = Product.builder()
                .sku(dao.sku())
                .name(dao.name())
                .category(category)
                .description(dao.description())
                .price(dao.price())
                .imageUrl(dao.imageUrl())
                .build();

        productRepository.save(newProduct);

        return new ProductCreateDao(
                newProduct.getCategory().toString(),
                newProduct.getName(),
                newProduct.getSku(),
                newProduct.getDescription(),
                newProduct.getPrice(),
                newProduct.getImageUrl()
        );
    }
}
