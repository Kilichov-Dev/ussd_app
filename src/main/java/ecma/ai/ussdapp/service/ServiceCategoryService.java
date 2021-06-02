package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.ServiceCategory;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.ServiceCategoryDto;
import ecma.ai.ussdapp.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {

    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;

    public List<ServiceCategory> getServiceCategoryList() {
        return serviceCategoryRepository.findAll();
    }

    public ApiResponse getServiceCategoryById(Integer id) {
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepository.findById(id);
        if (!optionalServiceCategory.isPresent()) {
            return new ApiResponse("Category not found!", false);
        }
        ServiceCategory serviceCategory = optionalServiceCategory.get();
        return new ApiResponse("Category", true, serviceCategory);
    }

    public ApiResponse addCategory(ServiceCategoryDto serviceCategoryDto) {
        boolean existsByName = serviceCategoryRepository.existsByName(serviceCategoryDto.getName());
        if (existsByName) {
            return new ApiResponse("This category name already exists!", false);
        }

        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setName(serviceCategory.getName());
        serviceCategoryRepository.save(serviceCategory);
        return new ApiResponse("Category adedd!", false);
    }

    public ApiResponse editCategory(ServiceCategoryDto serviceCategoryDto, Integer id) {
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepository.findById(id);
        if (!optionalServiceCategory.isPresent()) {
            return new ApiResponse("Categroy id not found!", false);
        }
        ServiceCategory serviceCategory = optionalServiceCategory.get();
        boolean existsByName = serviceCategoryRepository.existsByName(serviceCategory.getName());
        if (existsByName) {
            return new ApiResponse("This category name already exists!", false);
        }
        serviceCategory.setName(serviceCategory.getName());
        serviceCategoryRepository.save(serviceCategory);
        return new ApiResponse("Category editing!", true);


    }
}
