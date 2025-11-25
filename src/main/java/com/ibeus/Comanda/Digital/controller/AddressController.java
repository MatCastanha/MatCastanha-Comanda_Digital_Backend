package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.dto.AddressDTO;
import com.ibeus.Comanda.Digital.model.Address;
import com.ibeus.Comanda.Digital.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<AddressDTO> getAddress() {
        Address address = addressService.getAddress();
        if (address == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(AddressDTO.fromModel(address));
    }

    // Busca CEP na API externa e retorna DTO
    @GetMapping("/{cep}")
    public ResponseEntity<AddressDTO> getAddressByCep(@PathVariable String cep) {
        Address address = addressService.buscarPorCep(cep);
        return ResponseEntity.ok(AddressDTO.fromModel(address));
    }

    // Salva e retorna DTO
    @PostMapping("/{cep}")
    public ResponseEntity<AddressDTO> saveOrUpdateByCep(@PathVariable String cep) {
        Address address = addressService.salvarOuAtualizarPorCep(cep);
        return ResponseEntity.ok(AddressDTO.fromModel(address));
    }
}