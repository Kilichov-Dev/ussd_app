package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.entity.EntertainingService;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.EntertainingDto;
import ecma.ai.ussdapp.payload.ServiceDto;
import ecma.ai.ussdapp.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entertainingService")
public class EntertainingCategory {

    @Autowired
    ServiceService service;

    @GetMapping("/list")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> getEntertainingList() {
        List<EntertainingService> list = service.getList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/list/{id}")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse serviceById = service.getServiceById(id);
        return ResponseEntity.status(201).body(serviceById);
    }

    @PostMapping("/add")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> addEntertaining(@RequestBody ServiceDto serviceDto) {
        ApiResponse entertainingService = service.addEntertainingService(serviceDto);
        return ResponseEntity.status(201).body(entertainingService);
    }

    @PutMapping("/edit/{id}")
    @Secured({"ROLE_MANAGER"})
    public HttpEntity<?> editEntertaining(@PathVariable UUID id, @RequestBody ServiceDto serviceDto) {
        ApiResponse apiResponse = service.editEntertainingService(id, serviceDto);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @Secured({"ROLE_MANAGER"})
    @GetMapping("/famous")
    public HttpEntity<?> getFamousEntertainingList() {
        List<EntertainingService> serviceList = service.getList();
        return ResponseEntity.ok(serviceList);
    }

    @PostMapping("/addEntertaining")
    public HttpEntity<?> addEntertainingforClient(@RequestBody EntertainingDto entertainingDto) {
        ApiResponse apiResponse = service.addServiceForClient(entertainingDto);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete")
    public HttpEntity<?> deleteEntertainingforClient(@RequestBody EntertainingDto entertainingDto) {
        ApiResponse apiResponse = service.addServiceForClient(entertainingDto);
        return ResponseEntity.ok(apiResponse);
    }
}
