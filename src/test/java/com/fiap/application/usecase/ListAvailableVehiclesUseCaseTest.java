package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ListAvailableVehiclesUseCaseTest {

    @Test
    void deveRetornarListaDeVeiculosDisponiveis() {
        // Arrange
        Vehicle veiculo1 = new Vehicle("Honda", "Civic", 2018, "Preto", 55000.0, "DISPONIVEL");
        Vehicle veiculo2 = new Vehicle("Toyota", "Corolla", 2020, "Branco", 67000.0, "DISPONIVEL");

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
                return List.of(veiculo1, veiculo2);
            }

            @Override
            public List<Vehicle> findSoldVehicles() {
                return List.of();
            }
        };

        ListAvailableVehiclesUseCase useCase = new ListAvailableVehiclesUseCase(gateway);

        // Act
        List<Vehicle> resultado = useCase.execute();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Honda", resultado.get(0).getMarca());
        assertEquals("Toyota", resultado.get(1).getMarca());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverVeiculosDisponiveis() {
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

        ListAvailableVehiclesUseCase useCase = new ListAvailableVehiclesUseCase(gateway);

        // Act
        List<Vehicle> resultado = useCase.execute();

        // Assert
        assertTrue(resultado.isEmpty());
    }
}
