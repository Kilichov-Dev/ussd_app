package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.EntertainingService;
import ecma.ai.ussdapp.entity.ServiceCategory;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.enums.ActionType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.DetailDto;
import ecma.ai.ussdapp.payload.EntertainingDto;
import ecma.ai.ussdapp.payload.ServiceDto;
import ecma.ai.ussdapp.repository.ServiceCategoryRepository;
import ecma.ai.ussdapp.repository.ServiceRepository;
import ecma.ai.ussdapp.repository.SimCardRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ServiceService {
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    DetailsService detailsService;
    @Autowired
    SimCardService simCardService;
    @Autowired
    SimCardRepository simCardRepository;

    public List<EntertainingService> getList() {
        return serviceRepository.findAll();
    }

    public ApiResponse getServiceById(UUID id) {
        Optional<EntertainingService> optionalEntertainingService = serviceRepository.findById(id);
        if (!optionalEntertainingService.isPresent()) {
            return new ApiResponse("Bunday Id xizmat topilmadi!", false);
        }
        EntertainingService entertainingService = optionalEntertainingService.get();
        return new ApiResponse("Successfully!", true, entertainingService);

    }

    public ApiResponse addEntertainingService(ServiceDto serviceDto) {
        boolean existsByName = serviceRepository.existsByName(serviceDto.getName());
        if (existsByName) {
            return new ApiResponse("Bunday nomli xizmat mavjud!", false);

        }

        EntertainingService entertainingService = new EntertainingService();
        entertainingService.setName(serviceDto.getName());
        entertainingService.setExpiredDate(serviceDto.getExpiredDate());
        entertainingService.setPrice(serviceDto.getPrice());
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepository.findById(serviceDto.getServiceCategoryId());
        if (!optionalServiceCategory.isPresent()) {
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(serviceDto.getName());
            serviceCategoryRepository.save(serviceCategory);
        }
        ServiceCategory serviceCategory = optionalServiceCategory.get();
        entertainingService.setServiceCategory(serviceCategory);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Added!", true, entertainingService);
    }


    public ApiResponse editEntertainingService(UUID id, ServiceDto serviceDto) {
        Optional<EntertainingService> optionalEntertainingService = serviceRepository.findById(id);
        if (!optionalEntertainingService.isPresent()) {
            return new ApiResponse("Bunday id li xizmat topilmadi!", false);
        }
        EntertainingService entertainingService = optionalEntertainingService.get();
        boolean existsByName = serviceRepository.existsByName(serviceDto.getName());
        if (existsByName) {
            return new ApiResponse("Bunday nomli xizmat mavjud!", false);
        }
        entertainingService.setName(serviceDto.getName());
        entertainingService.setPrice(serviceDto.getPrice());
        entertainingService.setExpiredDate(serviceDto.getExpiredDate());
        Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepository.findById(serviceDto.getServiceCategoryId());
        if (!optionalServiceCategory.isPresent()) {
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(serviceDto.getName());
            serviceCategoryRepository.save(serviceCategory);
        }
        ServiceCategory serviceCategory = optionalServiceCategory.get();
        entertainingService.setServiceCategory(serviceCategory);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Service success editing!", true);
    }

    public ApiResponse addServiceForClient(EntertainingDto entertainingDto) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<EntertainingService> optional = serviceRepository.findByName(entertainingDto.getEnterteiningName());
        if (optional.isPresent()) return new ApiResponse("Bunday xizmat mavjud emas!", false);
        EntertainingService entertainingService = optional.get();
        if (entertainingService.getPrice() > simCard.getBalance()) {
            return new ApiResponse("Mablag' yatarli emas!", false);
        }
        Set<EntertainingService> entertainingServices = simCard.getEntertainingServices();
        entertainingServices.add(entertainingService);
        simCard.setEntertainingServices(entertainingServices);
        simCardRepository.save(simCard);
        detailsService.add(new DetailDto(ActionType.SERVICE, simCard, (float) entertainingService.getPrice()));
        entertainingService.setCount(entertainingService.getCount() + 1);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Ulandi", true, null);
    }

}
