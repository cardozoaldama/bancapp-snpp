package com.intepy.bancapp.servicies;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.Servicio;
import com.intepy.bancapp.entities.enums.TipoServicio;
import com.intepy.bancapp.repositories.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> obtenerServicioPorId(Long id) {
        return servicioRepository.findById(id);
    }

    public Servicio guardarServicio(Servicio servicio) {
        // Validar que el tipo de servicio no sea nulo
        if (servicio.getNombre() == null) {
            throw new RuntimeException("El tipo de servicio es requerido");
        }

        return servicioRepository.save(servicio);
    }

    public Servicio actualizarServicio(Long id, Servicio servicioActualizado) {
        return servicioRepository.findById(id)
                .map(servicio -> {
                    // Validar que el tipo de servicio no sea nulo
                    if (servicioActualizado.getNombre() == null) {
                        throw new RuntimeException("El tipo de servicio es requerido");
                    }

                    servicio.setNombre(servicioActualizado.getNombre());
                    return servicioRepository.save(servicio);
                })
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }

    // Método adicional para buscar servicios por tipo
    public Optional<Servicio> buscarPorTipo(TipoServicio tipo) {
        return servicioRepository.findAll().stream()
                .filter(servicio -> servicio.getNombre().equals(tipo))
                .findFirst();
    }

    // Método para verificar si un servicio existe
    public boolean existeServicio(Long id) {
        return servicioRepository.existsById(id);
    }

    // Método para verificar si ya existe un servicio de un tipo específico
    public boolean existeTipoServicio(TipoServicio tipo) {
        return servicioRepository.findAll().stream()
                .anyMatch(servicio -> servicio.getNombre().equals(tipo));
    }
}