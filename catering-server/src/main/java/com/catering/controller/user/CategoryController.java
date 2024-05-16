package com.catering.controller.user;

import com.catering.dto.CategoryDTO;
import com.catering.result.Result;
import com.catering.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<CategoryDTO>> list(Integer type){
        List<CategoryDTO> list = categoryService.categoryByType(type);
        return Result.success(list);
    }
}
