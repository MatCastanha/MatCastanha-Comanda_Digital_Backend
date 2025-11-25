package com.ibeus.Comanda.Digital.dto;

import com.ibeus.Comanda.Digital.model.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {

    private Long dishId;
    private String dishName;
    private String dishUrlImage; // Nome ajustado para clareza
    private Double price;
    private Integer quantity;
    private Double subTotal;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem entity) {
        // 1. Pega o ID (Lombok gera getId())
        this.dishId = entity.getDish().getId();

        // 2. Pega o Nome (Lombok gera getName())
        this.dishName = entity.getDish().getName();

        // 3. CORREÇÃO AQUI: O seu model usa 'urlImage', então o Lombok gera 'getUrlImage()'
        this.dishUrlImage = entity.getDish().getUrlImage();

        // 4. Preço e Quantidade do Item (não do prato)
        this.price = entity.getPrice();
        this.quantity = entity.getQuantity();
        this.subTotal = entity.getSubTotal();
    }

}