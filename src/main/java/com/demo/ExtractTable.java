package com.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;

public class ExtractTable {
	
	public static void main(String[] args) throws InvalidPasswordException, IOException {
		ExtractTable et = new ExtractTable(); 
		String data = et.extractTableFromPdf("D:\\m27.pdf");
		et.writeFile(data, "D:\\m27.csv");
	}
	
	public String extractTableFromPdf(String path) throws InvalidPasswordException, IOException {
		PDDocument document = PDDocument.load(new File(path));
		ObjectExtractor oe =new ObjectExtractor(document);
		int totalPages = document.getNumberOfPages();
		StringBuilder sb = new StringBuilder();
		 
		for(int pageNumber=1; pageNumber<=totalPages; pageNumber++) {
			sb.append(extractTableFromPdf(oe,pageNumber));
		}
		return sb.toString();
	}

	public String extractTableFromPdf(ObjectExtractor objExtractor, Integer pageNumber)
			throws InvalidPasswordException, IOException {
		StringBuilder sb = new StringBuilder();
		BasicExtractionAlgorithm se = new BasicExtractionAlgorithm();
		Page page = objExtractor.extract(pageNumber);
		CSVWriter writer = new CSVWriter();
		List<Table> tables = se.extract(page);
		if (!tables.isEmpty()) {
			tables.forEach(table -> {
				try {
					writer.write(sb, table);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		return sb.toString();
	}
	
	public void writeFile(String fileContent, String outputFile) {
		try (PrintWriter out = new PrintWriter(outputFile)) {
			out.println(fileContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	} 
}
