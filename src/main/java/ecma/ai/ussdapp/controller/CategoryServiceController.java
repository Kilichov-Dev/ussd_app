package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.entity.ServiceCategory;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.ServiceCategoryDto;
import ecma.ai.ussdapp.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/serviceCategory")
public class CategoryServiceController {
    @Autowired
    ServiceCategoryService serviceCategoryService;

    @GetMapping
    public HttpEntity<?> getAll() {
        List<ServiceCategory> serviceCategoryList = serviceCategoryService.getServiceCategoryList();
        return ResponseEntity.status(200).body(serviceCategoryList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        ApiResponse serviceServiceCategoryById = serviceCategoryService.getServiceCategoryById(id);
        return ResponseEntity.status(serviceServiceCategoryById.isSuccess() ? 200 : 409).body(serviceServiceCategoryById);
    }

    @PutMapping("/edit/{id}")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> editCategory(@PathVariable Integer id, @RequestBody ServiceCategoryDto serviceCategoryDto) {
        ApiResponse apiResponse = serviceCategoryService.editCategory(serviceCategoryDto, id);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @PostMapping("/add")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> addCategory(@RequestBody ServiceCategoryDto categoryServiceDto) {
        ApiResponse apiResponse = serviceCategoryService.addCategory(categoryServiceDto);
        return ResponseEntity.status(201).body(apiResponse);
    }
}
