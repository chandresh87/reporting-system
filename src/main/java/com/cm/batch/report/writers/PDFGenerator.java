package com.cm.batch.report.writers;

import com.cm.batch.report.aggregator.ReportModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

/** @author chandresh.mishra */
@Component
public class PDFGenerator implements DocumentGenerator {

  @Override
  public void generateDocument(String path, List<ReportModel> reportModel)
      throws FileNotFoundException, DocumentException {

    Document document = new Document();
    StringBuilder reportPath = new StringBuilder(path);
    reportPath.append("/");
    reportPath.append("report");
    reportPath.append("_");
    reportPath.append(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    reportPath.append(".pdf");

    PdfWriter.getInstance(document, new FileOutputStream(reportPath.toString()));

    document.open();
    Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
    Paragraph labour_cost_summary = new Paragraph("Labour Cost Summary", font);
    labour_cost_summary.setAlignment(Element.ALIGN_CENTER);
    document.add(labour_cost_summary);

    document.add(Chunk.NEWLINE);

    PdfPTable pdfPTable = getTable(reportModel);
    document.add(pdfPTable);
    document.close();
  }

  private PdfPTable getTable(List<ReportModel> reportModelList) {
    PdfPTable table = new PdfPTable(3);
    addTableHeader(table);
    addRows(table, reportModelList);
    return table;
  }

  private void addTableHeader(PdfPTable table) {
    Stream.of("Department Name", "Total Cost", "Currency")
        .forEach(
            columnTitle -> {
              PdfPCell header = new PdfPCell();
              header.setBackgroundColor(BaseColor.LIGHT_GRAY);
              header.setBorderWidth(2);
              header.setPhrase(new Phrase(columnTitle));
              table.addCell(header);
            });
  }

  private void addRows(PdfPTable table, List<ReportModel> reportModelList) {

    reportModelList.forEach(
        reportModel -> {
          table.addCell(reportModel.getDepartmentName());
          table.addCell(reportModel.getTotalCost().toPlainString());
          table.addCell(reportModel.getBaseCurrency());
        });
  }
}
