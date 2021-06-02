package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.PaymentDto;
import ecma.ai.ussdapp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    public HttpEntity<?> addPayment(@RequestBody PaymentDto paymentDto) {
        ApiResponse add = paymentService.add(paymentDto);
        return ResponseEntity.status(add.isSuccess() ? 200 : 409).body(add);
    }

    @GetMapping("{simCardNumber}")
    public HttpEntity<?> getOneClientshistory(String simCardNumber) {
        ApiResponse oneClientsPaymeHistory = paymentService.getOneClientsPaymeHistory(simCardNumber);
        return ResponseEntity.status(oneClientsPaymeHistory.isSuccess() ? 200 : 409).body(oneClientsPaymeHistory);
    }

    @GetMapping("{byClient}")
    public ResponseEntity<?> getOneHistoryForClient() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping
    public ResponseEntity<?> getAllPaymentHistory() {
        return ResponseEntity.ok(paymentService.getAll());
    }


}
