package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Tariff;
import ecma.ai.ussdapp.entity.enums.ClientType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.TariffDto;
import ecma.ai.ussdapp.payload.TariffDtoForClient;
import ecma.ai.ussdapp.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TariffService {
    @Autowired
    TariffRepository tariffRepository;

    public ApiResponse addTariff(TariffDto tariffDto) {
        boolean existsByName = tariffRepository.existsByName(tariffDto.getName());
        if (existsByName) return new ApiResponse("Bunday tarif mavjud!", false);
        Tariff tariff = new Tariff();
        tariff.setName(tariffDto.getName());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setSwitchPrice(tariffDto.getSwitchPrice());
        tariff.setExpireDate(tariffDto.getExpireDate());
        if (tariffDto.getClientTypeId() == 1) {
            tariff.setClientType(ClientType.USER);
        } else if (tariffDto.getClientTypeId() == 2) {
            tariff.setClientType(ClientType.COMPANY);
        } else {
            return new ApiResponse("Client type not found!", false);
        }
        tariff.setMb(tariffDto.getMb());
        tariff.setMin(tariffDto.getMin());
        tariff.setSms(tariffDto.getSms());
        tariff.setMbCost(tariffDto.getMbCost());
        tariff.setMinCost(tariffDto.getMinCost());
        tariff.setSmsCost(tariffDto.getSmsCost());
        tariffRepository.save(tariff);
        return new ApiResponse("Tariff saved!", true);
    }

    public ApiResponse editTariff(UUID id, TariffDto tariffDto) {
        Optional<Tariff> optionalTariff = tariffRepository.findById(id);
        if (!optionalTariff.isPresent()) {
            return new ApiResponse("Tarif not found!", false);
        }
        Tariff tariff = optionalTariff.get();
        tariff.setName(tariffDto.getName());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setSwitchPrice(tariffDto.getSwitchPrice());
        tariff.setExpireDate(tariffDto.getExpireDate());
        if (tariffDto.getClientTypeId() == 1) {
            tariff.setClientType(ClientType.USER);
        } else if (tariffDto.getClientTypeId() == 2) {
            tariff.setClientType(ClientType.COMPANY);
        } else {
            return new ApiResponse("Client type id wrong !", false);
        }
        tariff.setMb(tariffDto.getMb());
        tariff.setMin(tariffDto.getMin());
        tariff.setSms(tariffDto.getSms());
        tariff.setMbCost(tariffDto.getMbCost());
        tariff.setSmsCost(tariffDto.getSmsCost());
        tariff.setMinCost(tariffDto.getMinCost());
        tariffRepository.save(tariff);
        return new ApiResponse("Tariff edited !", true);
    }

    public ApiResponse deleteTariff(UUID id) {
        Optional<Tariff> optionalTariff = tariffRepository.findById(id);
        if (!optionalTariff.isPresent()) {
            return new ApiResponse("Tariff not found!", false);
        }
        tariffRepository.delete(optionalTariff.get());
        return new ApiResponse("Tariff deleted", true);
    }

    public List<Tariff> getAllTariff() {
        return tariffRepository.findAll();
    }

    public List<String> getAllForClient() {
        List<Tariff> tariffList = tariffRepository.findAll();
        List<String> list = new ArrayList<>();
        for (Tariff tariff : tariffList) {
            list.add(tariff.getName());
        }
        return list;
    }

    public TariffDtoForClient getTarifInfoForClient(String tariffName) {
        Tariff tariff = tariffRepository.findByName(tariffName);
        TariffDtoForClient tariffDtoForClient = new TariffDtoForClient();
        tariffDtoForClient.setName(tariff.getName());
        tariffDtoForClient.setPrice(tariff.getPrice());
        tariffDtoForClient.setSwitchPrice(tariff.getSwitchPrice());
        tariffDtoForClient.setExpireDate(tariff.getExpireDate());
        tariffDtoForClient.setSms(tariff.getSms());
        tariffDtoForClient.setMin(tariff.getMin());
        tariffDtoForClient.setMb(tariff.getMb());
        tariffDtoForClient.setSmsCost(tariff.getSmsCost());
        tariffDtoForClient.setMinCost(tariff.getMinCost());
        tariffDtoForClient.setMbCost(tariff.getMbCost());
        return tariffDtoForClient;
    }

}
