package com.concesionario.ventas.client;

import com.cocecionario.coche.dto.CocheDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "msvc-coche", url = "http://localhost:8081")
public interface CocheClient {

    @GetMapping("/api/admin/coche/{id}")
    CocheDto getCocheById(@PathVariable("id") Long id);

}
