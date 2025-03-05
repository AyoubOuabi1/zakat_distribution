package org.zakat.distribution.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zakat.distribution.dtos.ZakatDTO;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;
import org.zakat.distribution.repositories.ZakatRepository;

import java.util.List;

@Service
public class ZakatService {
    private static final Logger logger = LoggerFactory.getLogger(ZakatService.class);

    private final ZakatRepository zakatRepository;
    private final UserService userService;

    public ZakatService(ZakatRepository zakatRepository, UserService userService) {
        this.zakatRepository = zakatRepository;
        this.userService = userService;
        logger.info("ZakatService initialized with ZakatRepository and UserService.");
    }

    public List<ZakatDTO> getReceivedZakatHistory() {
        logger.info("Fetching received Zakat history for user: {}", userService.getCurrentUser().getFullName());

        User receiver = userService.getCurrentUser();
        List<ZakatDTO> zakatHistory = zakatRepository.findByReceiver(receiver)
                .stream()
                .map(ZakatDTO::fromEntity)
                .toList();

        logger.info("Successfully fetched {} Zakat entries for user: {}", zakatHistory.size(), receiver.getFullName());
        return zakatHistory;
    }

    public ZakatDTO addZakat(ZakatDTO zakatDTO) {
        logger.info("Adding a new Zakat entry for user: {}", userService.getCurrentUser().getFullName());

        User receiver = userService.getCurrentUser();
        Zakat zakat = ZakatDTO.toEntity(zakatDTO, receiver);
        Zakat savedZakat = zakatRepository.save(zakat);

        logger.info("Zakat entry added successfully with ID: {} for user: {}", savedZakat.getId(), receiver.getFullName());
        return ZakatDTO.fromEntity(savedZakat);
    }

    public ZakatDTO updateZakat(Long id, ZakatDTO zakatDTO) {
        logger.info("Updating Zakat entry with ID: {}", id);

        ZakatDTO updatedZakat = zakatRepository.findById(id)
                .map(zakat -> {
                    zakat.setAmountReceived(zakatDTO.getAmountReceived());
                    zakat.setDateReceived(zakatDTO.getDateReceived());
                    Zakat savedZakat = zakatRepository.save(zakat);
                    logger.debug("Zakat entry with ID: {} updated successfully.", id);
                    return ZakatDTO.fromEntity(savedZakat);
                })
                .orElseThrow(() -> {
                    logger.error("Zakat entry not found with ID: {}", id);
                    return new RuntimeException("Zakat entry not found");
                });

        logger.info("Zakat entry with ID: {} updated successfully.", id);
        return updatedZakat;
    }

    public void deleteZakat(Long id) {
        logger.info("Deleting Zakat entry with ID: {}", id);

        if (zakatRepository.existsById(id)) {
            zakatRepository.deleteById(id);
            logger.info("Zakat entry with ID: {} deleted successfully.", id);
        } else {
            logger.error("Zakat entry not found with ID: {}", id);
            throw new RuntimeException("Zakat entry not found");
        }
    }
}