package oliver.neuron.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.html.HTMLEditorKit;

public class HelpPanel extends JEditorPane {

	public HelpPanel() {
		HTMLEditorKit kit = new HTMLEditorKit();
		this.setEditorKit(kit);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();// 6 oliver.action.AssignAction

		InputStream is2 = classloader.getResourceAsStream("oliver/neuron/ui/SimpleNeuron.html");// 14
																								// oliver.action.AssignAction
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(is2));// 30 oliver.action.AssignAction
		String lien;

		
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
