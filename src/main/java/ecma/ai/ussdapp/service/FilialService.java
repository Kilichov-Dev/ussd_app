package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Filial;
import ecma.ai.ussdapp.entity.Role;
import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.entity.enums.RoleName;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.FilialDto;
import ecma.ai.ussdapp.repository.FilialRepository;
import ecma.ai.ussdapp.repository.RoleRepository;
import ecma.ai.ussdapp.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleResult;
import java.util.*;

@Service
public class FilialService {

    @Autowired
    FilialRepository filialRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StaffRepository staffRepository;

    public ApiResponse addFilial(FilialDto filialDto) {
        Filial filial = new Filial();
        filial.setName(filialDto.getName());

        Staff staff = new Staff();
        staff.setUserName(filialDto.getDirecUserName());
        staff.setFullName(filialDto.getDirecFullName());
        Role role = new Role(1, RoleName.ROLE_DIRECTOR);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        staff.setRoles(roleSet);
        staff.setPosition(filialDto.getDirecPosition());
//        staff.setFilial(filial);
        staff.setPassword(passwordEncoder.encode(filialDto.getDirecPassword()));
        filial.setDirector(staff);

        List<Staff> staffList = new ArrayList<>();
        for (String staffUsername : filialDto.getStaffUsernames()) {
            Optional<Staff> optionalStaff = staffRepository.findByUserName(staffUsername);
            if (!optionalStaff.isPresent()) {
                return new ApiResponse("Staff mavjud emas!", false);
            }
            staffList.add(optionalStaff.get());
        }
        filial.setStaffs(staffList);

        filialRepository.save(filial);
        return new ApiResponse("Filial added!", true);
    }

    public ApiResponse editFilial(Integer id, FilialDto filialDto) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (!optionalFilial.isPresent()) {
            return new ApiResponse("Filial not found!", false);
        }

        Filial filial = optionalFilial.get();
        filial.setName(filialDto.getName());
        Staff staff = new Staff();
        staff.setUserName(filialDto.getDirecUserName());
        staff.setFullName(filialDto.getDirecFullName());
        Role role = new Role(1, RoleName.ROLE_DIRECTOR);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        staff.setRoles(roleSet);
        staff.setPosition(filialDto.getDirecPosition());
//        staff.setFilial(filial);
        staff.setPassword(passwordEncoder.encode(filialDto.getDirecPassword()));
        filial.setDirector(staff);

        List<Staff> staffList = new ArrayList<>();
        for (String staffUsername : filialDto.getStaffUsernames()) {
            Optional<Staff> optionalStaff = staffRepository.findByUserName(staffUsername);
            if (!optionalStaff.isPresent()) {
                return new ApiResponse("Staff mavjud emas!", false);
            }
            staffList.add(optionalStaff.get());
        }
        filial.setStaffs(staffList);

        filialRepository.save(filial);
        return new ApiResponse("Filial edited!", true);
    }

    public List<Filial> getFilialList(){
        List<Filial> filialList = filialRepository.findAll();
        return filialList;
    }

    public ApiResponse getFilial(Integer id) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        return optionalFilial.map(filial -> new ApiResponse("Success", true, filial)).orElseGet(() -> new ApiResponse("Such filial doesnt exist", false));
    }

    public ApiResponse deleteFilial(Integer id) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (!optionalFilial.isPresent()) {
            return new ApiResponse("Filial not found!", false);
        }
        filialRepository.deleteById(id);
        return new ApiResponse("Filial deleted!", true);
    }

}
