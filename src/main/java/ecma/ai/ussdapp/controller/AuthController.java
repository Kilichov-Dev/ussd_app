package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.LoginDto;
import ecma.ai.ussdapp.repository.StaffRepository;
import ecma.ai.ussdapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    StaffRepository staffRepository;

    @PostMapping("/loginForStaff")
    public HttpEntity<?> loginForStaff(LoginDto loginDto) {
        ApiResponse apiResponse = authService.loginForStaff(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/loginForClient")
    public HttpEntity<?> loginForClient(LoginDto loginDto){
        ApiResponse apiResponse = authService.loginForClient(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
