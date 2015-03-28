package com.cetdhwani.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
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
import com.cetdhwani.scenes.Values;

public class PlayerSprite extends AnimatedSprite {
	final FixtureDef PLAYER_FIX;
	public final Body body;
	final float initX, initY;
	final float X_TOLERANCE = 0.1f;
	public static final int MOVING_YELLOW=1,STILL_YELLOW=0;
	public static final int MOVING_BLUE=3,STILL_BLUE=2;
	public static final long DUR=100;
	
	public PlayerSprite(float pX, float maxY, float pY,
			TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			PhysicsWorld physicsWorld) {
		super(pX, maxY, pTextureRegion, pVertexBufferObjectManager);
		long[] frameDurration = { PlayerSprite.DUR, PlayerSprite.DUR };
		animate(frameDurration);
		PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 0.0f, 0.0f);
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PLAYER_FIX);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false));
		body.setUserData("player");
		initX = body.getPosition().x;
		initY = body.getPosition().y;
		

	}

	public Body getBody() {
		return body;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	
			float x, y;
			x = body.getPosition().x;
			y = body.getPosition().y;
			if (Math.abs(x - initX) > X_TOLERANCE)// error correction
				x = initX;
			if (y > initY)
				y = initY;
		body.setTransform(x, y, body.getAngle());		

	}

}
