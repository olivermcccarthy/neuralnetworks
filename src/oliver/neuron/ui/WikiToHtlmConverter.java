package oliver.neuron.ui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
public class WikiToHtlmConverter {

	
	public static void main (String [] args) throws Exception {
		Parser parser = Parser.builder().build();
		Node document = parser.parse("This is *Sparta*");
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
		System.out.println(html);
		parseFile("README.md", "README.html");
	}
	
	
	
	 public static void parseFile(String fileIn, String fileOut) throws Exception{
		 BufferedReader reader = new BufferedReader(new FileReader(fileIn));
			Parser parser = Parser.builder().build();
			Node document = parser.parseReader(reader);
			HtmlRenderer renderer = HtmlRenderer.builder().build();
			String html = renderer.render(document);
		
		 FileWriter writer = new FileWriter(fileOut);
		 writer.write(html);
		
		 writer.close();
	 }
}
