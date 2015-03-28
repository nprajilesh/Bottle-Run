package com.cetdhwani.scenes;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.cetdhwani.managers.CamManager;
import com.cetdhwani.managers.ImgManager;
import com.cetdhwani.managers.SceneManager;
import com.cetdhwani.managers.SceneManager.Scenes;



public class SplashScene extends Scene{
	private SceneManager sM;
	private ImgManager iM;
	private CamManager cM;	
    private AnimatedSprite logoSprite;
	private AnimatedSprite playerSprite;
	private final int MARGIN=10;

	private VertexBufferObjectManager vbo;
	private AnimatedSprite themeSprite;
	private Sprite bottleSprite;
	public SplashScene(SceneManager sM) {
		this.sM = sM;
		this.iM=sM.rM.iM;
		this.cM=sM.rM.cM;
		vbo=this.sM.rM.engine.getVertexBufferObjectManager();
		setBackground(new Background(255, 255,255));
		loadLogos();
		attachPlayer();
		attachThemeSprite();
		attachBottle();
	}
	private void attachPlayer(){
		float left=cM.width/2-iM.loaderRegion.getWidth();
		float top=cM.height/2-iM.playerRegion.getHeight()/2;
		playerSprite=new AnimatedSprite(left, top, iM.playerRegion,vbo );
		attachChild(playerSprite);
		playerSprite.animate(new long[]{100,100},true);
	}
	private void attachBottle(){
		float left=cM.width/2+iM.loaderRegion.getWidth()-2*iM.bottleRegion[0].getWidth();
		float top=cM.height/2-iM.bottleRegion[0].getHeight()/2+10;
		bottleSprite=new Sprite(left, top, iM.bottleRegion[0],vbo );
		attachChild(bottleSprite);
	}
	
	private void loadLogos(){

		float logoLeft = (cM.mCamera
				.getWidth() - iM.loaderRegion
				.getWidth()) / 2;
		float logoTop = (cM.mCamera
				.getHeight() - iM.loaderRegion
				.getHeight()) / 2;
		
		logoSprite = new AnimatedSprite(logoLeft, logoTop,
				iM.loaderRegion,
				vbo);
		logoSprite.animate(72,false);
		attachChild(logoSprite);


	}
	private void attachThemeSprite(){
		themeSprite = new AnimatedSprite(cM.width/2-iM.themeRegion.getWidth()/2, cM.height-iM.themeRegion.getHeight()-MARGIN, iM.themeRegion,
				vbo);
		attachChild(themeSprite);
	}
	
}
