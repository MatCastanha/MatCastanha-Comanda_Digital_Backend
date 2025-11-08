package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cep")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    @Autowired
    private AddressService cepService;

    @GetMapping("/{cep}")
    public Map<String, Object> getEnderecoPorCep(@PathVariable String cep) {
        return cepService.buscarEnderecoPorCep(cep);
    }
}