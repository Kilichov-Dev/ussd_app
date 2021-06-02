package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Packet;
import ecma.ai.ussdapp.entity.Tariff;
import ecma.ai.ussdapp.entity.enums.PacketType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.PacketDto;
import ecma.ai.ussdapp.payload.PacketDtoForClinets;
import ecma.ai.ussdapp.repository.PacketRepository;
import ecma.ai.ussdapp.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PacketService {
    @Autowired
    PacketRepository packetRepository;
    @Autowired
    TariffRepository tariffRepository;

    public ApiResponse addPacket(PacketDto packetDto) {
        boolean exists = packetRepository.existsByName(packetDto.getName());
        if (exists) {
            return new ApiResponse("Bunday nomli packet mavjud emas!", false);
        }
        Packet packet = new Packet();
        packet.setName(packetDto.getName());
        if (packetDto.getPacketTypeId() == 1) {
            packet.setPacketType(PacketType.MB);
        } else if (packetDto.getPacketTypeId() == 2) {
            packet.setPacketType(PacketType.SMS);
        } else if (packetDto.getPacketTypeId() == 3) {
            packet.setPacketType(PacketType.MIN);
        } else {
            return new ApiResponse("Packet type id not found!", false);
        }
        packet.setAmount(packetDto.getAmount());
        packet.setCost(packetDto.getCost());
        packet.setDuration(packetDto.getDuration());
        packet.setTariff(packetDto.isTariff());
        List<String> availableTariffs = packetDto.getAvailableTariffs();
        List<Tariff> tariffs = new ArrayList<>();
        for (String availableTariff : availableTariffs) {
            Tariff byName = tariffRepository.findByName(availableTariff);
            tariffs.add(byName);
        }
        packet.setAvailableTariffs(tariffs);
        packetRepository.save(packet);
        return new ApiResponse("Packet added!", true);
    }

    public List<Packet> getAllPackets() {
        return packetRepository.findAll();
    }

    public List<String> getAllpacketForClient() {
        List<Packet> all = packetRepository.findAll();
        List<String> getAll = new ArrayList<>();
        for (Packet packet : all) {
            getAll.add(packet.getName());

        }
        return getAll;
    }

    public ApiResponse editPacket(UUID id, PacketDto packetDto) {
        Optional<Packet> optionalPacket = packetRepository.findById(id);
        if (!optionalPacket.isPresent()) {
            return new ApiResponse("Packet topilmadi!", false);
        }
        Packet packet = optionalPacket.get();
        packet.setName(packetDto.getName());
        packet.setAmount(packetDto.getAmount());
        packet.setCost(packetDto.getCost());
        packet.setDuration(packetDto.getDuration());
        packetRepository.save(packet);
        return new ApiResponse("Packet editing!", true);
    }

    public ApiResponse deletePacket(UUID id) {
        Optional<Packet> optionalPacket = packetRepository.findById(id);
        if (optionalPacket.isPresent()) {
            return new ApiResponse("Packet not found!", false);
        }
        packetRepository.deleteById(id);
        return new ApiResponse("Packet deleted!", true);
    }

    public PacketDtoForClinets getPacketInfo(String packetName) {
        Packet packet = packetRepository.findByName(packetName);
        PacketDtoForClinets packetDto = new PacketDtoForClinets();
        packetDto.setName(packet.getName());
        packetDto.setPacketType(packet.getPacketType());
        packetDto.setTariff(packet.isTariff());
        packetDto.setDuration(packet.getDuration());
        packetDto.setAmount(packet.getAmount());
        packetDto.setCost(packet.getCost());
        List<Tariff> availableTariffs = packet.getAvailableTariffs();
        List<String> list = new ArrayList<>();
        for (Tariff availableTariff : availableTariffs) {
            list.add(availableTariff.getName());
        }

        packetDto.setAvailableTariffs(list);
        return packetDto;
    }
}
