package com.ibeus.Comanda.Digital.controller;

import com.ibeus.Comanda.Digital.dto.DishDTO;
import com.ibeus.Comanda.Digital.model.Dish;
import com.ibeus.Comanda.Digital.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
@CrossOrigin(origins = "http://localhost:4200")
public class DishController {

    @Autowired
    private DishService dishService;

    // MÉTODOS GET
    // (Mapeamentos de busca foram atualizados para retornar DTOs)

    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> list = dishService.findAll().stream()
                .map(DishDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(DishDTO.fromModel(dishService.findById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishDTO>> getDishByName(@RequestParam("name") String name) {
        List<DishDTO> list = dishService.findByName(name).stream()
                .map(DishDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<DishDTO>> getDishByCategory(@PathVariable String category) {
        List<DishDTO> list = dishService.findByCategory(category).stream()
                .map(DishDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // --- Endpoint ÚNICO para Criação (Upload ou URL) ---
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DishDTO> createDish(
            // Usa @ModelAttribute para mapear campos de texto diretamente no DTO
            @ModelAttribute DishDTO dishDTO,
            // Recebe o arquivo, se existir (opcional)
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Dish savedDish = dishService.create(dishDTO, file);
        return ResponseEntity.ok(DishDTO.fromModel(savedDish));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        Dish updated = dishService.update(id, dishDTO);
        return ResponseEntity.ok(DishDTO.fromModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}