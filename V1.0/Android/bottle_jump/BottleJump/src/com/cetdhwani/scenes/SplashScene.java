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
	
    private Sprite freedom,ethics,happiness;
    private AnimatedSprite logoSprite;
	private MoveYModifier modY[];
	private MoveXModifier modX[];
	private TimerHandler loader;
	private AnimatedSprite playerSprite;
	Position f,e,h;
	private VertexBufferObjectManager vbo;
	public SplashScene(SceneManager sM) {
		this.sM = sM;
		this.iM=sM.rM.iM;
		this.cM=sM.rM.cM;
		vbo=this.sM.rM.engine.getVertexBufferObjectManager();
		setBackground(new Background(255, 255,255));
		modX=new MoveXModifier[3];
		modY=new MoveYModifier[3];
		loadLogos();
		attachPlayer();
	}
	private void attachPlayer(){
		float left=cM.width/2-iM.loaderRegion.getWidth();
		float top=cM.height/2-iM.playerRegion.getHeight()/2;
		playerSprite=new AnimatedSprite(left, top, iM.playerRegion,vbo );
		attachChild(playerSprite);
		playerSprite.animate(100);
	}
	
	private void loadLogos(){
		float camWidth=cM.width;
		float camHeight=cM.height;
		
//		float radius=iM.freedomRegion.getWidth()/2;

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

//		freedom= new Sprite(-radius*2,-radius*2 ,
//				iM.freedomRegion,
//				vbo);
//		happiness=new Sprite(camWidth,-radius*2,
//				iM.happinesRegion,
//				vbo);
//		ethics=new Sprite(camWidth/2-radius,camHeight,
//				iM.ethicsRegion,
//				vbo);
//		attachChild(freedom);
//		attachChild(happiness);
//		attachChild(ethics);
		
//		f=new Position(-radius*2,camWidth/2-radius*2,-radius*2,camHeight/2+10);
//		
//		h=new Position(camWidth,camWidth/2,-radius*2,camHeight/2+10);
//	
//		e=new Position(camWidth/2-radius,camWidth/2-radius,camHeight,camHeight/2+radius+10);
//		addDelay();

	}
	private void addDelay(){
	    registerUpdateHandler(loader=new TimerHandler(1.8f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				unregisterUpdateHandler(loader);
				moveXY(f,freedom,0);
				moveXY(e,ethics,2);
				moveXY(h,happiness,1);				
			}
}));

	
	}
	private void moveXY(Position pos,final Sprite sprite,final int index){
		modX[index] = new MoveXModifier(1.5f,
				pos.startX,
				pos.endX) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
//								sprite.unregisterEntityModifier(modX[index]);
			}

		};
		
		 modY[index] = new MoveYModifier(1.5f,
				pos.startY,
				pos.endY) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
	//							sprite.unregisterEntityModifier(modY[index]);
			}

		};
		sprite.registerEntityModifier(modX[index]);
		sprite.registerEntityModifier(modY[index]);

	}

}
 class Position{
	public float startX,endX,startY,endY; 
	public Position(float startX,float endX,float startY,float endY){
		this.startX=startX;
		this.startY=startY;
		this.endX=endX;
		this.endY=endY;
	}
}
