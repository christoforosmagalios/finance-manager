package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.CategoryDTO;
import com.github.cmag.financemanager.model.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Category Rest controller.
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController<CategoryDTO, Category> {

}
