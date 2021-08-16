package com.cm.batch.report.writers;

import com.cm.batch.report.aggregator.ReportModel;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

/** @author chandresh.mishra */
public interface DocumentGenerator {

  void generateDocument(String path, List<ReportModel> reportModel)
      throws FileNotFoundException, DocumentException;
}
