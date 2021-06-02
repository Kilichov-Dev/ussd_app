package ecma.ai.ussdapp.repository;

import ecma.ai.ussdapp.entity.Packet;
import ecma.ai.ussdapp.entity.enums.PacketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PacketRepository extends JpaRepository<Packet, UUID> {
    Optional<Packet> findByPacketTypeAndAmount(PacketType packetType, int amount);

    boolean existsByName(String name);

    Packet findByName(String name);
}
