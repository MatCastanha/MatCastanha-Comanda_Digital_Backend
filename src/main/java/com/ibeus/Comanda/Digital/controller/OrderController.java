package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.dto.OrderDTO;
import com.ibeus.Comanda.Digital.enums.OrderStatus;
import com.ibeus.Comanda.Digital.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // 🔹 Criar um novo pedido
    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO dto) {
        OrderDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    // 1. Atualizar para um status específico (Drag & Drop)
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        OrderDTO updatedOrder = service.updateStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // 2. Avançar para a próxima etapa (Botão de fluxo simples)
    @PostMapping("/{id}/next")
    public ResponseEntity<OrderDTO> nextStep(@PathVariable Long id) {
        return ResponseEntity.ok(service.nextStep(id));
    }

    // 3. Voltar etapa (Correção)
    @PostMapping("/{id}/previous")
    public ResponseEntity<OrderDTO> previousStep(@PathVariable Long id) {
        return ResponseEntity.ok(service.previousStep(id));
    }
}