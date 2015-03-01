package com.cetdhwani.sprites;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cetdhwani.scenes.GameScene;

public class LandSprite extends Rectangle{
	public final Body body;
	public final FixtureDef fixture;
	public PhysicsWorld physicsWorld;
	public static final String DHWANI="dhwani";
	public static final String DEVIL="devil";
	public Engine engine;
	private LandSprite self; 
	private GameScene scene;
	public LandSprite(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld physicsWorld,float speed,String userData,Engine engine,GameScene scene) {
		super(pX, pY,pX,pY,pVertexBufferObjectManager);
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
		speed*=1.5f;
		if(speed>20)
			speed=20;
		body.setLinearVelocity(-speed,0f);
		scene.attachChild(this);
	}
	

}
