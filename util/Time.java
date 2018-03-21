/** Ben F Rayfield offers this software opensource GNU GPL 2+ */
package util;

import java.text.DecimalFormat;

public class Time{
	private Time(){}
	
	public static final long startMillis;
	
	public static final long startNano;
	
	public static final int secondsPerDay = 24*60*60;
	
	protected static final DecimalFormat secondsFormat  = new DecimalFormat("0000000000.0000000");
	
	protected static final DecimalFormat stardateFormat = new DecimalFormat("00000.000000000000");
	
	static{
		startMillis = System.currentTimeMillis();
		startNano = System.nanoTime();
	}
	
	/** Seconds since year 1970
	with relative nanosecond precision (System.nanoTime)
	and absolute few milliseconds precision (System.currentTimeMillis).
	<br><br>
	Practically, at least in normal computers in year 2011, this has about microsecond precision
	because you can only run it a few million times per second.
	TODO test it again on newer computers.
	*/
	public static double time(){
		//TODO optimize by caching the 2 start numbers into 1 double */
		long nanoDiff = System.nanoTime()-startNano;
		return .001*startMillis + 1e-9*nanoDiff; 
	}
	
	public static String timeStr(){
		return timeStr(time());
	}
	
	public static String timeStr(double time){
		return secondsFormat.format(time);
	}
	
	public static String stardateStr(){
		return stardateStr(time());
	}
	
	/** number of 24 hour blocks since year 1970 */
	public static String stardateStr(double time){
		return stardateFormat.format(time/secondsPerDay);
	}

	/** Uses Thread.sleep(milliseconds,nanoseconds) for extra accuracy,
	but don't count on Java running threads often enough to use the extra accuracy on all computers.
	*/
	public static void sleep(double seconds) throws InterruptedException{
		if(seconds <= 0) return;
		double millis = seconds*1e3;
		long millisL = (long)millis;
		millis -= millisL;
		double nanos = millis*1e6;
		int nanosI = (int)Math.round(nanos);
		Thread.sleep(millisL, nanosI);
	}
	
	public static void sleepNoThrow(double seconds){
		try{
			sleep(seconds);
		}catch(InterruptedException e){}
	}

}