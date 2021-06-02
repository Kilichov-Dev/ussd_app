package ecma.ai.ussdapp.component;

import ecma.ai.ussdapp.entity.Client;
import ecma.ai.ussdapp.entity.Role;
import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.entity.UssdCode;
import ecma.ai.ussdapp.entity.enums.ClientType;
import ecma.ai.ussdapp.entity.enums.RoleName;
import ecma.ai.ussdapp.repository.ClientRepository;
import ecma.ai.ussdapp.repository.RoleRepository;
import ecma.ai.ussdapp.repository.StaffRepository;
import ecma.ai.ussdapp.repository.UssdCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class DataLoader implements CommandLineRunner {
    @Value("${spring.datasource.initialization-mode}")
    private String initialModel;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    UssdCodeRepository ussdCodeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (initialModel.equals("always")) {
            Role direcRole = new Role();
            direcRole.setRoleName(RoleName.ROLE_DIRECTOR);
            Role staffRole = new Role();
            staffRole.setRoleName(RoleName.ROLE_STAFF);
            Role managerRole = new Role();
            managerRole.setRoleName(RoleName.ROLE_MANAGER);
            roleRepository.save(direcRole);
            roleRepository.save(staffRole);
            roleRepository.save(managerRole);

            Staff director = new Staff();
            director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
            director.setUserName("Falck@123123");
            director.setFullName("Q.N123123");
            director.setPosition("boss");
            director.setPassword("123");
            director.setEnabled(true);
            staffRepository.save(director);

            Client client = new Client();
            client.setClientType(ClientType.USER);
            client.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_CLIENT)));
            client.setFullName("Suhrob");
            client.setPassportNumber("1234567891313");
            clientRepository.save(client);

            List<UssdCode> preAddedUssdCodes = new ArrayList<>();
            UssdCode hisob = new UssdCode();
            hisob.setCode("*100#");
            hisob.setDescription("Hisobni tekshirish");
            preAddedUssdCodes.add(hisob);

            UssdCode internet = new UssdCode();
            internet.setCode("*101#");
            internet.setDescription("Internet qoldig'ini tekshirish");
            preAddedUssdCodes.add(internet);

            UssdCode sms = new UssdCode();
            sms.setCode("*102#");
            sms.setDescription("Sms qoldig'ini tekshirish");
            preAddedUssdCodes.add(sms);

            UssdCode daqiqa = new UssdCode();
            daqiqa.setCode("*103#");
            daqiqa.setDescription("Daqiqa qoldig'ini tekshirish");
            preAddedUssdCodes.add(daqiqa);

            UssdCode internet10Gb = new UssdCode();
            internet10Gb.setCode("*104#");
            internet10Gb.setDescription("10 Gb internet paketini sotib olish");
            preAddedUssdCodes.add(internet10Gb);

            UssdCode minut500 = new UssdCode();
            minut500.setCode("*105#");
            minut500.setDescription("500 minutli paketni paketini sotib olish");
            preAddedUssdCodes.add(minut500);

            ussdCodeRepository.saveAll(preAddedUssdCodes);


        }
    }

}
