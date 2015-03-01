package com.cetdhwani.sprites;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cetdhwani.scenes.GameScene;
import com.cetdhwani.scenes.Values;

public class BottleSprite extends Sprite {
	public final Body body;
	public final FixtureDef fixture;
	public float speed;
	private PhysicsWorld physicsWorld;
	private Engine engine;
	private BottleSprite self;
	private GameScene scene;

	public BottleSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld, float speed, Engine engine,
			GameScene scene, String userdata) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		fixture = PhysicsFactory.createFixtureDef(4.0f, 0.0f, 0.0f);
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.KinematicBody, fixture);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, true));
		body.setUserData(userdata);
		this.speed = speed;
		this.engine = engine;
		this.scene = scene;
		this.physicsWorld = physicsWorld;
		body.setLinearVelocity(new Vector2(-speed, 0));
		self = this;
		scene.attachChild(this);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		body.setLinearVelocity(new Vector2(-speed, 0));
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(Values.PAUSE_WORLD){
			destroySelf();
			return;
		}

		if (this.getX() < 0) {
			destroySelf();
			return;
		}
		synchronized (Values.countQueue) {
			if (GameScene.playerSprite.getX() > getX()
					&& body.getUserData().equals("bottle0")
					&& !Values.countQueue.isEmpty()) {

				Values.countQueue.remove();
				scene.updateScoreBoard();
				Log.d("bottlecount", "updating " + body);
				body.setUserData("null");
			}
		}
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				body.setLinearVelocity(new Vector2(-Values.bottleSpeed, 0));
				
			}});
	}

	public void destroySelf() {
		engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				final PhysicsConnector physicsConnector = physicsWorld
						.getPhysicsConnectorManager()
						.findPhysicsConnectorByShape(self);
				if (physicsConnector != null) {
					physicsWorld.unregisterPhysicsConnector(physicsConnector);
					body.setActive(false);
					physicsWorld.destroyBody(body);
					setIgnoreUpdate(true);
					self.clearUpdateHandlers();
					detachSelf();
				}

			}

		});

	}

}
