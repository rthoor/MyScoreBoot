package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.myscore.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
