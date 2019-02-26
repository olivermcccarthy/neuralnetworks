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

	public HelpPanel() {
		HTMLEditorKit kit = new HTMLEditorKit();
		this.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
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

		double w1= -0.205;
		double w2 = 0.84;
		double i1= 0.34;
		double i2 = 0.67;
		double b = 0.0;
		double z = w1 *i1 + w2 *i2 -b;
		double sigmoid = 1/(1 + Math.exp(z*-1));
		double T=1;
		double Error= (sigmoid - T) * (1 -sigmoid) * (sigmoid);
		double learningRate = 1;
		w1 = w1 - learningRate *Error *i1;
		w2 = w2 - learningRate *Error *i2;
		String text= "";
		try {
			lien = bufReader.readLine();
			while (lien != null) {
				text += lien;
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
		HelpPanel panel = new HelpPanel();
		frame.add(panel);
		frame.setVisible(true);
	}
}
