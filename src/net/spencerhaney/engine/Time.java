package net.spencerhaney.engine;

public class Time {
	
	private static final float NANOS_IN_SECOND = 1000000000;
	
	private static long lastTime;
	private static long currentTime;
	
	private Time(){}
	
	public static void init(){
		currentTime = lastTime = System.nanoTime();
	}
	
	public static void update(){
		lastTime = currentTime;
		currentTime = System.nanoTime();
	}
	
	public static float getDelta(){
		return (currentTime - lastTime) / NANOS_IN_SECOND;
	}
}
