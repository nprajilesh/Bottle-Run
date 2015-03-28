package com.cetdhwani.managers;

import org.andengine.entity.scene.Scene;

import com.cetdhwani.scenes.GameScene;
import com.cetdhwani.scenes.MenuScene;
import com.cetdhwani.scenes.SplashScene;




public class SceneManager {
	public ResManager rM;
	public  enum Scenes{SPLASH,MENU,GAME};
	public Scene gameScene,splashScene;
	public MenuScene menuScene;
	public  SceneManager(CamManager cameraManager,ResManager resourceManager){
		this.rM=resourceManager;
	}	
	public void createGameScene(){
		this.gameScene=new GameScene(this);
	}
	public void createMenuScene(){
		this.menuScene=new MenuScene(this);
	}

	public void createSplashScene(){
		this.splashScene=new SplashScene(this);
	}
	public void setCurrentScene(Scenes scenes){
		switch(scenes){
			case SPLASH:rM.engine.setScene(splashScene);break;
			case MENU:rM.engine.setScene(menuScene);break;
			case GAME:rM.engine.setScene(gameScene);break;
		}
	}
}
