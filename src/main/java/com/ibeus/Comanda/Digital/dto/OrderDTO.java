package com.ibeus.Comanda.Digital.dto;

import com.ibeus.Comanda.Digital.model.Order;
import com.ibeus.Comanda.Digital.enums.OrderStatus;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;

    private ClientDTO client;

    private List<OrderItemDTO> items = new ArrayList<>();

    private Double total;

    public OrderDTO() {}

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.status = entity.getStatus();

        // Conversão correta do Client
        if (entity.getClient() != null) {
            this.client = ClientDTO.fromModel(entity.getClient());
        }

        // Conversão dos itens
        entity.getItems().forEach(item ->
                items.add(new OrderItemDTO(item))
        );

        // Total do pedido
        this.total = entity.getTotal();
    }

    // getters e setters (pode ser com Lombok @Data também)
}
