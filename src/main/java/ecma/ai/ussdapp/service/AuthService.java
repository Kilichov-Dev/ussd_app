package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.entity.Client;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.Staff;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.LoginDto;
import ecma.ai.ussdapp.repository.SimCardRepository;
import ecma.ai.ussdapp.repository.StaffRepository;
import ecma.ai.ussdapp.security.JwtFilter;
import ecma.ai.ussdapp.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    SimCardRepository simCardRepository;

    public ApiResponse loginForStaff(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            Staff staff = (Staff) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), staff.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (Exception e) {
            return new ApiResponse("Password or username error!", false);
        }
    }

    public ApiResponse loginForClient(LoginDto loginDto) {
        try {

            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            Client client = (Client) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), client.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (Exception e) {
            return new ApiResponse("Password or username error!", false);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Staff> optionalStaff = staffRepository.findByUserName(username);
        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(username.substring(0, 2), username.substring(3));

        if (optionalStaff.isPresent()) {
            return optionalStaff.get();
        }
        if (optionalSimCard.isPresent()) {
            return optionalSimCard.get();
        }
        throw new UsernameNotFoundException(username + " not found!");
    }

    public UserDetails loadClientByUserNameFromSimCard(String simCardNumber) {
        SimCard simCard = simCardRepository.findBySimCardNumber(simCardNumber).orElseThrow(() -> new UsernameNotFoundException("Error!"));
        return simCard;

    }
}
