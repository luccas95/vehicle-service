package com.fiap.application.usecase;

import com.fiap.application.gateway.VehicleGateway;
import com.fiap.domain.entity.Vehicle;
import com.fiap.infrastructure.controller.dto.VehicleRequest;
import com.fiap.infrastructure.exception.BusinessException;
import com.fiap.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UpdateVehicleUseCaseTest {

    @Test
    void deveAtualizarVeiculoDisponivelComSucesso() {
        // Arrange
        Vehicle veiculo = new Vehicle("Honda", "Civic", 2022, "Cinza", 80000.0, "DISPONIVEL");
        VehicleRequest request = new VehicleRequest();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setAno(2023);
        request.setCor("Preto");
        request.setPreco(85000.0);

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

        UpdateVehicleUseCase useCase = new UpdateVehicleUseCase(gateway);

        // Act
        Vehicle updatedVehicle = useCase.execute(1L, request);

        // Assert
        assertEquals("Toyota", updatedVehicle.getMarca());
        assertEquals("Corolla", updatedVehicle.getModelo());
        assertEquals(2023, updatedVehicle.getAno());
        assertEquals("Preto", updatedVehicle.getCor());
        assertEquals(85000.0, updatedVehicle.getPreco());
    }

    @Test
    void deveLancarNotFoundExceptionQuandoVeiculoNaoExistir() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setMarca("Toyota");
        request.setModelo("Corolla");
        request.setAno(2023);
        request.setCor("Preto");
        request.setPreco(85000.0);

        VehicleGateway gateway = new VehicleGateway() {
            @Override
            public Vehicle save(Vehicle vehicle) {
                return vehicle;
            }

            @Override
            public Optional<Vehicle> findById(Long id) {
                return Optional.empty(); // Simula o veículo não encontrado
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

        UpdateVehicleUseCase useCase = new UpdateVehicleUseCase(gateway);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.execute(99L, request));
    }

    @Test
    void deveLancarBusinessExceptionQuandoVeiculoJaVendido() {
        // Arrange
        Vehicle veiculo = new Vehicle("Fiat", "Mobi", 2021, "Preto", 35000.0, "VENDIDO");
        VehicleRequest request = new VehicleRequest();
        request.setMarca("Fiat");
        request.setModelo("Uno");
        request.setAno(2022);
        request.setCor("Azul");
        request.setPreco(40000.0);

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

        UpdateVehicleUseCase useCase = new UpdateVehicleUseCase(gateway);

        // Act & Assert
        assertThrows(BusinessException.class, () -> useCase.execute(1L, request));
    }
}
