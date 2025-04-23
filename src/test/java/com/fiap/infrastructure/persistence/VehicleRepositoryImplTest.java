package com.fiap.infrastructure.persistence;

import com.fiap.domain.entity.Vehicle;
import com.fiap.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleRepositoryImplTest {

    @Mock
    private JpaVehicleRepository jpaVehicleRepository;  // Mock do JpaVehicleRepository

    @InjectMocks
    private VehicleRepositoryImpl vehicleRepository; // O mock do JpaVehicleRepository será injetado no VehicleRepositoryImpl

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks antes de cada teste
    }

    @Test
    void whenSaveVehicle_thenVehicleShouldBeSaved() {
        Vehicle vehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONIVEL");

        when(jpaVehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        assertEquals(vehicle.getMarca(), savedVehicle.getMarca());
    }

    @Test
    void whenFindById_thenReturnVehicle() {
        Vehicle vehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONIVEL");

        when(jpaVehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> found = vehicleRepository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(vehicle.getMarca(), found.get().getMarca());
    }

    @Test
    void whenFindSoldVehicles_thenShouldReturnEmptyList() {
        when(jpaVehicleRepository.findByStatusOrderByPrecoAsc("VENDIDO")).thenReturn(List.of());

        List<Vehicle> soldVehicles = vehicleRepository.findSoldVehicles();
        assertTrue(soldVehicles.isEmpty());
    }
}
