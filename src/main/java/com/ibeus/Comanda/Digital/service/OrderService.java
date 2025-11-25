package com.ibeus.Comanda.Digital.service;

import com.ibeus.Comanda.Digital.dto.OrderDTO;
import com.ibeus.Comanda.Digital.dto.OrderItemDTO;
import com.ibeus.Comanda.Digital.enums.OrderStatus;
import com.ibeus.Comanda.Digital.model.Client;
import com.ibeus.Comanda.Digital.model.Dish;
import com.ibeus.Comanda.Digital.model.Order;
import com.ibeus.Comanda.Digital.model.OrderItem;
import com.ibeus.Comanda.Digital.repository.ClientRepository;
import com.ibeus.Comanda.Digital.repository.DishRepository;
import com.ibeus.Comanda.Digital.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ClientRepository clientRepository;
    private final DishRepository dishRepository;

    public OrderService(OrderRepository repository,
                        ClientRepository clientRepository,
                        DishRepository dishRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.dishRepository = dishRepository;
    }

    // 🔹 CRIAR PEDIDO
    @Transactional
    public OrderDTO create(OrderDTO dto) {
        // buscar o cliente pelo CPF que está no ClientDTO
        if (dto.getClient() == null || dto.getClient().getCpf() == null) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório para criar pedido");
        }

        Client client = clientRepository.findById(dto.getClient().getCpf())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + dto.getClient().getCpf()));

        Order order = new Order();
        order.setClient(client);
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.RECEIVED);

        // monta itens do pedido a partir dos DTOs
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Dish dish = dishRepository.findById(itemDTO.getDishId())
                    .orElseThrow(() -> new EntityNotFoundException("Prato não encontrado: " + itemDTO.getDishId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setDish(dish);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(dish.getPrice()); // assumindo que OrderItem tem price
            // se o subtotal é calculado no @PrePersist, nem precisa setar aqui

            order.getItems().add(item);
        }

        // se sua entidade Order calcula total em @PrePersist/@PreUpdate, ok.
        // se não, pode calcular aqui também se quiser.

        Order saved = repository.save(order);
        return new OrderDTO(saved);
    }

    // 🔹 Atualiza para um status específico (Drag & Drop)
    @Transactional
    public OrderDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));

        order.setStatus(newStatus);
        Order updated = repository.save(order);

        return new OrderDTO(updated); // 🔥 CONVERSÃO
    }

    // 🔹 Avançar automaticamente por etapas
    @Transactional
    public OrderDTO nextStep(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        switch (order.getStatus()) {
            case RECEIVED -> order.setStatus(OrderStatus.IN_PREPARATION);
            case IN_PREPARATION -> order.setStatus(OrderStatus.READY);
            case READY -> order.setStatus(OrderStatus.DELIVERED);
            case DELIVERED -> throw new IllegalStateException("Pedido já foi entregue!");
        }

        Order updated = repository.save(order);
        return new OrderDTO(updated);
    }

    // 🔹 Retroceder automaticamente
    @Transactional
    public OrderDTO previousStep(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        switch (order.getStatus()) {
            case DELIVERED -> order.setStatus(OrderStatus.READY);
            case READY -> order.setStatus(OrderStatus.IN_PREPARATION);
            case IN_PREPARATION -> order.setStatus(OrderStatus.RECEIVED);
            case RECEIVED -> throw new IllegalStateException("Pedido já está no início!");
        }

        Order updated = repository.save(order);
        return new OrderDTO(updated);
    }
}
