package com.digitalmart.application.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public String addCategory(CategoryDto dto) throws BadRequestException {

        if(categoryRepository.existsByName(dto.name())){
            throw new BadRequestException("Category already exist");
        }

        Category category = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .build();

        categoryRepository.save(category);

        return "Saved Successfully";
    }
}
