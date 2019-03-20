package oliver.neuron.math;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MathToWiki {


	 
	 static class Part{
		 String myString  ="";
		 Part parent;
		 ArrayList<Part> children = new ArrayList<Part>();
		 boolean isfaction= false;
		 
		 public String toString() {
			 
			 String ret ="";
			 if(this.isfaction ) {
				ret= String.format("{\\frac{%s}{%s}", myString, children.get(0));
				return ret;
			   
			 }
			 if(this.children.size() == 0) {
				 ret = myString;
			 }else {
				 ret =  myString;
				 int index =0;
				 for(Part child : children) {
					 ret = ret.replace("REP"+index, child.toString());
					 index ++;
				 }
			 }
			 ret = ret.replace("+", "%2B");
			 ret = ret.replace("&", "\\partial ");
			    ret = ret.replace(")", "%29");
			    ret = ret.replace(" ", "%20");
			 return ret;
		 }
	 }
	 
	 public static void parseFile(String fileIn, String fileOut) throws Exception{
		 BufferedReader reader = new BufferedReader(new FileReader(fileIn));
		 
		 String line = reader.readLine();
		 FileWriter writer = new FileWriter(fileOut);
		 while(line != null) {
			 while(line.contains("mmm")) {
				 line = mathME(line);
			 }
			 writer.write( line + "\n");
			 line = reader.readLine();
		 }
		 writer.close();
	 }
	 
	 static String mathME(String test) {
		 int startMath = test.indexOf("mmm");
		    
		    if(startMath >=0) {
		    	String part = test.substring(startMath + 3);
		    	int mathEnd = part.indexOf("nnn");
		    	if(mathEnd > 3) {
		    		part = part.substring(0, mathEnd);
		    		Part thisPart = new Part();
		    		Part workingPart = thisPart;
		    		System.out.println(part);
		    		for(int c =0; c < part.length(); c++) {
		    			char charC = part.charAt(c);
		    			
		    			if(charC == '{') {
		    				Part existing =  workingPart;
		    				workingPart = new Part();
		    				
		    				workingPart.parent = existing;
		    				if(!existing.isfaction) {
		    				   existing.myString += "REP" + existing.children.size();
		    				}
		    				existing.children.add(workingPart);
		    			}else if(charC == '^') {
		    			
							
		    				workingPart.myString +=(charC);
		    				workingPart.myString += "{";
		    				c++;
		    				charC = part.charAt(c);
							while(charC != '^') {
								workingPart.myString +=(charC);
								c++;
								charC = part.charAt(c);
								
							}
							workingPart.myString += "}";
							
		    			}
		    			else if(charC == '}') {
		    				c++;
							charC = part.charAt(c);
							if(charC == '/') {
								workingPart.isfaction= true;
							}else {
								c --;
		    				workingPart = workingPart.parent;
		    				if(workingPart.isfaction) {
		    					workingPart = workingPart.parent;
		    				}
							}
		    			}else {
		    				workingPart.myString += charC;
		    			}
		    		}
		    		
		    		String startString = test.substring(0, startMath);
		    		String ret = startString;
		    		ret += " ![d](" + "http://chart.apis.google.com/chart?cht=tx&chl="+thisPart.toString() +"  )\n <br>";
		    		if(test.length() > startMath+ mathEnd +3) {
		    			ret +=  test.substring(startMath+mathEnd +6);
		    		}
		    		return ret;
		    		
		    }
		    
		    }
		   return test;
	 }
	public static void main(String[] args) throws Exception {
		String test = "mmm {-df}/{f^2^} = {e ^-Z^ }/{(1 + e^-Z^)^2^} nnn";
         mathME(test);
		
         parseFile("README2.md", "README.md");
	   
	}
}
	
	    	
	    
		


