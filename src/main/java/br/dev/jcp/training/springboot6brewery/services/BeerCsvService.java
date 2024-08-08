package br.dev.jcp.training.springboot6brewery.services;

import br.dev.jcp.training.springboot6brewery.models.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
