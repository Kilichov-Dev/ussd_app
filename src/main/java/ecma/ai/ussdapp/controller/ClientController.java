package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.payload.*;
import ecma.ai.ussdapp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PreAuthorize(value = "hasAnyRole(ROLE_STAFF,ROLE_CLIENT)")
    @PostMapping("/buy")
    public ResponseEntity<?> buySimCard(@RequestBody ClientDto clientDto) {
        ApiResponse apiResponse = clientService.buySimCard(clientDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/requestDebt")
    public ResponseEntity<?> requestCredit(@RequestBody DebitDto debitDto) {
        ApiResponse apiResponse = clientService.requestCredit(debitDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/call")
    public ResponseEntity<?> call(@RequestBody CallDto callDto) {
        ApiResponse apiResponse = clientService.callSmb(callDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/sendSms")
    public ResponseEntity<?> sendSms(@RequestBody SmsDto smsDto) {
        ApiResponse apiResponse = clientService.sendSms(smsDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }
}
