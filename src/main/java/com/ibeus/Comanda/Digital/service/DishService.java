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
    private StorageService storageService; // Injeta o serviço que sabe salvar arquivos

    // --- Buscas ---

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Dish findById(Long id) {
        return dishRepository.findById(id)
                // Se não achar, lança erro 404 (Not Found) em vez de erro genérico
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado: " + id));
    }

    public List<Dish> findByName(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Dish> findByCategory(String category) {
        List<Dish> dishes = dishRepository.findByCategoryIgnoreCase(category);
        if (dishes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum prato nesta categoria: " + category);
        }
        return dishes;
    }

    // --- Criação Unificada (Lógica Principal) ---

    public Dish create(DishDTO dishDTO, MultipartFile file) {
        try {
            // 1. Verifica se o usuário enviou uma imagem
            if (file != null && !file.isEmpty()) {
                // Se enviou, chama o StorageService para salvar no disco
                String imageUrl = storageService.store(file);
                // Atualiza o DTO com o caminho da nova imagem
                dishDTO.setUrlImage(imageUrl);
            }
            // Se file for null, ele mantém a URL que talvez já tenha vindo no DTO (ou fica null)

            // 2. Converte DTO -> Entity e salva no banco
            return dishRepository.save(dishDTO.toModel());

        } catch (Exception e) {
            // Captura erros e devolve um 400 Bad Request
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao processar dados do prato", e);
        }
    }

    // --- Atualização ---

    public Dish update(Long id, DishDTO dishDTO) {
        Dish existingDish = findById(id); // Garante que existe antes de atualizar

        // Atualiza campos
        existingDish.setName(dishDTO.getName());
        existingDish.setPrice(dishDTO.getPrice());
        existingDish.setCategory(dishDTO.getCategory());
        existingDish.setDescription(dishDTO.getDescription());

        // Só atualiza a imagem se uma nova URL for passada (útil para updates sem upload)
        if (dishDTO.getUrlImage() != null) {
            existingDish.setUrlImage(dishDTO.getUrlImage());
        }

        return dishRepository.save(existingDish);
    }

    // --- Deleção ---

    public void delete(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado para deletar");
        }
        dishRepository.deleteById(id);
    }
}