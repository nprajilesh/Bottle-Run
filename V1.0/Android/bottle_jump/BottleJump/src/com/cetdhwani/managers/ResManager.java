package com.cetdhwani.managers;

import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.ui.activity.BaseGameActivity;


public class ResManager {

	public BaseGameActivity activity;
	public CamManager cM;
	public Engine engine;


	public Font font, font1;
	public final int BOTTLES = 5;
	public AudManager aM;  
	public ImgManager iM;
	public ResManager(BaseGameActivity activity,
			CamManager cameraManager) {
		this.cM = cameraManager;
		this.activity = activity;
		engine = activity.getEngine();	
		iM=new ImgManager(activity);
		aM=new AudManager(activity,engine.getMusicManager());
	}

	public void loadSplashResources() {
		iM.loadSplashResources();
	}

	public void unLoadSplashResources() {
		iM.unloadSplashResources();
	}

	public void loadGameResources() {
		iM.loadGameResources();
	}

	public void loadMenuResources() {
	}

	public void loadSounds(){
		aM.loadMusic();
	}
	public void loadFonts() {
		String mFontFile = "font/base.otf";
		final ITexture font_hud = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		font = FontFactory.createFromAsset(activity.getFontManager(), font_hud,
				activity.getAssets(), mFontFile, 80, true,
				android.graphics.Color.WHITE);
		font.load();
		final ITexture font_hud1 = new BitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		font1 = FontFactory.createFromAsset(activity.getFontManager(),
				font_hud1, activity.getAssets(), mFontFile, 30, true,
				android.graphics.Color.WHITE);
		font1.load();
	}
}
