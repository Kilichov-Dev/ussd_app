package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.component.NumberGenerator;
import ecma.ai.ussdapp.entity.Packet;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.Tariff;
import ecma.ai.ussdapp.entity.enums.ActionType;
import ecma.ai.ussdapp.entity.enums.PacketType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.DetailDto;
import ecma.ai.ussdapp.payload.SimCardDto;
import ecma.ai.ussdapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimCardService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    NumberGenerator numberGenerator;
    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    PacketRepository packetRepository;
    @Autowired
    DetailsService detailsService;
    @Autowired
    TariffRepository tariffRepository;


    public ApiResponse addSimCard(SimCardDto simCardDto) {
        String generetorNumber = numberGenerator.generetorNumber(7);
        boolean exists = simCardRepository.existsByCodeAndNumber(simCardDto.getCode(), generetorNumber);
        if (exists) {
            return new ApiResponse("Bu nomer mavjud!", false);
        }
        SimCard simCard = new SimCard();
        simCard.setSimCardNumber(generetorNumber);
        simCard.setBalance(0);
        simCard.setName(simCardDto.getName());
        simCard.setCode(simCardDto.getCode());

        String pinCode = numberGenerator.generetorNumber(4);
        simCard.setPinCode(pinCode);
        simCardRepository.save(simCard);
        return new ApiResponse("Successfully added!", true);
    }

    public ApiResponse getunableSimCard(String code, String number) {
        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(code, number);
        if (!optionalSimCard.isPresent()) return new ApiResponse("SimCard not found!", false);
        SimCard simCard = optionalSimCard.get();
        simCard.setActive(false);
        simCardRepository.save(simCard);
        return new ApiResponse("The SIM card has not been activated!", true);
    }

    public ApiResponse getUssdCode(String ussdCode) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean existsByCode = ussdCodeRepository.existsByCode(ussdCode);
        if (!existsByCode) return new ApiResponse("Ussd code invalid!", false);

        if (ussdCode.equals("*100#")) {
            return new ApiResponse("Your balance", true, simCard.getBalance());
        } else if (ussdCode.equals("*101#")) {
            return new ApiResponse("Your internet traffik", true, simCard.getAmountMb());
        } else if (ussdCode.equals("*102#")) {
            return new ApiResponse("Your Sms", true, simCard.getAmountSms());
        } else if (ussdCode.equals("104#")) {
            Optional<Packet> optionalPacket = packetRepository.findByPacketTypeAndAmount(PacketType.MB, 1024);
            if (!optionalPacket.isPresent()) {
                return new ApiResponse("Packet not found!", false);
            }
            Packet packet = optionalPacket.get();
            if (simCard.isActive() && simCard.isTariffIsActive() && simCard.getBalance() > packet.getCost()) {
                simCard.setBalance(simCard.getBalance() - packet.getCost());
                simCard.setAmountMb(simCard.getAmountMb() + packet.getAmount());
                detailsService.add(new DetailDto(ActionType.PAKET, simCard, (float) packet.getAmount()));
                simCardRepository.save(simCard);
                return new ApiResponse("Successfully your trafik 1GB", true);
            }
            return new ApiResponse("Request noto'gri bajarildi!", false);
        } else if (ussdCode.equals("*105#")) {
            Optional<Packet> minut = packetRepository.findByPacketTypeAndAmount(PacketType.MIN, 100);
            if (!minut.isPresent()) {
                return new ApiResponse("Ushbu packet yo'q", false);
            }
            Packet packet = minut.get();
            if (simCard.isActive() && simCard.isTariffIsActive() && simCard.getBalance() > packet.getCost()) {
                simCard.setBalance(simCard.getBalance() - packet.getCost());
                simCard.setAmountMb(simCard.getAmountMinute() + packet.getAmount());
                simCardRepository.save(simCard);
                detailsService.add(new DetailDto(ActionType.PAKET, simCard, (float) packet.getCost()));
                return new ApiResponse("Successfully your min 100!", true);
            }
            return new ApiResponse("Request noto'gri bajarildi!", false);

        }
        return new ApiResponse("Invalid Usdd code!", false);


    }

    public ApiResponse connectTariff(UUID tarifId) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tariff> optionalTariff = tariffRepository.findById(tarifId);
        if (!optionalTariff.isPresent()) return new ApiResponse("Tarif not found!", false);
        Tariff tariff = optionalTariff.get();
        if (simCard.getBalance() > (tariff.getSwitchPrice() + tariff.getPrice()) && simCard.isActive()) {

            simCard.setTariff(tariff);
            simCard.setBalance(simCard.getBalance() - (tariff.getPrice() + tariff.getSwitchPrice()));
            simCard.setAmountMinute(tariff.getMin());
            simCard.setAmountMb(tariff.getMb());
            simCard.setAmountSms(tariff.getSms());
            simCardRepository.save(simCard);
            return new ApiResponse("Sizning tarifingiz " + tariff.getName(), true);
        }
        return new ApiResponse("Error!", false);
    }

    public Set<SimCard> getSimCards() {
        List<SimCard> simCardList = simCardRepository.findAll();
        Set<SimCard> simCardSet = new HashSet<>();
        for (SimCard simCard : simCardList) {
            if (!simCard.isActive()) {
                simCardSet.add(simCard);
            }
        }
        return simCardSet;
    }
}
