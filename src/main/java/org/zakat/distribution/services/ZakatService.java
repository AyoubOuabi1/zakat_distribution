package org.zakat.distribution.services;

import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.ZakatDTO;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;
import org.zakat.distribution.repositories.ZakatRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZakatService {
    private final ZakatRepository zakatRepository;
    private final UserService userService;

    public ZakatService(ZakatRepository zakatRepository, UserService userService) {
        this.zakatRepository = zakatRepository;
        this.userService = userService;
    }

    public List<ZakatDTO> getReceivedZakatHistory() {
        User receiver = userService.getCurrentUser();
        return zakatRepository.findByReceiver(receiver)
                .stream()
                .map(ZakatDTO::fromEntity)
                .toList();
    }

    public ZakatDTO addZakat(ZakatDTO zakatDTO) {
        User receiver = userService.getCurrentUser();
        Zakat zakat = ZakatDTO.toEntity(zakatDTO, receiver);
        return ZakatDTO.fromEntity(zakatRepository.save(zakat));
    }

    public ZakatDTO updateZakat(Long id, ZakatDTO zakatDTO) {
        return zakatRepository.findById(id)
                .map(zakat -> {
                    zakat.setAmountReceived(zakatDTO.getAmountReceived());
                    zakat.setDateReceived(zakatDTO.getDateReceived());
                    return ZakatDTO.fromEntity(zakatRepository.save(zakat));

                })
                .orElseThrow(() -> new RuntimeException("Zakat entry not found"));
    }

    public void deleteZakat(Long id) {
        zakatRepository.deleteById(id);
    }
}



