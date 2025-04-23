package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import com.fiap.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindVehicleByIdUseCaseTest {

    @Test
    void deveRetornarVeiculoQuandoIdExistir() {
        // Arrange
        Vehicle veiculo = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONIVEL");

        VehicleGateway gateway = new VehicleGateway() {
            @Override
            public Vehicle save(Vehicle vehicle) {
                return vehicle;
            }

            @Override
            public Optional<Vehicle> findById(Long id) {
                return Optional.of(veiculo);
            }

            @Override
            public List<Vehicle> findAvailableVehicles() {
                return List.of();
            }

            @Override
            public List<Vehicle> findSoldVehicles() {
                return List.of();
            }
        };

        FindVehicleByIdUseCase useCase = new FindVehicleByIdUseCase(gateway);

        // Act
        Vehicle result = useCase.execute(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Fiat", result.getMarca());
        assertEquals("Uno", result.getModelo());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        VehicleGateway gateway = new VehicleGateway() {
            @Override
            public Vehicle save(Vehicle vehicle) {
                return vehicle;
            }

            @Override
            public Optional<Vehicle> findById(Long id) {
                return Optional.empty();
            }

            @Override
            public List<Vehicle> findAvailableVehicles() {
                return List.of();
            }

            @Override
            public List<Vehicle> findSoldVehicles() {
                return List.of();
            }
        };

        FindVehicleByIdUseCase useCase = new FindVehicleByIdUseCase(gateway);

        // Act + Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(999L));
        assertEquals("Veículo não encontrado com o ID informado", ex.getMessage());
    }
}
