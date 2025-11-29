package com.ibeus.Comanda.Digital.dto;

import lombok.Data;

// DTO usado para receber dados do POST /orders/{id}/items
@Data
public class OrderItemInputDTO {

    // O ID do prato (Dish) a ser adicionado
    private Long dishId;

    // A quantidade desejada
    private Integer quantity;

}