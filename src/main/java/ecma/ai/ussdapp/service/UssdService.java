package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Role;
import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.entity.UssdCode;
import ecma.ai.ussdapp.entity.enums.RoleName;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.repository.RoleRepository;
import ecma.ai.ussdapp.repository.UssdCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UssdService {
    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse add(UssdCode ussdCode) {
        Staff worker = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Role manager = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        Role staff = roleRepository.findByRoleName(RoleName.ROLE_STAFF);
        if (worker.getRoles().contains(staff) || worker.getRoles().contains(manager) && worker.getPosition().equalsIgnoreCase("USSD_manger")) {
            return new ApiResponse("Siz yangi USSD qo'sholmaysiz!", false);
        }
        UssdCode ussdCode1 = new UssdCode();
        ussdCode1.setCode(ussdCode.getCode());
        ussdCode1.setDescription(ussdCode.getDescription());
        ussdCodeRepository.save(ussdCode1);
        return new ApiResponse("Yangi USSD qo'shildi!", true);
    }

    public ApiResponse edit(Integer id, UssdCode ussdCode) {
        Staff worker = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Role manager = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        Role staff = roleRepository.findByRoleName(RoleName.ROLE_STAFF);
        if (worker.getRoles().contains(staff) || worker.getRoles().contains(manager) && worker.getPosition().equalsIgnoreCase("USSD_manger")) {
            return new ApiResponse("Siz yangi USSD qo'sholmaysiz!", false);
        }
        Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
        if (!optionalUssdCode.isPresent()) return new ApiResponse("UssdCode not found!", false);
        UssdCode ussdCode1 = optionalUssdCode.get();
        ussdCode1.setCode(ussdCode.getCode());
        ussdCode1.setDescription(ussdCode.getDescription());
        ussdCodeRepository.save(ussdCode1);
        return new ApiResponse("USSDCode editing!", true);
    }

    public ApiResponse getAll() {

        List<UssdCode> all = ussdCodeRepository.findAll();
        return new ApiResponse("All USSDCode ", true, all);
    }

    public ApiResponse getOne(Integer id) {
        Optional<UssdCode> byId = ussdCodeRepository.findById(id);
        return byId.map(ussdCode -> new ApiResponse("Your USSDCode", true, byId)).orElseGet(() -> new ApiResponse("Ussd code not found!", false));
    }


}
