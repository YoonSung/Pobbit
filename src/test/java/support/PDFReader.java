package support;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.ExtractionAlgorithm;

public class PDFReader {
	@Test
	public void readTest() throws IOException {
		ObjectExtractor oe = new ObjectExtractor(PDDocument.load(new ClassPathResource("criminal_record.pdf").getInputStream()));
		ExtractionAlgorithm extractor = new BasicExtractionAlgorithm();

		PageIterator it = oe.extract();

		while (it.hasNext()) {
			Page page = it.next();

			for (Table table : extractor.extract(page)) {
				for (List<RectangularTextContainer> row : table.getRows()) {
					for (RectangularTextContainer cell : row) {
						System.out.println(cell.getText());
					}
				}
			}
		}
	}
}
