package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SalesReportDTO;
import com.devsuperior.dsmeta.dto.SalesSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SalesReportDTO(obj.id, obj.date, obj.amount, obj.seller.name AS sellerName) " +
            "FROM Sale obj " +
            "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "AND obj.date BETWEEN :minLocalDate AND :maxLocalDate")
    Page<SalesReportDTO> getReport(String name, LocalDate minLocalDate, LocalDate maxLocalDate, Pageable pageable);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SalesSummaryDTO(obj.seller.name AS sellerName, SUM(obj.amount) AS total) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :minLocalDate AND :maxLocalDate " +
            "GROUP BY obj.seller.name")
    List<SalesSummaryDTO> getSummary(LocalDate minLocalDate, LocalDate maxLocalDate);
}
