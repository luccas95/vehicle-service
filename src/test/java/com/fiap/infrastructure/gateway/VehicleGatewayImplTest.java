package com.fiap.infrastructure.gateway;

import com.fiap.domain.entity.Vehicle;
import com.fiap.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VehicleGatewayImplTest {

    private VehicleRepository repository;
    private VehicleGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        repository = mock(VehicleRepository.class);
        gateway = new VehicleGatewayImpl(repository);
    }

    @Test
    void shouldSaveVehicle() {
        Vehicle vehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL");
        when(repository.save(vehicle)).thenReturn(vehicle);

        Vehicle saved = gateway.save(vehicle);

        assertEquals(vehicle, saved);
        verify(repository).save(vehicle);
    }

    @Test
    void shouldFindVehicleById() {
        Vehicle vehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL");
        when(repository.findById(1L)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = gateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void shouldFindAvailableVehicles() {
        List<Vehicle> vehicles = List.of(new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL"));
        when(repository.findAvailableVehicles()).thenReturn(vehicles);

        List<Vehicle> result = gateway.findAvailableVehicles();

        assertEquals(vehicles, result);
        verify(repository).findAvailableVehicles();
    }

    @Test
    void shouldFindSoldVehicles() {
        List<Vehicle> vehicles = List.of(new Vehicle("Ford", "Ka", 2019, "Branco", 25000.0, "VENDIDO"));
        when(repository.findSoldVehicles()).thenReturn(vehicles);

        List<Vehicle> result = gateway.findSoldVehicles();

        assertEquals(vehicles, result);
        verify(repository).findSoldVehicles();
    }
}
