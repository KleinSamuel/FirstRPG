package util;

public class DateEntry {

	private int day;
	private int month;
	private int year;
	
	private int hour;
	private int min;
	private int sec;
	
	public DateEntry(String dateString) {
		parseDateString(dateString);
	}
	
	private void parseDateString(String s) {
		
		String date = s.split(" ")[0];
		String time = s.split(" ")[1];
		
		this.day = Integer.parseInt(date.split("/")[2]);
		this.month = Integer.parseInt(date.split("/")[1]);
		this.year = Integer.parseInt(date.split("/")[0]);
		
		this.hour = Integer.parseInt(time.split(":")[0]);
		this.min = Integer.parseInt(time.split(":")[1]);
		this.sec = Integer.parseInt(time.split(":")[2]);
		
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getSec() {
		return sec;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
	
	
	
}
