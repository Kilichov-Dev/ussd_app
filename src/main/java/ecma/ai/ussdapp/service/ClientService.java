package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.*;
import ecma.ai.ussdapp.entity.enums.ActionType;
import ecma.ai.ussdapp.entity.enums.ClientType;
import ecma.ai.ussdapp.entity.enums.RoleName;
import ecma.ai.ussdapp.payload.*;
import ecma.ai.ussdapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DetailsService detailsService;
    //    @Autowired
//    NumberGenerator numberGenerator;
    @Autowired
    TariffRepository tariffRepository;

    public ApiResponse buySimCard(ClientDto clientDto) {
        boolean existsByPassportNumber = clientRepository.existsByPassportNumber(clientDto.getPassportNumber());
        if (existsByPassportNumber) {
            Optional<Client> optionalClient = clientRepository.findByPassportNumber(clientDto.getPassportNumber());

            Client client = optionalClient.get();

            List<BuySimCardDto> buySimCardDtos = clientDto.getBuySimCardDtos();
            List<SimCard> simCardList = new ArrayList<>();
            for (BuySimCardDto buySimCard : buySimCardDtos) {
                Optional<Tariff> optionalTariff = tariffRepository.findById(buySimCard.getTariffId());
                if (!optionalTariff.isPresent()) {
                    return new ApiResponse("Tarif not found!", false);
                }
                Tariff tariff = optionalTariff.get();

                Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(buySimCard.getCode(), buySimCard.getNumber());
                if (!optionalSimCard.isPresent()) {
                    return new ApiResponse("SimCard not found!", false);
                }
                SimCard simCard = optionalSimCard.get();


                if (simCard.isActive()) {
                    return new ApiResponse("Simcard already exist!", false);
                }
                simCard.setActive(true);
                simCard.setTariff(tariff);
                simCard.setClient(client);
                simCard.setBalance(buySimCard.getSum());
                if (buySimCard.getSum() >= tariff.getPrice()) {
                    simCard.setBalance(simCard.getBalance() - tariff.getPrice());
                    simCard.setAmountMb(tariff.getMb());
                    simCard.setAmountMinute(tariff.getMin());
                    simCard.setAmountSms(tariff.getSmsCost());
                    simCard.setTariffIsActive(true);
                } else {
                    simCard.setAmountMb(0);
                    simCard.setAmountMinute(0);
                    simCard.setAmountSms(0);
                    simCard.setTariffIsActive(false);
                }
                simCardList.add(simCard);
            }
            client.setSimCardList(simCardList);
            clientRepository.save(client);
            return new ApiResponse("Simcard Successfully berildi!", true);
        } else {
            Client client = new Client();
            client.setPassportNumber(clientDto.getPassportNumber());
            if (clientDto.getClientTypeOrdinal() == 1) {
                client.setClientType(ClientType.USER);
            }
            if (clientDto.getClientTypeOrdinal() == 2) {
                client.setClientType(ClientType.COMPANY);
            } else {
                return new ApiResponse("Client type not found!", false);
            }
            client.setFullName(clientDto.getFullName());
            Role byRoleName = roleRepository.findByRoleName(RoleName.ROLE_CLIENT);
            client.setRoles(Collections.singleton(byRoleName));

            List<SimCard> simCardList = new ArrayList<>();

            for (BuySimCardDto buySimCardDto : clientDto.getBuySimCardDtos()) {
                Optional<Tariff> optionalTariff = tariffRepository.findById(buySimCardDto.getTariffId());
                if (!optionalTariff.isPresent()) return new ApiResponse("Tarif not found!", false);
                Tariff tariff = optionalTariff.get();

                Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(buySimCardDto.getCode(), buySimCardDto.getNumber());
                if (!optionalSimCard.isPresent()) return new ApiResponse("SimCard not found!", false);
                SimCard simCard = optionalSimCard.get();

                simCard.setActive(true);
                simCard.setTariff(optionalTariff.get());
                simCard.setBalance(buySimCardDto.getSum());
                simCard.setClient(client);

                if (buySimCardDto.getSum() >= tariff.getPrice()) {
                    simCard.setBalance(simCard.getBalance() - tariff.getPrice());
                    simCard.setAmountMb(tariff.getMb());
                    simCard.setAmountMinute(tariff.getMin());
                    simCard.setAmountSms(tariff.getSms());
                    simCard.setTariffIsActive(true);
                } else {
                    simCard.setAmountMb(0);
                    simCard.setAmountMinute(0);
                    simCard.setAmountSms(0);
                    simCard.setTariffIsActive(false);
                }
                simCardList.add(simCard);
            }
            client.setSimCardList(simCardList);
            clientRepository.save(client);
            return new ApiResponse("SimCard Successfully berildi!", true);
        }
    }

    public ApiResponse requestCredit(DebitDto debitDto) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (simCard.isCredit() || simCard.getBalance() > 5000) {
            return new ApiResponse("Sizga qarz berilmaydi!", false);
        }
        if (debitDto.getAmount() > 25000) {
            return new ApiResponse("Siz faqat 25000 gacha qarz olasiz!", false);
        }

        simCard.setCredit(true);
        simCard.setBalance(simCard.getBalance() + debitDto.getAmount());

        DebtSimCard debtSimCard = new DebtSimCard();
        debtSimCard.setSimCard(simCard);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        debtSimCard.setExpireDate(new Timestamp(calendar.getTime().getTime()));

        simCard.setDebtSimCards(Collections.singletonList(debtSimCard));

        simCardRepository.save(simCard);
        return new ApiResponse("Sizga qarz berildi!", true);

    }

    public ApiResponse callSmb(CallDto callDto) {

        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(callDto.getCode(), callDto.getNumber());
        if (!optionalSimCard.isPresent()) return new ApiResponse("Siz tergan raqam mavjud emas!", false);

        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tariff tariff = simCard.getTariff();

        if (!simCard.isCredit() && simCard.isActive() && simCard.getBalance() > tariff.getMinCost()) {
            int minCost = tariff.getMinCost();
            float outcome = 0;

            for (int i = 0; i < callDto.getSeconds() / 60; i++) {
                if (simCard.getAmountMinute() > 1 && simCard.getBalance() > minCost) {
                    simCard.setAmountMinute(simCard.getAmountMinute() - 1);
                } else if (simCard.getAmountMinute() == 0 && simCard.getAmountMinute() > 0) {
                    simCard.setBalance(simCard.getBalance() - minCost);
                    outcome += minCost;
                } else if (simCard.getBalance() <= 0 && simCard.getAmountMinute() == 0) {
                    simCard.setCredit(true);
                    return new ApiResponse("Qo'ng'iroq yakunlandi balansada mablag' qolmadi!", false);
                }
            }
            detailsService.add(new DetailDto(ActionType.MIN, simCard, outcome));
            simCardRepository.save(simCard);
            return new ApiResponse("Qo'ng'iroq yakunlandi!", true);
        }
        return new ApiResponse("Sizda yetarli mablag' mavjud emas!", false);
    }

    public ApiResponse sendSms(SmsDto smsDto) {
        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(smsDto.getCode(), smsDto.getNumber());
        if (optionalSimCard.isPresent()) {
            return new ApiResponse("Number not found!", false);
        }

        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tariff tariff = simCard.getTariff();
        float outcome = 0;

        if (simCard.getAmountSms() > 1 && !simCard.isCredit() && simCard.isActive()) {
            simCard.setAmountMinute(simCard.getAmountMinute() - 1);
        } else if (simCard.getAmountSms() == 0 && simCard.getBalance() >= tariff.getSmsCost()) {
            simCard.setBalance(simCard.getAmountSms() - tariff.getSmsCost());
            outcome = outcome + tariff.getSmsCost();
        } else if (simCard.getBalance() <= 0 && simCard.getAmountSms() == 0) {
            return new ApiResponse("Sizda yetarli mablag' mavjud emas!", false);
        }
        detailsService.add(new DetailDto(ActionType.SMS, simCard, outcome));
        simCardRepository.save(simCard);
        return new ApiResponse("Success", true);
    }


}
