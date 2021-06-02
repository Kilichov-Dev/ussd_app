package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Filial;
import ecma.ai.ussdapp.entity.Role;
import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.StaffDto;
import ecma.ai.ussdapp.repository.FilialRepository;
import ecma.ai.ussdapp.repository.RoleRepository;
import ecma.ai.ussdapp.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FilialRepository filialRepository;

    public ApiResponse addStaff(StaffDto staffDto) {
        Optional<Role> optionalRole = roleRepository.findById(staffDto.getRoleId());
        if (!optionalRole.isPresent()) {
            return new ApiResponse("Role not found!", false);
        }
        Optional<Filial> optionalFilial = filialRepository.findById(staffDto.getFilialId());
        if (!optionalFilial.isPresent()) {
            return new ApiResponse("Filial not found", false);
        }
        Staff staff = new Staff();
        staff.setFullName(staffDto.getFullName());
        staff.setUserName(staffDto.getUsername());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        staff.setRoles(roleSet);
        staff.setPosition(staffDto.getPosition());
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        staff.setFilial(optionalFilial.get());
        staffRepository.save(staff);
        return new ApiResponse("Stadd added!", false);
    }

    public List<Staff> getStaffList() {
        List<Staff> all = staffRepository.findAll();
        return all;
    }

    public ApiResponse getStaff() {
        Staff staff = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Staff> optionalStaff = staffRepository.findById(staff.getId());
        if (!optionalStaff.isPresent()) {
            return new ApiResponse("Staff not found!", false);
        }
        return new ApiResponse("Staff", true, staff);
    }

    public ApiResponse editStaff(String name, StaffDto staffDto) {
        Optional<Staff> optionalStaff = staffRepository.findByUserName(name);
        if (!optionalStaff.isPresent()) {
            return new ApiResponse("Staff not found!", false);
        }

        Optional<Filial> optionalFilial = filialRepository.findById(staffDto.getFilialId());
        if (!optionalFilial.isPresent()) {
            return new ApiResponse("Filial not found!", false);
        }
        Staff staff = optionalStaff.get();
        staff.setFullName(staffDto.getFullName());
        boolean exists = staffRepository.existsByUserName(staff.getUsername());
        if (exists) {
            return new ApiResponse("Such staff alredy exist", false);
        }

        staff.setUserName(staffDto.getUsername());
        staff.setFilial(optionalFilial.get());
        staff.setPosition(staffDto.getPosition());
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        staffRepository.save(staff);
        return new ApiResponse("Staff edited!", true);
    }

    public ApiResponse deleteStaff(String username) {
        Optional<Staff> optionalStaff = staffRepository.findByUserName(username);
        if (!optionalStaff.isPresent()) {
            return new ApiResponse("Staff not found!", false);
        }
        staffRepository.delete(optionalStaff.get());
        return new ApiResponse("Staff deleted!", false);
    }
}
