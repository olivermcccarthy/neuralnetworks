package oliver.neuron.readme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ReadmeToHtml {

	static class DenomNumer {
		String denom = "";
		String numer = null;
		ArrayList<DenomNumer> children = new ArrayList<DenomNumer>();
		DenomNumer parent=null;
		public String toString() {
              
			String ret ="<table  cellspacing=\"0\" > <tr>";
	        if(this.children.size() > 0) {
	        	for(int c =0; c < this.children.size(); c++) {
	        		ret += "<td>";
	        		ret += this.children.get(c).toString();
	        		ret += "" +"</td>";
	        	}
	        	ret +=" </tr></table>";
	        	return ret;
	        }
			return String.format(
					"<table style=\"white-space:pre;border-collapse: collapse;\" cellspacing=\"0\" > <tr ><td style=\"font-size: 8pt;2px solid black;padding:0px;text-align:center;\">%s</td></tr> <tr><td class=\"numer\">%s</td></tr> </table>",
					denom, numer);
		}
		public void addChar(char charC) {
			if (numer != null) {
				
				numer += charC;
			} else {
				denom += charC;
			}
		}
		public void addString(String charC) {
			if (numer != null) {
				
				numer += charC;
			} else {
				denom += charC;
			}
		}
	}
	 static class FreeStanding extends DenomNumer {
		 public String toString() {
		 return String.format(
					"<table  cellspacing=\"0\" > <tr ><td class=\"math\">%s</td></tr> </table>",
					denom);
		}
	 }
	 
	 
	 public static void parseFile(String fileIn, String fileOut) throws Exception{
		 BufferedReader reader = new BufferedReader(new FileReader(fileIn));
		 
		 String line = reader.readLine();
		 FileWriter writer = new FileWriter(fileOut);
		 
		 while(line != null) {
			 
			 if(line.trim().startsWith("-")) {
				
				 while(line.trim().startsWith("-")) {
					 line =line.replaceFirst("-","");
					 line = formatLine(line).replace("<br>", "");
					 writer.write("<li>" + line + "");
					 line = reader.readLine(); 
					 
				 }
				 writer.write("<br>");
			 }
			 if(line.trim().startsWith("#")) {
				 line = line.trim();
				 int hLevel=0;
				 for(int c=0;c < line.length(); c++) {
					 if(line.charAt(c) != '#') {
						 break;
					 }
					 hLevel++;
				 }
				 line = line.substring(hLevel);
				 line = "<h"+hLevel+">" + line +"</h"+hLevel+">"; 
				 line = formatLine(line);
				 writer.write(line + "\n");
				 line = reader.readLine();
				 continue;
			 }
			
					 
			 if(line.trim().startsWith("|")) {
				 writer.write("<table>\n");
				 boolean first= true;
				while (line.trim().startsWith("|")) {
					if (!line.contains("---")) {
						line = line.trim();
						if(!line.endsWith("|")) {
							line += "|";
						}
						line = line.replace("|", "</td><td>");
						
					    line=line.substring(5);
					    line=line.substring(0, line.length() -4);
						line = "<tr>" + line + "</tr>";
						if (first) {
							line = line.replaceAll("td>", "th>");
						}
						first = false;
						line = formatLine(line);
						writer.write(line + "\n");
					}
					
					line = reader.readLine();
				}
				 
				 writer.write("</table>\n"); 
			 }
			 line = formatLine(line);
			 writer.write( line + "\n");
			 line = reader.readLine();
		 }
		 writer.close();
	 }
	
	 private static String formatLine(String line) {
		 if(line.trim().contains("![d](")) {
			 if(line.contains("http://chart.apis.google.com/chart?cht=tx&chl=%20Z%20=%20Wm%20*%20M%20%2B%20Wc%20*C%20%20Z%20=%200.24%20*%200.4%20%2B%200.45%20*0.5%20")) {
				 int debugMe =0;
			 }
			
			 line = line.replace("![d](", "<img src=\"");
			
			 line = "<br> " + line.replace(" )", "\">") +" <br>" ;
			
			
			
		 }
		 return line;
	 }
	public static void main(String[] args) {
		 try {
			parseFile("README.md","src/oliver/neuron/ui/SimpleNeuron.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
