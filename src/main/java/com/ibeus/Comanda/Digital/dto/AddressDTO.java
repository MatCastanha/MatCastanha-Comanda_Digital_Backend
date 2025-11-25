package com.ibeus.Comanda.Digital.dto;

import lombok.Data;
import com.ibeus.Comanda.Digital.model.Address;

@Data
public class AddressDTO {

    private Long id;
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    public static AddressDTO fromModel(Address address) {
        if (address == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setCep(address.getCep());
        dto.setLogradouro(address.getLogradouro());
        dto.setBairro(address.getBairro());
        dto.setLocalidade(address.getLocalidade());
        dto.setUf(address.getUf());
        return dto;
    }

    public Address toModel() {
        Address address = new Address();
        address.setId(this.id);
        address.setCep(this.cep);
        address.setLogradouro(this.logradouro);
        address.setBairro(this.bairro);
        address.setLocalidade(this.localidade);
        address.setUf(this.uf);
        return address;
    }

}

