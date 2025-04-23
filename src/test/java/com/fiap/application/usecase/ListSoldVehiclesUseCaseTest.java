package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ListSoldVehiclesUseCaseTest {

    @Test
    void deveRetornarListaDeVeiculosVendidos() {
        // Arrange
        Vehicle veiculo1 = new Vehicle("Ford", "Ka", 2015, "Vermelho", 25000.0, "VENDIDO");
        Vehicle veiculo2 = new Vehicle("Chevrolet", "Onix", 2019, "Cinza", 40000.0, "VENDIDO");

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
                return List.of(veiculo1, veiculo2);
            }
        };

        ListSoldVehiclesUseCase useCase = new ListSoldVehiclesUseCase(gateway);

        // Act
        List<Vehicle> vendidos = useCase.execute();

        // Assert
        assertEquals(2, vendidos.size());
        assertEquals("Ford", vendidos.get(0).getMarca());
        assertEquals("Chevrolet", vendidos.get(1).getMarca());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverVeiculosVendidos() {
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

        ListSoldVehiclesUseCase useCase = new ListSoldVehiclesUseCase(gateway);

        // Act
        List<Vehicle> vendidos = useCase.execute();

        // Assert
        assertTrue(vendidos.isEmpty());
    }
}
