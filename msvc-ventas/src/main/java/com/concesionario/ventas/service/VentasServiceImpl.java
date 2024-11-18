package com.concesionario.ventas.service;

import com.cocecionario.coche.dto.CocheDto;
import com.cocecionario.coche.entities.Coche;
import com.concesionario.cliente.dto.CustomerDto;
import com.concesionario.cliente.entities.Customer;
import com.concesionario.ventas.client.CocheClient;
import com.concesionario.ventas.client.CustomerClient;
import com.concesionario.ventas.dto.VentasDto;
import com.concesionario.ventas.entities.Ventas;
import com.concesionario.ventas.repository.VentasRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentasServiceImpl {


    private final CocheClient cocheClient;

    private final CustomerClient customerClient;

    private final VentasRepository ventasRepository;




    public CocheDto obtenerVehiculo(Long cocheId) {
        return cocheClient.getCocheById(cocheId);
    }

    public CustomerDto obtenerCustomer(Long customerId) {
        return customerClient.getCustomerById(customerId);
    }



    public List<VentasDto> getAllVentas(){
        return ventasRepository.findAll().stream().map(Ventas::getDto).collect(Collectors.toList());
    }


    public List<VentasDto> findByFechaBetween(LocalDate fechaInicial, LocalDate fechaFinal){
        List<Ventas> fechaVenta = ventasRepository.findByFechaBetween(fechaInicial, fechaFinal);
        return fechaVenta.stream().map(Ventas::getDto).collect(Collectors.toList());
    }


    public List<VentasDto> getAllVentasByCliente(Long clienteId) {

        // Obtener el cliente desde Feign
        CustomerDto customerDto;
        try{
            customerDto = customerClient.getCustomerById(clienteId);
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("El cliente no existe" + clienteId);
        }catch (FeignException e){
            throw new RuntimeException("Error al obtener el cliente del microservicio " + clienteId, e);
        }

        // Validar que el cliente existe
        if(customerDto == null){
            throw new NoSuchElementException("El cliente no existe" + clienteId);
        }


        // Obtener las ventas del cliente
        List<Ventas> ventasList = ventasRepository.findByCustomerId(clienteId);

        // Validar si no hay ventas asociadas
        if(ventasList.isEmpty()){
            throw new NoSuchElementException("El cliente " + clienteId + " no tiene compras");
        }

        // Convertir a DTO y devolver
        return ventasList.stream().map(Ventas::getDto).collect(Collectors.toList());
    }


    public VentasDto createVentas(VentasDto ventasDto) {
        //validar datos de entrada
        Optional.ofNullable(ventasDto.getCocheId()).orElseThrow(()-> new IllegalArgumentException("El coche es Obligatorio"));
        Optional.ofNullable(ventasDto.getCustomerId()).orElseThrow(()-> new IllegalArgumentException("El cliente es obligatorio"));

        //Obtener datos del coche desde le microservicio
        CocheDto cocheDto = Optional.ofNullable(cocheClient.getCocheById(ventasDto.getCocheId())).orElseThrow(()-> new NoSuchElementException("El coche " + ventasDto.getCocheId() + " no existe"));

        //Obtener datos del customer desde le microservicio
        CustomerDto customerDto = Optional.ofNullable(customerClient.getCustomerById(ventasDto.getCustomerId())).orElseThrow(()-> new NoSuchElementException("El cliente " + ventasDto.getCustomerId() + " no existe"));

        //CRear la entidad
        Coche coche = Optional.of(cocheDto).map(CocheDto::toEntity)
                .orElseThrow(()-> new RuntimeException("Error al obtener el coche"));

        Customer customer = Optional.of(customerDto).map(CustomerDto::toEntity)
                .orElseThrow(()-> new RuntimeException("Error al obtener el cliente"));

        Ventas venta = new Ventas();
        venta.setCoche(coche);
        venta.setCustomer(customer);
        venta.setFechaVenta(ventasDto.getFechaVenta() != null ? ventasDto.getFechaVenta() : LocalDateTime.now());
        venta.setPrecioVentaTotal(ventasDto.getPrecioVentaTotal());
        return ventasRepository.save(venta).getDto();
    }

    public VentasDto updateVenta(Long ventaId, VentasDto ventasDto) {
        //BUscar la venta
        Ventas ventas = ventasRepository.findById(ventaId).orElseThrow(()-> new NoSuchElementException("El venta no existe"));

        //Validad y Actuliar el precio
        if(ventasDto.getPrecioVentaTotal() != null){
            ventas.setPrecioVentaTotal(ventasDto.getPrecioVentaTotal());
        }

        //validar y actualizar coche asociado
        if(ventasDto.getCocheId() != null){
            //Obtener coche actualizado desde microservicio
            CocheDto cocheDto = cocheClient.getCocheById(ventasDto.getCocheId());
            if(ventasDto.getEstadoVehiculo() != null){
                cocheDto.setEstadoVehiculo(ventasDto.getEstadoVehiculo());
            }

            ventas.setCoche(cocheDto.toEntity());
        }
        //Actualizar la venta en la base de datos
        Ventas ventaActualizada = ventasRepository.save(ventas);
        return ventaActualizada.getDto();
    }
}
