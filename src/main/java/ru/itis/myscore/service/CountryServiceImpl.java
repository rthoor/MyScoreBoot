package ru.itis.myscore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.myscore.dto.CountryDto;
import ru.itis.myscore.model.Country;
import ru.itis.myscore.repository.CountryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;

    @Override
    public CountryDto addCountry(CountryDto countryDto) {
        return CountryDto.from(countryRepository.save(
                Country.builder()
                        .name(countryDto.getName())
                        .build()));
    }

    @Override
    public List<CountryDto> getAllCountries() {
        return CountryDto.from(countryRepository.findAll());
    }
}
