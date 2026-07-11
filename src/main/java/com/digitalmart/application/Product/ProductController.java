package com.digitalmart.application.Product;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductCreateDao dao) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(dao));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }

}
