package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CatEntryDto;
import ru.practicum.exception.DuplicateException;
import ru.practicum.model.Category;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@CrossOrigin(maxAge = 3600, origins = "*", allowedHeaders = "*")
public class AdminCatController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Valid @RequestBody CatEntryDto catEntryDto) throws DuplicateException {
        return categoryService.addCategory(catEntryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public Category updateCategory(@PathVariable Long catId, @Valid @RequestBody CatEntryDto catEntryDto) throws DuplicateException {
        return categoryService.updateCategory(catId, catEntryDto);
    }
}
