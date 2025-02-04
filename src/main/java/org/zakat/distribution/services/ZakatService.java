package org.zakat.distribution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;
import org.zakat.distribution.repositories.ZakatRepository;

import java.util.List;

@Service
public class ZakatService {
    @Autowired
    private ZakatRepository zakatRepository;

    public List<Zakat> getReceivedZakatHistory(User receiver) {
        return zakatRepository.findByReceiver(receiver);
    }
}

