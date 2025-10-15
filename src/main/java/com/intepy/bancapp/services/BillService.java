package com.intepy.bancapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intepy.bancapp.entities.enums.ServiceType;
import com.intepy.bancapp.repositories.ServiceRepository;

@Service
public class BillService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<com.intepy.bancapp.entities.Service> listServices() {
        return serviceRepository.findAll();
    }

    public Optional<com.intepy.bancapp.entities.Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public com.intepy.bancapp.entities.Service saveService(com.intepy.bancapp.entities.Service service) {
        // Validate that the service type is not null
        if (service.getName() == null) {
            throw new RuntimeException("Service type is required");
        }

        return serviceRepository.save(service);
    }

    public com.intepy.bancapp.entities.Service updateService(Long id, com.intepy.bancapp.entities.Service updatedService) {
        return serviceRepository.findById(id)
                .map(service -> {
                    // Validate that the service type is not null
                    if (updatedService.getName() == null) {
                        throw new RuntimeException("Service type is required");
                    }

                    service.setName(updatedService.getName());
                    return serviceRepository.save(service);
                })
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    // Additional method to search services by type
    public Optional<com.intepy.bancapp.entities.Service> findByType(ServiceType type) {
        return serviceRepository.findAll().stream()
                .filter(service -> service.getName().equals(type))
                .findFirst();
    }

    // Method to check if a service exists
    public boolean serviceExists(Long id) {
        return serviceRepository.existsById(id);
    }

    // Method to check if a service of a specific type already exists
    public boolean serviceTypeExists(ServiceType type) {
        return serviceRepository.findAll().stream()
                .anyMatch(service -> service.getName().equals(type));
    }
}
