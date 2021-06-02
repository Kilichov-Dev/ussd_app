package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Payment;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.enums.PayType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.PaymentDto;
import ecma.ai.ussdapp.repository.PaymentRepository;
import ecma.ai.ussdapp.repository.RoleRepository;
import ecma.ai.ussdapp.repository.SimCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse add(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPayerName(paymentDto.getPayerName());
        payment.setPayerId(paymentDto.getPayerId());
        payment.setAmount(paymentDto.getAmount());
        SimCard simCard = simCardRepository.findBySimCardNumber(paymentDto.getSimCardNumber()).get();
        simCard.setBalance(simCard.getBalance() + paymentDto.getAmount());
        payment.setSimCard(simCard);
        if (paymentDto.getPayType().equalsIgnoreCase("payme")) {
            payment.setPayType(PayType.PAYME);
        } else if (paymentDto.getPayType().equalsIgnoreCase("click")) {
            payment.setPayType(PayType.CLICK);
        } else if (paymentDto.getPayType().equalsIgnoreCase("naqd")) {
            payment.setPayType(PayType.NAQD);
        } else if (paymentDto.getPayType().equalsIgnoreCase("humo")) {
            payment.setPayType(PayType.HUMO);
        } else {
            return new ApiResponse("Error!", false);
        }
        paymentRepository.save(payment);
        return new ApiResponse("Payment successfully!", true);
    }

    public ApiResponse getAll() {
        List<Payment> payments = paymentRepository.findAll();
        return new ApiResponse("History!", true, payments);
    }

    public ApiResponse getOneClientsPaymeHistory(String simCardNumber) {
        Optional<Payment> all = paymentRepository.findAllBySimCardNumber(simCardNumber);
        Optional<SimCard> bySimCardNumber = simCardRepository.findBySimCardNumber(simCardNumber);
        SimCard simCard = bySimCardNumber.get();
        return new ApiResponse("Success! " + simCard.getNumber(), true, all);
    }

    public ApiResponse getPaymentHistoryClient() {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Payment> allBySimCardNumber = paymentRepository.findAllBySimCardNumber(simCard.getNumber());
        return new ApiResponse("Success! History: " + simCard.getNumber(), true, allBySimCardNumber);

    }
}
