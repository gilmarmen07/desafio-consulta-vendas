package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SalesReportDTO;
import com.devsuperior.dsmeta.dto.SalesSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    public Page<SalesReportDTO> getReport(String name, String minDate, String maxDate, Pageable pageable) {
        LocalDate maxLocalDate = fillMaxDate(maxDate);
        LocalDate minLocalDate = fillMinDate(minDate, maxLocalDate);

        return repository.getReport(name, minLocalDate, maxLocalDate, pageable);
    }

    public List<SalesSummaryDTO> getSummary(String minDate, String maxDate) {
        LocalDate maxLocalDate = fillMaxDate(maxDate);
        LocalDate minLocalDate = fillMinDate(minDate, maxLocalDate);
        return repository.getSummary(minLocalDate, maxLocalDate);
    }

    private LocalDate fillMaxDate(String maxDate) {
        LocalDate maxLocalDate;
        if (StringUtils.isBlank(maxDate)) {
            maxLocalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        } else {
            maxLocalDate = LocalDate.parse(maxDate);
        }
        return maxLocalDate;
    }

    private LocalDate fillMinDate(String minDate, LocalDate maxLocalDate) {
        LocalDate minLocalDate;
        if (StringUtils.isBlank(minDate)) {
            minLocalDate = maxLocalDate.minusYears(1L);
        } else {
            minLocalDate = LocalDate.parse(minDate);
        }
        return minLocalDate;
    }
}
