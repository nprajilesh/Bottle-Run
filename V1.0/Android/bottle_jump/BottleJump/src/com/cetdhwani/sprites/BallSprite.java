package com.cetdhwani.sprites;

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
import com.cetdhwani.scenes.Values.State;

public class BallSprite extends Sprite{
	
	public final Body body;
	public final FixtureDef fixture;
	public PhysicsWorld physicsWorld;
	public static final String DHWANI="dhwani";
	public static final String DEVIL="devil";
	public Engine engine;
	private BallSprite self; 
	private GameScene scene;
	public BallSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld physicsWorld,float speed,String userData,Engine engine,GameScene scene) {
		super(pX, pY,pTextureRegion, pVertexBufferObjectManager);
		fixture = PhysicsFactory.createFixtureDef(4.0f,
				0.0f, 0.2f);		
		body = PhysicsFactory.createCircleBody(physicsWorld, this,
				BodyType.DynamicBody, fixture);
		this.physicsWorld=physicsWorld;
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				body, true, true));
		body.setUserData(userData);
		this.engine=engine;
		this.scene=scene;
		self=this;
		speed*=1.5f;
		if(speed>20)
			speed=20;
		body.setLinearVelocity(-speed,0f);
		scene.attachChild(this);
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
				scene.aM.pointMusic(true);
				scene.createPlusPoint();
				if(Values.cards%Values.CARDS_COUNT==0&&Values.cards>0&&Values.state==Values.State.before){
					Values.state=Values.State.visiting;
					scene.playDemo();
				}
			destroySelf();	
				return;
			}
			else if(!Values.GAME_OVER)
			{
				scene.setGameOver();				
				scene.aM.playHitMusic(true);
				return;
			}
		}
		if(GameScene.playerSprite.getX()>getX()&&body.getUserData().equals(DEVIL)&&!Values.countQueue.isEmpty()){
			Values.countQueue.remove();
			scene.updateScoreBoard();
			Log.d("bottlecount","updating " +body);
			
			body.setUserData("null");
		}

		else if(getX()<-getWidth())
			destroySelf();		
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
