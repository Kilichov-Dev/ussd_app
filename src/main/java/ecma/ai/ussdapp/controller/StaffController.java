package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.StaffDto;
import ecma.ai.ussdapp.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_DIRECTOR')")
    @PostMapping
    public HttpEntity<?> add(@RequestBody StaffDto staffDto) {
        ApiResponse apiResponse = staffService.addStaff(staffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_DIRECTOR','ROLE_STAFF')")
    @PutMapping("{username}")
    public HttpEntity<?> editStaff(@PathVariable String username, @RequestBody StaffDto staffDto) {
        ApiResponse apiResponse = staffService.editStaff(username, staffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_DIRECTOR','ROLE_STAFF')")
    @GetMapping("/getOneStaff")
    public HttpEntity<?> getFilial() {
        ApiResponse apiResponse = staffService.getStaff();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getStaffList() {
        List<Staff> staff = staffService.getStaffList();
        return ResponseEntity.ok(staff);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER','ROLE_MANAGER')")
    @DeleteMapping("{username}")
    public HttpEntity<?> deleteFilial(@PathVariable String username) {
        ApiResponse apiResponse = staffService.deleteStaff(username);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
