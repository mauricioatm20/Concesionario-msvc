package com.concesionario.ventas.client;

import com.concesionario.cliente.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-customer", url = "http://localhost:8084")
public interface CustomerClient {

    @GetMapping("/api/admin/customer/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);
}
