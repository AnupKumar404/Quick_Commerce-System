package com.digitalmart.application.Product;

import com.digitalmart.application.services.PresignedUrlResponse;
import com.digitalmart.application.services.S3Service;
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
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductCreateDao dao) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(dao));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> displayProducts() throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.displayProducts());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<AdminDao>> getProducts() throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }


    @GetMapping("/upload/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(
            @RequestParam String fileName,
            @RequestParam String contentType
    ){

        return ResponseEntity.ok(s3Service.generatePresignedUrl(fileName, contentType));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> fetchSingleProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.fetchSingleProduct(productId));
    }
}
