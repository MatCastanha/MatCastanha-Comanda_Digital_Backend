package com.ibeus.Comanda.Digital.model;

import com.ibeus.Comanda.Digital.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data // Gera Getters, Setters, Equals, HashCode, ToString
@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant moment;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // ⚠️ @ToString.Exclude impede que o Lombok tente imprimir a lista inteira
    // e entre em loop infinito com o OrderItem
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Captura o nome do cliente no momento da finalização
    private String clientSnapshotName;

    // Captura o endereço completo (Rua, Número, Bairro, CEP) no momento da finalização
    private String addressSnapshot;

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }
}
