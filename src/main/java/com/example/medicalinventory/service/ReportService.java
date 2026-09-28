package com.example.medicalinventory.service;

import com.example.medicalinventory.model.Medicine;
import com.example.medicalinventory.repository.MedicineRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    private final MedicineRepository medicineRepository;

    public ReportService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public byte[] generateInventoryPdf() {

        List<Medicine> medicines = medicineRepository.findAll();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);

            document.open();


            document.add(new Paragraph("MEDICINE INVENTORY REPORT"));
            document.add(new Paragraph(" "));


            document.add(
                    new Paragraph("Total Medicines: " + medicines.size())
            );

            document.add(new Paragraph(" "));

            Table table = new Table(5);

            table.addCell(new Phrase("Medicine Name"));
            table.addCell(new Phrase("Category"));
            table.addCell(new Phrase("Quantity"));
            table.addCell(new Phrase("Price"));
            table.addCell(new Phrase("Expiry Date"));


            for (Medicine medicine : medicines) {

                table.addCell(
                        new Phrase(
                                medicine.getMedicineName() != null
                                        ? medicine.getMedicineName()
                                        : ""
                        )
                );

                table.addCell(
                        new Phrase(
                                medicine.getCategory() != null
                                        ? medicine.getCategory()
                                        : ""
                        )
                );

                table.addCell(
                        new Phrase(
                                String.valueOf(medicine.getQuantity())
                        )
                );

                table.addCell(
                        new Phrase(
                                String.valueOf(medicine.getPrice())
                        )
                );

                table.addCell(
                        new Phrase(
                                medicine.getExpiryDate() != null
                                        ? medicine.getExpiryDate()
                                        : ""
                        )
                );
            }

            document.add(table);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }

        return outputStream.toByteArray();
    }
}