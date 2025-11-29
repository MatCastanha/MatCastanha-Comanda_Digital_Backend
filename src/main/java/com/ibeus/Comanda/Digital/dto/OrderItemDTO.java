package com.ibeus.Comanda.Digital.dto;

import com.ibeus.Comanda.Digital.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Double price;
    private Double subTotal;

    public OrderItemDTO(OrderItem entity) {
        this.id = entity.getId();
        this.dishId = entity.getDish().getId();
        this.dishName = entity.getDish().getName();
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
        this.subTotal = entity.getSubTotal();
    }

}