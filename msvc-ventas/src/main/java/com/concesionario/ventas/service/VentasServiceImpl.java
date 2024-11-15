package com.concesionario.ventas.service;

import com.cocecionario.coche.dto.CocheDto;
import com.concesionario.cliente.dto.CustomerDto;
import com.concesionario.ventas.client.CocheClient;
import com.concesionario.ventas.client.CustomerClient;
import com.concesionario.ventas.dto.VentasDto;
import com.concesionario.ventas.entities.Ventas;
import com.concesionario.ventas.repository.VentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentasServiceImpl {

    @Autowired
    private CocheClient cocheClient;

    @Autowired
    private CustomerClient clienteClient;

    @Autowired
    private VentasRepository ventasRepository;

    public CocheDto obtenerVehiculo(Long id) {
        return cocheClient.getCocheById(id);
    }

    public CustomerDto obtenerCustomer(Long customerId) {
        return clienteClient.getCustomerById(customerId);
    }


    public List<VentasDto> getAllVentas(){
        return ventasRepository.findAll().stream().map(Ventas::getDto).collect(Collectors.toList());
    }
}
