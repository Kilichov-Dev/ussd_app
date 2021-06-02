package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.entity.Filial;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.FilialDto;
import ecma.ai.ussdapp.service.FilialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filial")
public class FilialController {
    @Autowired
    FilialService filialService;

    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> add(FilialDto filialDto) {
        ApiResponse apiResponse = filialService.addFilial(filialDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER')")
    @PutMapping("{id}")
    public HttpEntity<?> editFilial(@PathVariable Integer id, @RequestBody FilialDto filialDto) {
        ApiResponse apiResponse = filialService.editFilial(id, filialDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping("{id}")
    public HttpEntity<?> getFilial(@PathVariable Integer id) {
        ApiResponse apiResponse = filialService.getFilial(id);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getFilialList() {
        List<Filial> filialList = filialService.getFilialList();
        return ResponseEntity.ok(filialList);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER')")
    @DeleteMapping("{id}")
    public HttpEntity<?> deleteFilial(@PathVariable Integer id) {
        ApiResponse apiResponse = filialService.deleteFilial(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
