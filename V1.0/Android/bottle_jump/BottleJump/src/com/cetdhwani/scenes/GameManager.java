package com.cetdhwani.scenes;

import java.util.Random;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cetdhwani.managers.CamManager;
import com.cetdhwani.managers.ImgManager;
import com.cetdhwani.managers.ResManager;
import com.cetdhwani.managers.SceneManager;
import com.cetdhwani.sprites.BottleSprite;
import com.cetdhwani.sprites.PlayerSprite;

public class GameManager {
	private GameScene gameScene;
	private SceneManager sM; 
	private ResManager rM;
	private CamManager cM; 
	private ImgManager iM;
	
	private PhysicsWorld pWorld;
	
	private PlayerSprite playerSprite;
	private AnimatedSprite blinkSprite, vanSprite, flagSprite,dhwaniLoaderSprite;
	private Sprite dhwaniCardSprite;
	
	private Text scoreText, cardText;
	private Rectangle topBox,bottomBox,land;
	private Random random;
	private VertexBufferObjectManager vbo;
	public GameManager(GameScene gameScene,PhysicsWorld physicsWorld){
		this.gameScene=gameScene;
		sM=gameScene.getSceneManager();
		iM=sM.rM.iM;
		cM=sM.rM.cM;
		vbo=sM.rM.engine.getVertexBufferObjectManager();
		pWorld=physicsWorld;
		random=new Random();
	}
	public void loadAllSprites(){
		attachFlag();
		attachDhwaniVan();
		attachPlayer();
		attachBlinker();
		attachPlatfoiM();
	}
	private void loadInitialSettings(){
		
	}
	private void attachFlag() {
		float left, top;
		left = cM.width
				- iM.landRegion.getWidth() / 4;
		top = cM.height
				- iM.landRegion.getHeight()
				- iM.flagRegion.getHeight();
		flagSprite = new AnimatedSprite(left, top,
				iM.flagRegion,
				vbo);
		gameScene.attachChild(flagSprite);
		flagSprite.setVisible(false);
	}
	private void attachDhwaniVan() {
		float left, top;
		left = iM.playerRegion.getWidth() * 3;

		top = cM.height
				- iM.landRegion.getHeight()
				- iM.vehicleRegion.getHeight();
		vanSprite = new AnimatedSprite(
				-iM.vehicleRegion.getWidth(), top,
				iM.vehicleRegion,
				vbo);
		vanSprite.animate(100);
		gameScene.attachChild(vanSprite);
		vanSprite.setVisible(false);
	}
	private void attachPlayer() {
		float landPos = cM.height
				- iM.landRegion.getHeight()
				- iM.playerRegion.getHeight();

		float left = iM.playerRegion.getWidth() * 3;
		playerSprite = new PlayerSprite(left, landPos,
				cM.height / 2,
				iM.playerRegion,
				vbo, pWorld);
		gameScene.attachChild(playerSprite);
	}
	private void attachBlinker() {
		blinkSprite = new AnimatedSprite(0, 0,
				iM.blinkRegion,
				vbo);
		long[] frameDurration = { 100, 100 };
		blinkSprite.animate(frameDurration);
		gameScene.attachChild(blinkSprite);
		blinkSprite.setVisible(false);

	}
	private void attachDhwaniLoader(){
		float left = (cM.mCamera
				.getWidth() - iM.loaderRegion
				.getWidth()) / 2;
		float height = (cM.mCamera
				.getHeight() / 2);
		float width = (cM.mCamera
				.getWidth());
		float top = (cM.mCamera
				.getHeight() - iM.loaderRegion
				.getHeight()) / 2;

		topBox = new Rectangle(0, 0, width, height,
				vbo);
		topBox.setColor(Color.WHITE);
		bottomBox = new Rectangle(0, height, width, height,
				vbo);
		bottomBox.setColor(Color.WHITE);
		gameScene.attachChild(topBox);
		gameScene.attachChild(bottomBox);
		dhwaniLoaderSprite = new AnimatedSprite(left, top,
				iM.loaderRegion,
				vbo);
		dhwaniLoaderSprite.animate(72);
		gameScene.attachChild(dhwaniLoaderSprite);

	}
	private void attachDhwaniCardCounter() {
		float top, left;
		left = iM.cardRegion.getWidth() + 15;
		top = cM.mCamera.getHeight()
				- iM.cardRegion.getHeight();
		cardText = new Text(left, top - 10,
				this.rM.font1, "X 0123456789",
				this.vbo);
		cardText.setText("X " + gameScene.getDhwaniCardCount());
		cardText.setColor(Color.WHITE);
		cardText.setSize(10, 10);
		dhwaniCardSprite = new Sprite(10, top - 15,
				iM.cardRegion,
				vbo);
		gameScene.attachChild(dhwaniCardSprite);
		gameScene.attachChild(cardText);
		cardText.setVisible(false);
	}
	private void attachPlatfoiM() {
		float top = cM.height - 64;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(4.0f, 0.0f, 0.8f);
		land = new Rectangle(0, top,
				cM.width, 2,
				vbo);
		land.setColor(Color.BLACK);
		Body body = PhysicsFactory.createBoxBody(pWorld, land,
				BodyType.StaticBody, fixture);
		body.setUserData("land");
		gameScene.attachChild(land);
		pWorld.registerPhysicsConnector(new PhysicsConnector(land,
				body, true, true));

	}
	private void attachBottle(int offset) {
		Log.d("game", "attached bottle");
		int count = random.nextInt(iM.BOTTLES);
		int arr[] = new int[count + 1];
		Log.d("game", "count:" + count);
		float left, top;
		for (int i = 0; i < count + 1; ++i) {
			arr[i] = random.nextInt(iM.BOTTLES);
			left = cM.width
					- iM.bottleRegion[arr[i]]
							.getWidth()
					+ (i * iM.bottleRegion[0]
							.getWidth()) + offset;
			top = cM.height
					- iM.landRegion.getHeight()
					- iM.bottleRegion[arr[i]]
							.getHeight();
//			BottleSprite bottleSprite = new BottleSprite(left, top,
//					iM.bottleRegion[arr[i]],
//					rM.engine
//							.getVertexBufferObjectManager(), pWorld,
//							gameScene.getBottleSpeed(),rM.engine,GameScene.this);
//			gameScene.attachChild(bottleSprite);
	//		gameScene.getBottleList().add(bottleSprite);
		}
	}


}