package org.zakat.distribution.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;

import java.util.List;

public interface ZakatRepository extends JpaRepository<Zakat, Long> {
    List<Zakat> findByReceiver(User receiver);
}
