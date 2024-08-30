package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.request.CategoryRequest;
import com.phatpl.learnvocabulary.services.CategoryService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        try {
            return BuildResponse.ok(categoryService.findAll());
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Integer id, @RequestBody @Valid CategoryRequest req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors();
            return BuildResponse.badRequest(errors.get(0).getDefaultMessage());
        }
        try {
            return BuildResponse.ok(categoryService.updCategory(Long.valueOf(id), req));
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id) {
        try {
            categoryService.delCategory(Long.valueOf(id));
            return BuildResponse.ok("success");
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest req, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors();
            return BuildResponse.badRequest(errors.get(0).getDefaultMessage());
        }

        try {
            return BuildResponse.ok(categoryService.addCategory(req));
        } catch (Exception e) {
            return BuildResponse.badRequest(e.getMessage());
        }
    }
}
