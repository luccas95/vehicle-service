package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import com.fiap.infrastructure.exception.BusinessException;
import com.fiap.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SellVehicleUseCaseTest {

    @Test
    void deveVenderVeiculoDisponivelComSucesso() {
        // Arrange
        Vehicle veiculo = new Vehicle("Toyota", "Corolla", 2022, "Branco", 85000.0, "DISPONIVEL");

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

        SellVehicleUseCase useCase = new SellVehicleUseCase(gateway);

        // Act
        Vehicle vendido = useCase.execute(1L);

        // Assert
        assertEquals("VENDIDO", vendido.getStatus());
    }

    @Test
    void deveLancarNotFoundExceptionQuandoVeiculoNaoExistir() {
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

        SellVehicleUseCase useCase = new SellVehicleUseCase(gateway);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.execute(99L));
    }

    @Test
    void deveLancarBusinessExceptionQuandoVeiculoJaVendido() {
        // Arrange
        Vehicle veiculo = new Vehicle("Fiat", "Mobi", 2021, "Preto", 35000.0, "VENDIDO");

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

        SellVehicleUseCase useCase = new SellVehicleUseCase(gateway);

        // Act & Assert
        assertThrows(BusinessException.class, () -> useCase.execute(1L));
    }
}
