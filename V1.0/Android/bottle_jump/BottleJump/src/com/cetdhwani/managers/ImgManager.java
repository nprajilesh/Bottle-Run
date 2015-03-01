package com.cetdhwani.managers;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;



public class ImgManager {
	public BaseGameActivity activity;
	public final int BOTTLES = 5;

	public BitmapTextureAtlas splashTexture, playerTexture, flagTexture,
			blinkTexture, vehicleTexture, landTexture, cardTexture,
			bottleTexture[], mAutoParallaxBackgroundTexture, gameOverTexture,
			playGameTexture, blurTexture, devilTexture, logoTexture,
			leafTexture, collegeTexture, loaderTexture, plus2Texture,
			plus10Texture, freedomTexture, happinesTexture, ethicsTexture,
			leaderBoardTexture, notifyTexture,musicTexture,pauseTexture,remTexture,relTexture,abtTexture,grunTexture,fireTexture,themeTexture,wheelTexture;
	
	

    
    
	private void loadPixelPerfects(){
		
		
		
	}
	
	
	public ITextureRegion splashRegion, landRegion, mParallaxLayerBack,
			mParallaxLayerMid, plusPointRegion, plus2Region, plus10Region,
			bottleRegion[],  cardRegion, blurRegion,
			collegeRegion, devilRegion, logoRegion, leafRegion,notifyRegion;
//	public ITextureRegion freedomRegion,
//			happinesRegion, ethicsRegion ;

	public TiledTextureRegion playGameRegion, loaderRegion, playerRegion,remRegion,relRegion,abtRegion,
			leaderBoardRegion,gameOverRegion,musicRegion,grunRegion,pauseRegion, flagRegion, blinkRegion,fireRegion, vehicleRegion,themeRegion,wheelRegion;
	
	public AutoParallaxBackground autoParallaxBackground;
	public TextureManager tM;
	
	public ImgManager(	BaseGameActivity activity) {
		this.activity = activity;
		this.tM=activity.getTextureManager();
		bottleTexture = new BitmapTextureAtlas[BOTTLES];
		bottleRegion = new ITextureRegion[BOTTLES];

	}
	public void loadSplashResources() {
		splashTexture = new BitmapTextureAtlas(tM,
				128, 256);
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTexture, activity, "gfx/game/bottle5.png", 0, 0);
		splashTexture.load();
		
		freedomTexture = new BitmapTextureAtlas(tM, 128,
				128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		freedomRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				freedomTexture, activity, "gfx/logo/freedom.png", 0, 0);
//		freedomTexture.load();
//		
//		happinesTexture = new BitmapTextureAtlas(tM, 128,
//				128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		happinesRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				happinesTexture, activity, "gfx/logo/happiness.png", 0, 0);
//		happinesTexture.load();
//		
//
//		ethicsTexture = new BitmapTextureAtlas(tM, 128,
//				128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		ethicsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
//				ethicsTexture, activity, "gfx/logo/ethics.png", 0, 0);
//		ethicsTexture.load();

		playerTexture = new BitmapTextureAtlas(tM,
				128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, activity,
						"gfx/game/player.png", 0, 0, 2, 1);
		playerTexture.load();
		
		
		loaderTexture = new BitmapTextureAtlas(tM,
				1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		loaderRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(loaderTexture, activity,
						"gfx/game/loader.png", 0, 0, 5, 5);
		loaderTexture.load();

	}
	public void loadGameResources() {

		playGameTexture = new BitmapTextureAtlas(tM,
				256,128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		playGameRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playGameTexture, activity,
						"gfx/game/play1.png", 0, 0, 2, 1);
		playGameTexture.load();

		
		vehicleTexture = new BitmapTextureAtlas(tM,
				512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		vehicleRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(vehicleTexture, activity,
						"gfx/game/vehicle.png", 0, 0, 2, 2);
		vehicleTexture.load();


		musicTexture = new BitmapTextureAtlas(tM,
				128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		musicRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(musicTexture, activity,
						"gfx/game/music.png", 0, 0, 2, 1);
		musicTexture.load();

		relTexture = new BitmapTextureAtlas(tM,
				128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		relRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(relTexture, activity,
						"gfx/game/reload.png", 0, 0, 2, 1);
		relTexture.load();

		abtTexture = new BitmapTextureAtlas(tM,
				128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		abtRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(abtTexture, activity,
						"gfx/game/help.png", 0, 0, 2, 1);
		abtTexture.load();

		
		pauseTexture = new BitmapTextureAtlas(tM,
				128,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		pauseRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(pauseTexture, activity,
						"gfx/game/pause.png", 0, 0, 2, 1);
		pauseTexture.load();

		remTexture = new BitmapTextureAtlas(tM,
				256,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		remRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(remTexture, activity,
						"gfx/game/rem.png", 0, 0, 4, 1);
		remTexture.load();

		grunTexture = new BitmapTextureAtlas(tM,
				256,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		grunRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(grunTexture, activity,
						"gfx/game/grun.png", 0, 0, 4, 1);
		grunTexture.load();

		wheelTexture = new BitmapTextureAtlas(tM,
				256,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		wheelRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(wheelTexture, activity,
						"gfx/game/dhwanimotor.png", 0, 0, 4, 1);
		wheelTexture.load();

		
		fireTexture = new BitmapTextureAtlas(tM,
				256,64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		fireRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(fireTexture, activity,
						"gfx/game/fire.png", 0, 0, 4, 1);
		fireTexture.load();

		
		leaderBoardTexture = new BitmapTextureAtlas(tM,
				128, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		leaderBoardRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(leaderBoardTexture, activity,
						"gfx/game/board.png", 0, 0, 2, 1);
		leaderBoardTexture.load();

		
		blinkTexture = new BitmapTextureAtlas(tM,
				2048, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		blinkRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(blinkTexture, activity,
						"gfx/game/blink.png", 0, 0, 2, 1);
		blinkTexture.load();

		landTexture = new BitmapTextureAtlas(tM,
				1024, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		landRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				landTexture, activity, "gfx/game/platform.png", 0, 0);
		landTexture.load();

		themeTexture = new BitmapTextureAtlas(tM,
				512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		themeRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				themeTexture, activity, "gfx/game/theme.png", 0, 0,1,2);
		themeTexture.load();

		
		logoTexture = new BitmapTextureAtlas(tM, 64,
				64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		logoRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				logoTexture, activity, "gfx/game/logo.png", 0, 0);
		logoTexture.load();

		notifyTexture = new BitmapTextureAtlas(tM, 256,
				64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		notifyRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				notifyTexture, activity, "gfx/game/msg.png", 0, 0);
		notifyTexture.load();

		
		plus2Texture = new BitmapTextureAtlas(tM, 64,
				64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		plus2Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				plus2Texture, activity, "gfx/game/plus2.png", 0, 0);
		plus2Texture.load();

		plus10Texture = new BitmapTextureAtlas(tM,
				64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		plus10Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				plus10Texture, activity, "gfx/game/plus10.png", 0, 0);
		plus10Texture.load();

		flagTexture = new BitmapTextureAtlas(tM, 256,
				512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		flagRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(flagTexture, activity,
						"gfx/game/flag.png", 0, 0, 4, 2);
		flagTexture.load();

		devilTexture = new BitmapTextureAtlas(tM, 64,
				64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		devilRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				devilTexture, activity, "gfx/game/devil.png", 0, 0);
		devilTexture.load();

		blurTexture = new BitmapTextureAtlas(tM, 1024,
				512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		blurRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				blurTexture, activity, "gfx/game/blur.png", 0, 0);
		blurTexture.load();

		
		
		cardTexture = new BitmapTextureAtlas(tM, 32,
				32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		cardRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				cardTexture, activity, "gfx/game/dhwani.png", 0, 0);
		cardTexture.load();

		collegeTexture = new BitmapTextureAtlas(tM,
				2048, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		collegeRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				collegeTexture, activity, "gfx/game/college.png", 0, 0);
		collegeTexture.load();

		leafTexture = new BitmapTextureAtlas(tM, 32,
				32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		leafRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				leafTexture, activity, "gfx/game/marichuvana.png", 0, 0);
		leafTexture.load();

		for (int i = 0; i < BOTTLES; ++i) {
			bottleTexture[i] = new BitmapTextureAtlas(
					tM, 16, 64,
					TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			bottleRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(bottleTexture[i], activity,
							"gfx/game/bottle" + (i + 1) + ".png", 0, 0);
			bottleTexture[i].load();
		}

		gameOverTexture = new BitmapTextureAtlas(tM,
				512, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		gameOverRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameOverTexture, activity,
						"gfx/game/warning.png", 0, 0,2,1);
		gameOverTexture.load();

		mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(
				tM, 2048, 2048,
				TextureOptions.DEFAULT);
		mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
						"gfx/game/back.png", 0, 480);
		mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, activity,
						"gfx/game/back6.png", 0, 0);

		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 30);
		tM.loadTexture(mAutoParallaxBackgroundTexture);

	}

	 public void unloadSplashResources(){

	 }
}
