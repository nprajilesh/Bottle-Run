package com.cetdhwani.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cetdhwani.scenes.GameScene;
import com.cetdhwani.scenes.Values;

public class CreatureSprite extends AnimatedSprite{
	final FixtureDef PLAYER_FIX;
	public final Body body;
	public final static String DHWANI ="dhwani";
	public final static String DANGER = "danger";
	
	
	private PhysicsWorld physicsWorld;
	final float X_TOLERANCE = 0.1f;
	public static final int MOVING=1,STILL=0;
	private GameScene scene;
	private CreatureSprite self;
	public CreatureSprite(float pX, float pY, 
			TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld,GameScene scene,float speed,String userData) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		animate(100);
		PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 0.0f, 0.0f);
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PLAYER_FIX);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false));
		body.setUserData(userData);
		body.setLinearVelocity(-speed,0f);
		this.scene=scene;
		this.physicsWorld=physicsWorld;
		scene.attachChild(this);
		self=this;
	}
	
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {	
		super.onManagedUpdate(pSecondsElapsed);		
		if(Values.PAUSE_WORLD){
			destroySelf();
			return;
		}
		if(collidesWith(GameScene.playerSprite)){
			if(body.getUserData().equals(DHWANI)){
				destroySelf();	
				scene.aM.pointMusic(true);
				scene.createPlusPoint();
				if(Values.cards%Values.CARDS_COUNT==0&&Values.cards>0&&Values.state==Values.State.before){
					Values.state=Values.State.visiting;
					scene.playDemo();
				}
				return;
			}
			else if(!Values.GAME_OVER)
			{
				scene.setGameOver();				
				scene.aM.playHitMusic(true);
				return;
			}
		}
		if(GameScene.playerSprite.getX()>getX()&&body.getUserData().equals(DANGER)&&!Values.countQueue.isEmpty()){
			Values.countQueue.remove();
			scene.updateScoreBoard();
			body.setUserData("null");
		}

		
		if(getX()<-getWidth())
			destroySelf();		
	}

	
	
	public void destroySelf(){
		scene.rM.engine.runOnUpdateThread(new Runnable(){
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
