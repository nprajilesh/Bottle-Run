package com.cetdhwani.sprites;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cetdhwani.scenes.GameScene;
import com.cetdhwani.scenes.Values;

public class LeafSprite extends Sprite{
	public final Body body;
	public final FixtureDef fixture;
	private boolean topToBottom=false;
	private float speedX;
	private Random random;
	private float minY,maxY;
	private PhysicsWorld physicsWorld;
	private Engine engine;
	private LeafSprite self;
	private GameScene scene ;
	public LeafSprite(float pX, float pY,
			ITextureRegion pTextureRegion,float minX,float minY,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld physicsWorld,float speed,Engine engine,GameScene scene) {
		super(pX, pY,pTextureRegion, pVertexBufferObjectManager);
		fixture = PhysicsFactory.createFixtureDef(4.0f,
				0.0f, 0.2f);
		self=this;
		this.scene=scene;
		this.physicsWorld=physicsWorld;
		this.engine=engine;		
		body = PhysicsFactory.createCircleBody(physicsWorld, this,
				BodyType.KinematicBody, fixture);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				body, true, true));
		speed=speed*1.2f;
		if(speed>20)
			speed=20;
		body.setLinearVelocity(-speed,0f);
		body.setUserData("leaf");
		random=new Random();
		scene.attachChild(this);
	}
	protected void onManagedUpdate(float pSecondsElapsed) {
		if(Values.PAUSE_WORLD){
			destroySelf();
			return;
		}

		if(GameScene.playerSprite.collidesWith(this)){
			destroySelf();
			scene.setBlinkDelay();
			return;
		}
		else if(this.getX()==0){
			
		}
		body.setTransform(body.getPosition().x, 12,body.getAngle());
		super.onManagedUpdate(pSecondsElapsed);
	
	}
	public void destroySelf(){
		engine.runOnUpdateThread(new Runnable(){
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
