package ru.itis.myscore.service;

import ru.itis.myscore.dto.CountryDto;

import java.util.List;

public interface CountryService {
    CountryDto addCountry(CountryDto countryDto);
    List<CountryDto> getAllCountries();
}
