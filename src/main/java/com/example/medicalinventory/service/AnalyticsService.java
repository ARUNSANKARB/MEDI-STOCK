package com.example.medicalinventory.service;

import com.example.medicalinventory.model.Medicine;
import com.example.medicalinventory.model.Stock;
import com.example.medicalinventory.repository.MedicineRepository;
import com.example.medicalinventory.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final MedicineRepository medicineRepository;
    private final StockRepository stockRepository;

    public AnalyticsService(MedicineRepository medicineRepository,
                            StockRepository stockRepository) {
        this.medicineRepository = medicineRepository;
        this.stockRepository = stockRepository;
    }

    public Map<String, Object> getAnalyticsSummary() {

        List<Medicine> medicines = medicineRepository.findAll();
        List<Stock> stockRecords = stockRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalDate next30Days = today.plusDays(30);


        long totalMedicines = medicines.size();

        long totalStock = medicines.stream()
                .mapToLong(Medicine::getQuantity)
                .sum();


        long lowStockMedicines = medicines.stream()
                .filter(medicine ->
                        medicine.getQuantity() <= medicine.getReorderLevel())
                .count();


        long outOfStockMedicines = medicines.stream()
                .filter(medicine ->
                        medicine.getQuantity() == 0)
                .count();


        long expiredMedicines = medicines.stream()
                .filter(medicine -> medicine.getExpiryDate() != null)
                .filter(medicine -> {
                    LocalDate expiryDate =
                            LocalDate.parse(medicine.getExpiryDate());

                    return expiryDate.isBefore(today);
                })
                .count();


        long expiringSoonMedicines = medicines.stream()
                .filter(medicine -> medicine.getExpiryDate() != null)
                .filter(medicine -> {
                    LocalDate expiryDate =
                            LocalDate.parse(medicine.getExpiryDate());

                    return !expiryDate.isBefore(today)
                            && !expiryDate.isAfter(next30Days);
                })
                .count();


        long totalStockIn = stockRecords.stream()
                .filter(stock ->
                        "IN".equalsIgnoreCase(stock.getType()))
                .mapToLong(Stock::getQuantity)
                .sum();


        long totalStockOut = stockRecords.stream()
                .filter(stock ->
                        "OUT".equalsIgnoreCase(stock.getType()))
                .mapToLong(Stock::getQuantity)
                .sum();

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("totalMedicines", totalMedicines);
        result.put("totalStock", totalStock);
        result.put("lowStockMedicines", lowStockMedicines);
        result.put("outOfStockMedicines", outOfStockMedicines);
        result.put("expiredMedicines", expiredMedicines);
        result.put("expiringSoonMedicines", expiringSoonMedicines);
        result.put("totalStockIn", totalStockIn);
        result.put("totalStockOut", totalStockOut);

        return result;
    }
}