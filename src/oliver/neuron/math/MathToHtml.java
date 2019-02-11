package oliver.neuron.math;

import java.util.ArrayList;

public class MathToHtml {

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
	public static void main(String[] args) {
		String test = "[[(1 + e^-Z^)%(1 + e^-Z^)^2^]  - [1%(1 + e^-Z^)^2^]]";

		DenomNumer aboveAndBelow = null;
		DenomNumer first = null;
		for (int c = 0; c < test.length(); c++) {
			char charC = test.charAt(c);
			switch (charC) {
			case '[': {
				if(aboveAndBelow != null) {
					DenomNumer existing = aboveAndBelow;	
					if(existing instanceof MathToHtml.FreeStanding) {
						existing = existing.parent;
					}
					aboveAndBelow = new DenomNumer();
					existing.children.add(aboveAndBelow);
					aboveAndBelow.parent = existing;
				}else {
					aboveAndBelow = new DenomNumer();
					first = aboveAndBelow;
				}
				
				break;
			}
			case '%': {
				aboveAndBelow.numer = "";
				break;
			}
			
			case ']': {
				if(aboveAndBelow instanceof MathToHtml.FreeStanding) {
					DenomNumer existing = aboveAndBelow;
					aboveAndBelow =aboveAndBelow.parent;
					aboveAndBelow.children.remove(existing);
				}
				if(aboveAndBelow.parent != null) {
					aboveAndBelow =aboveAndBelow.parent;
					DenomNumer existing = aboveAndBelow;	
					aboveAndBelow = new FreeStanding();
					existing.children.add(aboveAndBelow);
					aboveAndBelow.parent = existing;
				}
			
				break;
			}
			default: {
				if (aboveAndBelow != null) {
					
					if(charC =='^') {
						c++;
						charC = test.charAt(c);
						aboveAndBelow.addString("<sup>");
						
						while(charC != '^') {
							aboveAndBelow.addChar(charC);
							c++;
							charC = test.charAt(c);
							
						}
						aboveAndBelow.addString("</sup>");
						c++;
						charC = test.charAt(c);
					}
					if(charC ==']') {
						c--;
						break;
					}
					aboveAndBelow.addChar(charC);
				}
			}
			}
		}
		System.out.println(first);
	}
}
