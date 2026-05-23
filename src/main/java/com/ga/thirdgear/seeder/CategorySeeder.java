package com.ga.thirdgear.seeder;

import com.ga.thirdgear.model.Category;
import com.ga.thirdgear.repository.CategoryRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CategorySeeder {

    private static final Logger logger = LoggerFactory.getLogger(CategorySeeder.class);

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    public void seed() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(Category.builder().name("Sports Car").description("High performance sports cars").build());
        categoryRepository.save(Category.builder().name("Classic").description("Vintage and classic cars").build());
        categoryRepository.save(Category.builder().name("SUV").description("Sport utility vehicles").build());
        categoryRepository.save(Category.builder().name("Sedan").description("Standard sedan cars").build());

        logger.info("✅ Categories seeded!");
    }
}