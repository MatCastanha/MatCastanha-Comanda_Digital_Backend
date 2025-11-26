package com.ibeus.Comanda.Digital.service;

import com.ibeus.Comanda.Digital.dto.DishDTO;
import com.ibeus.Comanda.Digital.model.Dish;
import com.ibeus.Comanda.Digital.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private StorageService storageService; // Injeção do novo serviço

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Dish findById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado: " + id));
    }

    public List<Dish> findByName(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Dish> findByCategory(String category) {
        List<Dish> dishes = dishRepository.findByCategoryIgnoreCase(category);
        if (dishes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum prato na categoria: " + category);
        }
        return dishes;
    }

    // --- Lógica de Criação Unificada (Sem ObjectMapper) ---
    public Dish create(DishDTO dishDTO, MultipartFile file) {
        try {
            // Se veio arquivo, salva e atualiza a URL
            if (file != null && !file.isEmpty()) {
                String imageUrl = storageService.store(file);
                dishDTO.setUrlImage(imageUrl);
            }

            // Converte e Salva no banco
            return dishRepository.save(dishDTO.toModel());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar prato", e);
        }
    }

    public Dish update(Long id, DishDTO dishDTO) {
        Dish existingDish = findById(id);

        // Atualiza campos
        existingDish.setName(dishDTO.getName());
        existingDish.setPrice(dishDTO.getPrice());
        existingDish.setCategory(dishDTO.getCategory());
        existingDish.setDescription(dishDTO.getDescription());

        // Permite atualizar a URL manualmente ou por upload (neste caso, por DTO)
        if (dishDTO.getUrlImage() != null) {
            existingDish.setUrlImage(dishDTO.getUrlImage());
        }

        return dishRepository.save(existingDish);
    }

    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado para deletar");
        }
        dishRepository.deleteById(id);
    }
}