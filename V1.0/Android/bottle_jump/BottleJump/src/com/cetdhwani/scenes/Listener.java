package com.cetdhwani.scenes;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.cetdhwani.managers.AudManager;
import com.cetdhwani.managers.ResManager;
import com.cetdhwani.sprites.CreatureSprite;
import com.cetdhwani.sprites.PlayerSprite;

public class Listener implements ContactListener, IOnSceneTouchListener {
	private GameScene gameScene;
	private AudManager aM;
	private ResManager rM;

	public Listener(GameScene gameScene) {
		this.gameScene = gameScene;
		Log.d("list", "loaded");
	}

	@Override
	public void beginContact(Contact contact) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();
		if (isPlayerOnground(x1, x2)) {
			if (!Values.GAME_OVER) {

				if (!Values.CAN_JUMP) {
					Values.CAN_JUMP = true;
					gameScene.playerSprite.animate(100);
				}

			} else {
				gameScene.playerSprite.body.setActive(false);
				Values.CAN_JUMP = false;

			}
		}
		else
			if (playerCollidesWithBottle(x1, x2) ) {
				gameScene.rM.engine.runOnUpdateThread(new Runnable(){

					@Override
					public void run() {
						gameScene.playerSprite.setZIndex(10);
						gameScene.playerSprite.getParent().sortChildren();
					}
					
				});
				gameScene.setGameOver();
				gameScene.aM.playCrashMusic(true);
			} 
			else if(playerCollidesWithDevil(x1, x2)){
				gameScene.rM.engine.runOnUpdateThread(new Runnable(){

					@Override
					public void run() {
						gameScene.playerSprite.setZIndex(10);
						gameScene.playerSprite.getParent().sortChildren();
					}
					
				});
				gameScene.setGameOver();
				gameScene.aM.playHitMusic(true);						
			}
			else if(playerCollidesWithCreature(x1, x2)){
				gameScene.rM.engine.runOnUpdateThread(new Runnable(){
					@Override
					public void run() {
						gameScene.playerSprite.setZIndex(10);
						gameScene.playerSprite.getParent().sortChildren();
					}
					
				});
				gameScene.setGameOver();
				gameScene.aM.playHitMusic(true);						
				
			}

	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		final Fixture x1 = contact.getFixtureA();
		final Fixture x2 = contact.getFixtureB();

		if (playerCollidesWithBottle(x1, x2) || playerCollidesWithDevil(x1, x2)) {
			gameScene.setGameOver();
			gameScene.aM.playCrashMusic(true);
		} 
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	private boolean isPlayerOnground(Fixture x1, Fixture x2) {
		if ((x1.getBody().getUserData().equals("player") && x2.getBody()
				.getUserData().equals("land"))
				|| (x2.getBody().getUserData().equals("player") && x1.getBody()
						.getUserData().equals("land")))
			return true;
		return false;
	}

	private boolean playerCollidesWithBottle(Fixture x1, Fixture x2) {
		if ((x1.getBody().getUserData().equals("player") && x2.getBody()
				.getUserData().toString().startsWith("bottle"))
				|| (x2.getBody().getUserData().equals("player") && x1.getBody()
						.getUserData().toString().startsWith("bottle")))
			return true;
		return false;
	}

	private boolean playerCollidesWithLeaf(Fixture x1, Fixture x2) {
		if ((x1.getBody().getUserData().equals("player") && x2.getBody()
				.getUserData().equals("leaf"))
				|| (x2.getBody().getUserData().equals("player") && x1.getBody()
						.getUserData().equals("leaf")))
			return true;
		return false;
	}

	private boolean playerCollidesWithDevil(Fixture x1, Fixture x2) {
		if ((x1.getBody().getUserData().equals("player") && x2.getBody()
				.getUserData().equals("devil"))
				|| (x2.getBody().getUserData().equals("player") && x1.getBody()
						.getUserData().equals("devil")))
			return true;
		return false;
	}
	private boolean playerCollidesWithCreature(Fixture x1, Fixture x2) {
		if ((x1.getBody().getUserData().equals("player") && x2.getBody()
				.getUserData().equals(CreatureSprite.DANGER))
				|| (x2.getBody().getUserData().equals("player") && x1.getBody()
						.getUserData().equals(CreatureSprite.DANGER)))
			return true;
		return false;
	}
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		if (pSceneTouchEvent.isActionDown() && Values.CAN_JUMP) {
			Values.CAN_JUMP = false;
			Values.jmp_counter++;
			return true;
		}

		return false;
	}

}
