package com.ibeus.Comanda.Digital.dto;

import lombok.Data;
import com.ibeus.Comanda.Digital.model.Client;
// Adicione o import da Entity de Endereço se você precisar usar a Entity dentro deste DTO
import com.ibeus.Comanda.Digital.model.Address;

@Data
public class ClientDTO {

    private Long cpf; // Note: Na prática, CPF não deveria ser gerado, mas é o seu @Id atual
    private String name;
    private String midName;
    private AddressDTO address; // O Address também deve ser DTO neste nível
    private int addressNumber;
    // O campo 'payment' não estava no seu DTO original, mas se quiser expor, adicione aqui.
    // private String payment;

    /**
     * 🟢 CORREÇÃO: Converte a Entity 'Client' para o DTO 'ClientDTO' (USADO NO GET e POST de retorno)
     * - Este método é 'static', então é chamado diretamente na classe (ClientDTO.fromModel).
     */
    public static ClientDTO fromModel(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.setCpf(client.getCpf());
        dto.setName(client.getName());
        dto.setMidName(client.getMidName());
        dto.setAddressNumber(client.getAddressNumber());

        // Converte a Entity Address para o DTO AddressDTO
        // Note que o AddressDTO também precisa ter o seu próprio método fromModel(Address)
        if (client.getAddress() != null) {
            dto.setAddress(AddressDTO.fromModel(client.getAddress()));
        } else {
            dto.setAddress(null);
        }

        // dto.setPayment(client.getPayment()); // Se 'payment' estiver na Entity e você quiser expor

        return dto;
    }

    /**
     * Converte o DTO 'ClientDTO' para a Entity 'Client' (USADO NA ENTRADA/POST)
     */
    public Client toModel() {
        Client client = new Client();

        // Se o CPF vier no DTO, ele será usado para identificar (se não for gerado)
        client.setCpf(this.cpf);
        client.setName(this.name);
        client.setMidName(this.midName);
        client.setAddressNumber(this.addressNumber);

        // Converte o DTO Address para a Entity Address
        client.setAddress(this.address == null ? null : this.address.toModel());

        // client.setPayment(this.payment); // Se 'payment' estiver no DTO

        return client;
    }
}