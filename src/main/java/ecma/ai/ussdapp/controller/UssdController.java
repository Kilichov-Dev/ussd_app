package ecma.ai.ussdapp.controller;

import ecma.ai.ussdapp.entity.UssdCode;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.service.UssdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ussdCode")
public class UssdController {

    @Autowired
    UssdService ussdService;

    @PostMapping
    public HttpEntity<?> add(@RequestBody UssdCode ussdCode) {
        ApiResponse add = ussdService.add(ussdCode);
        return ResponseEntity.status(add.isSuccess() ? 200 : 409).body(add);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editByEmployeeToChangeTaskStatus
            (@RequestBody UssdCode ussdCode, @PathVariable Integer id) {
        ApiResponse apiResponse = ussdService.edit(id,ussdCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }
    @GetMapping
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(ussdService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        ApiResponse response = ussdService.getOne(id);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }



}
