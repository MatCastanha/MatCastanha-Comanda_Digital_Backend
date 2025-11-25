package com.ibeus.Comanda.Digital.enums;

public enum OrderStatus {
    RECEIVED,        // 1. Pedido chegou
    IN_PREPARATION,  // 2. Cozinha aceitou e está fazendo
    READY,           // 3. Pronto para retirada/entrega
    DELIVERED        // 4. Finalizado
}
