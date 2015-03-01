package com.cetdhwani.scenes;

import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class Values {
	public static enum State {
		before, visiting, visited
	}
	public static State state;
//20f
	public static final float BOTTLE_INIT_VEL = 10f;
	public static final float INIT_UP_VEL = 21f;//20f;
	public static final float INIT_GRAVITY = 47f;
	public static float MIN_OFFSET=10;
	public static float  ALLOWANCE=10;
	public static float GRAVITY = INIT_GRAVITY;
	public static float UP_VELOCITY = INIT_UP_VEL;
	public static float MIN_HEIGHT, MIN_WIDTH; 
	public final static int CARDS_COUNT = 15;
	public static float AIR_TIME;
	public static final float LIMIT=0.05f;//gravity velocity()
	public static float bottleSpeed;
	public static boolean GAME_OVER=false;
	public static boolean GAME_PAUSE=false;
	public static boolean PAUSE_WORLD=false;
	public static boolean CAN_JUMP;
	public static int score;
	public static int secs;
	public static int cards;
	public static int blinkDur;
	public static int jmp_counter;
	public static int jump;
	
	public static Queue countQueue;
	public static void loadInitVals() {
		countQueue=new LinkedList();		
		GRAVITY = INIT_GRAVITY;
		UP_VELOCITY = INIT_UP_VEL;
		GAME_OVER = false;
		GAME_PAUSE=false;
		PAUSE_WORLD=false;
		CAN_JUMP = true;
		state=State.before;
		score = 0;
		secs = 0;
		cards = 0;
		blinkDur = 0;
		jmp_counter = 0;
		jump=0;
		bottleSpeed = BOTTLE_INIT_VEL;		
		MIN_OFFSET=135;
		ALLOWANCE=1;
		
		MIN_HEIGHT = INIT_UP_VEL * INIT_UP_VEL / (2 * INIT_GRAVITY);
		AIR_TIME = 2 * INIT_UP_VEL / INIT_GRAVITY;
		AIR_TIME = Math.abs(AIR_TIME);
		MIN_WIDTH = AIR_TIME * BOTTLE_INIT_VEL;
		MIN_WIDTH = Math.abs(MIN_WIDTH);
		
		Log.d("physics", "VELOCITY X" + Values.bottleSpeed);
		Log.d("physics", "GRAVITY" + Values.GRAVITY);
		Log.d("physics", "UP VELOCITY" + Values.UP_VELOCITY);
		Log.d("physics", "MIN WIDTH" + Values.MIN_WIDTH);
		Log.d("physics", "MIN HEIGHT" + Values.MIN_HEIGHT);

		
	}
}
