package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import com.fiap.infrastructure.controller.dto.VehicleRequest;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Implementação simples e in-memory do VehicleGateway
class InMemoryVehicleGateway implements VehicleGateway {
    @Override
    public Vehicle save(Vehicle vehicle) {
        // Simula o comportamento de salvar, apenas retorna o veículo
        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return Optional.empty();  // Retorna vazio para o exemplo
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return List.of();  // Retorna uma lista vazia
    }

    @Override
    public List<Vehicle> findSoldVehicles() {
        return List.of();  // Retorna uma lista vazia
    }
}

public class CreateVehicleUseCaseTest {

    @Test
    void shouldCreateAndSaveVehicleSuccessfully() {
        // Instanciando a implementação simples do gateway
        VehicleGateway gateway = new InMemoryVehicleGateway();
        CreateVehicleUseCase createVehicleUseCase = new CreateVehicleUseCase(gateway);

        // Criando o VehicleRequest com dados
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setMarca("Fiat");
        vehicleRequest.setModelo("Uno");
        vehicleRequest.setAno(2020);
        vehicleRequest.setCor("Prata");
        vehicleRequest.setPreco(30000.0);

        // Executando o caso de uso
        Vehicle result = createVehicleUseCase.execute(vehicleRequest);

        // Verificações básicas
        assertNotNull(result);
        assertEquals("Fiat", result.getMarca());
        assertEquals("Uno", result.getModelo());
        assertEquals(2020, result.getAno());
        assertEquals("Prata", result.getCor());
        assertEquals(30000.0, result.getPreco(), 0.01);
        assertEquals("DISPONIVEL", result.getStatus());
    }
}
