package com.ibeus.Comanda.Digital.service;

import com.ibeus.Comanda.Digital.model.Address; // Importante
import com.ibeus.Comanda.Digital.model.Client;
import com.ibeus.Comanda.Digital.repository.AddressRepository; // Importante
import com.ibeus.Comanda.Digital.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository; // Injete o repositório de endereço

    public Client getClient() {
        return clientRepository.findAll().stream().findFirst().orElse(null);
    }

    @Transactional // Garante que tudo roda na mesma transação
    public Client saveOrUpdate(Client clientData) {

        // 1. Tenta recuperar o cliente existente
        Optional<Client> existingClientOpt = clientRepository.findAll().stream().findFirst();
        Client clientToSave;

        if (existingClientOpt.isPresent()) {
            clientToSave = existingClientOpt.get();
            // Atualiza dados básicos
            clientToSave.setCpf(clientData.getCpf());
            clientToSave.setName(clientData.getName());
            clientToSave.setMidName(clientData.getMidName());
            clientToSave.setAddressNumber(clientData.getAddressNumber());
            clientToSave.setComplement(clientData.getComplement());
        } else {
            clientToSave = clientData;
        }

        // 2. LÓGICA DE VÍNCULO DE ENDEREÇO CORRIGIDA
        // Verifica se já existe um endereço salvo no banco (pelo AddressController)
        Optional<Address> existingAddressOpt = addressRepository.findAll().stream().findFirst();

        if (existingAddressOpt.isPresent()) {
            // Se já existe endereço no banco, vinculamos este endereço ao cliente
            clientToSave.setAddress(existingAddressOpt.get());
        } else if (clientData.getAddress() != null) {
            // Se não existe no banco, mas veio no JSON, deixamos o Cascade salvar
            clientToSave.setAddress(clientData.getAddress());
        }

        return clientRepository.save(clientToSave);
    }
}