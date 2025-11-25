package com.ibeus.Comanda.Digital.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "tb_order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Proteção JSON
    @ToString.Exclude // Proteção do Lombok contra Loops
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    // --- Métodos Customizados ---

    public Double getSubTotal() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0.0;
    }
}