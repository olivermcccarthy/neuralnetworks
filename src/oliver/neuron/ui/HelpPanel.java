package oliver.neuron.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HelpPanel extends JEditorPane {

	public HelpPanel(String packageS) {
		HTMLEditorKit kit = new HTMLEditorKit();
		this.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; font-size:12;}");
		styleSheet.addRule("h1 {color: blue;}");
		styleSheet.addRule("h2 {color: #ff0000;}");
		styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");
		styleSheet.addRule("td {border-right: 1px solid black;}");
		styleSheet.addRule("th {border-right: 1px solid black;}");
		styleSheet.addRule("tr:nth-child(n+1) {background-color: lightblue;}");
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();// 6 oliver.action.AssignAction

		InputStream is2 = classloader.getResourceAsStream("oliver/neuron/ui/SimpleNeuron.html");// 14
																								// oliver.action.AssignAction
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(is2));// 30 oliver.action.AssignAction
		String lien;

		String text= "";
		try {
			lien = bufReader.readLine();
			while (lien != null) {
				text += lien.trim();
				lien = bufReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   this.setText(text);
	}
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("test");
		HelpPanel panel = new HelpPanel("ui");
		frame.add(panel);
		frame.setVisible(true);
	}
}
