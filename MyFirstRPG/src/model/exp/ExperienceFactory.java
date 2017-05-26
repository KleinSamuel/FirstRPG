package model.exp;

public class ExperienceFactory {

	public static int getNeededXpForLevel(int level) {
		return (int)(5*(Math.pow(level*1.0, 3)))/(4);
	}
	
	public static int getGainedXpForLevel(int level) {
		return getNeededXpForLevel(level)/30;
	}
}
