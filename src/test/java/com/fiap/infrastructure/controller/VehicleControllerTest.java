package com.fiap.infrastructure.controller;

import com.fiap.application.usecase.*;
import com.fiap.domain.entity.Vehicle;
import com.fiap.infrastructure.controller.dto.VehicleRequest;
import com.fiap.infrastructure.controller.dto.VehicleResponse;
import com.fiap.application.gateway.VehicleGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleControllerTest {

    private VehicleController controller;

    // Mockando o VehicleGateway e os Use Cases
    private VehicleGateway vehicleGateway;
    private CreateVehicleUseCase createVehicleUseCase;
    private UpdateVehicleUseCase updateVehicleUseCase;
    private ListAvailableVehiclesUseCase listAvailableVehiclesUseCase;
    private ListSoldVehiclesUseCase listSoldVehiclesUseCase;
    private SellVehicleUseCase sellVehicleUseCase;
    private FindVehicleByIdUseCase findVehicleByIdUseCase;

    @BeforeEach
    public void setUp() {
        // Criando mocks
        vehicleGateway = mock(VehicleGateway.class);
        createVehicleUseCase = new CreateVehicleUseCase(vehicleGateway);
        updateVehicleUseCase = new UpdateVehicleUseCase(vehicleGateway);
        listAvailableVehiclesUseCase = new ListAvailableVehiclesUseCase(vehicleGateway);
        listSoldVehiclesUseCase = new ListSoldVehiclesUseCase(vehicleGateway);
        sellVehicleUseCase = new SellVehicleUseCase(vehicleGateway);
        findVehicleByIdUseCase = new FindVehicleByIdUseCase(vehicleGateway);

        // Criando o controller com as dependências simuladas
        controller = new VehicleController(
                createVehicleUseCase,
                updateVehicleUseCase,
                listAvailableVehiclesUseCase,
                listSoldVehiclesUseCase,
                sellVehicleUseCase,
                findVehicleByIdUseCase
        );
    }

    @Test
    public void testCreateVehicle() {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setMarca("Fiat");
        vehicleRequest.setModelo("Uno");
        vehicleRequest.setAno(2020);
        vehicleRequest.setCor("Prata");
        vehicleRequest.setPreco(30000.0);

        // Mockando a resposta do VehicleGateway
        Vehicle savedVehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL");
        when(vehicleGateway.save(Mockito.any(Vehicle.class))).thenReturn(savedVehicle);

        // Simulando a criação do veículo
        VehicleResponse vehicleResponse = controller.create(vehicleRequest);

        // Teste básico: validando as informações do veículo
        assertEquals("Fiat", vehicleResponse.getMarca());
        assertEquals("Uno", vehicleResponse.getModelo());
        assertEquals(2020, vehicleResponse.getAno());
        assertEquals("Prata", vehicleResponse.getCor());
        assertEquals(30000.0, vehicleResponse.getPreco());
        assertEquals("DISPONÍVEL", vehicleResponse.getStatus());
    }

    @Test
    public void testUpdateVehicle() {
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setMarca("Fiat");
        vehicleRequest.setModelo("Uno");
        vehicleRequest.setAno(2020);
        vehicleRequest.setCor("Prata");
        vehicleRequest.setPreco(30000.0);

        // Mockando o veículo encontrado
        Vehicle updatedVehicle = new Vehicle("Fiat", "Uno", 2021, "Prata", 31000.0, "DISPONÍVEL");
        when(vehicleGateway.findById(Mockito.anyLong())).thenReturn(Optional.of(new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL")));
        when(vehicleGateway.save(Mockito.any(Vehicle.class))).thenReturn(updatedVehicle);

        // Simulando a atualização do veículo
        VehicleResponse vehicleResponse = controller.update(1L, vehicleRequest);

        // Teste de atualização: validando se o ano foi atualizado
        assertEquals(2021, vehicleResponse.getAno());
        assertEquals(31000.0, vehicleResponse.getPreco());
    }

    @Test
    public void testFindById() {
        // Mockando o comportamento do FindVehicleByIdUseCase
        Vehicle vehicle = new Vehicle("Fiat", "Uno", 2020, "Prata", 30000.0, "DISPONÍVEL");
        when(vehicleGateway.findById(Mockito.anyLong())).thenReturn(Optional.of(vehicle));

        // Teste básico de busca por ID
        VehicleResponse vehicleResponse = controller.findById(1L).getBody();

        assertEquals("Fiat", vehicleResponse.getMarca());
        assertEquals("Uno", vehicleResponse.getModelo());
        assertEquals(2020, vehicleResponse.getAno());
        assertEquals("Prata", vehicleResponse.getCor());
        assertEquals(30000.0, vehicleResponse.getPreco());
        assertEquals("DISPONÍVEL", vehicleResponse.getStatus());
    }
}
