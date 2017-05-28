package model;

public class CharacterFactory {

	private static int computeFormula(int x) {
		return (int)(5*(Math.pow(x*1.0, 3)))/(4);
	}
	
	public static int getNeededXpForLevel(int level) {
		return computeFormula(level);
	}
	
	public static int getGainedXpForLevel(int level) {
		return getNeededXpForLevel(level)/30;
	}
	
	public static int getHealthForLevel(int level) {
		return computeFormula(level)/2;
	}
}
