package model.exp;

public class ExperienceFactory {

	public static int getNeededXpForLevel(int level) {
//		return (int)(5*(Math.pow(level*1.0, 3)))/(4);
		return 1250;
	}
	
	public static int getGainedXpForLevel(int level) {
		return 1250/30;
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(getNeededXpForLevel(50));
	}
}
