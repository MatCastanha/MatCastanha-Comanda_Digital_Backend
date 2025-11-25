package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.dto.ClientDTO;
import com.ibeus.Comanda.Digital.model.Client;
import com.ibeus.Comanda.Digital.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<ClientDTO> getClient() {
        // 1. Busca a Entity no serviço
        Client clientEntity = clientService.getClient();

        // 2. Se não existir, retorna 404 (Not Found)
        if (clientEntity == null) {
            return ResponseEntity.notFound().build();
        }

        // 3. Converte Entity -> DTO para devolver ao front-end
        ClientDTO dto = ClientDTO.fromModel(clientEntity);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> saveOrUpdate(@RequestBody ClientDTO clientDTO) {
        // 1. Converte DTO -> Entity para enviar ao serviço
        Client clientEntity = clientDTO.toModel();

        // 2. O serviço processa e salva (com a correção de vínculo de endereço que fizemos antes)
        Client savedClient = clientService.saveOrUpdate(clientEntity);

        // 3. Converte a Entity salva de volta para DTO para confirmar ao front-end o que foi salvo
        ClientDTO resultDTO = ClientDTO.fromModel(savedClient);

        return ResponseEntity.ok(resultDTO);
    }
}
