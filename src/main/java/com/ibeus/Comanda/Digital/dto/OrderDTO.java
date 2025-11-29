package com.ibeus.Comanda.Digital.dto;

import com.ibeus.Comanda.Digital.enums.OrderStatus;
import com.ibeus.Comanda.Digital.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data // Gera Getters, Setters, ToString, etc.
@NoArgsConstructor // Construtor vazio para desserialização
public class OrderDTO implements Serializable {

    private Long id;
    private Long clientId;
    private OrderStatus status;
    private Double total;
    private List<OrderItemDTO> items;

    // --- 💡 CAMPOS DE SNAPSHOT (Histórico Imutável) ---
    // Estes campos são preenchidos na finalização e são imunes a futuras alterações no cadastro do cliente.
    private String clientSnapshotName;
    private String addressSnapshot;
    // ----------------------------------------------------

    /**
     * Construtor para converter a Entidade Order (Model) no Objeto de Transferência (DTO).
     */
    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.status = entity.getStatus();
        this.total = entity.getTotal();
        this.clientId = entity.getClient() != null ? entity.getClient().getId() : null; // Pega o ID do cliente

        // Mapeia a lista de itens relacionados
        this.items = entity.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());

        // --- 🎯 LÓGICA DE DECISÃO DO SNAPSHOT 🎯 ---
        if (entity.getStatus() == OrderStatus.DRAFT) {
            // 1. SE É RASCUNHO: Busca dados ATUAIS e VINCULADOS
            this.clientSnapshotName = entity.getClient() != null ? entity.getClient().getName() : "Cliente não definido";
            this.addressSnapshot = "Dados de endereço serão fixados após a finalização.";

        } else {
            // 2. SE ESTÁ FINALIZADO (RECEIVED ou superior): Usa os dados CONGELADOS
            this.clientSnapshotName = entity.getClientSnapshotName();
            this.addressSnapshot = entity.getAddressSnapshot();
        }
        // -------------------------------------------
    }
}