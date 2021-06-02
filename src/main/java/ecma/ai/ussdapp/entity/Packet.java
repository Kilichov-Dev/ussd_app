package ecma.ai.ussdapp.entity;

import ecma.ai.ussdapp.entity.enums.PacketType;
import ecma.ai.ussdapp.entity.template.AbsEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Packet extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private PacketType packetType;

    @Column(nullable = false,unique = true)
    private String name;

    private int amount;

    private int cost;

    private int duration; // 5 kunlik

//    private Timestamp expireDate;
//
//    @Transient
//    private int duration;
//
//    public int getDuration() {
//        return Period.between(this.getCreatedAt(), expireDate).getDays();
//    }

    private boolean isTariff;

    @OneToMany
    private List<Tariff> availableTariffs; //shu tariflarga dostup


}
