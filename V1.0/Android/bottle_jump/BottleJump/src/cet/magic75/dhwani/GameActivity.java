package cet.magic75.dhwani;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface.OnCreateResourcesCallback;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.ui.IGameInterface.OnPopulateSceneCallback;
import org.andengine.ui.activity.BaseGameActivity;

import com.cetdhwani.managers.CamManager;
import com.cetdhwani.managers.ResManager;
import com.cetdhwani.managers.SceneManager;
import com.cetdhwani.managers.SceneManager.Scenes;
import com.cetdhwani.scenes.GameScene;
import com.cetdhwani.scenes.Values;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends BaseGameActivity {

	private CamManager cM;
	private ResManager rM;
	private SceneManager sM;
	private TimerHandler loader;

	@Override
	public EngineOptions onCreateEngineOptions() {
		
		cM = new CamManager(720, 480);
		cM.engineOptions.getAudioOptions().setNeedsMusic(true);
		cM.engineOptions.getRenderOptions().setDithering(true);
		return cM.engineOptions;
//		new LimitedFPSEngine(EngineOptions pEngineOptions, int pFramesPerSecond);.

	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		rM = new ResManager(this, cM);
		rM.loadSplashResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		sM = new SceneManager(cM, rM);
		sM.createSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(sM.splashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		rM.engine.registerUpdateHandler(loader = new TimerHandler(
				1.8f, true, new ITimerCallback() {
					public void onTimePassed(TimerHandler pTimerHandler) {
						rM.unLoadSplashResources();
						rM.engine.unregisterUpdateHandler(loader);
						rM.loadFonts();
						rM.loadSounds();
						rM.loadGameResources();
						sM.createMenuScene();
						sM.setCurrentScene(Scenes.MENU);

					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onBackPressed() {
		if (this.mEngine != null) {
			Scene scene = this.mEngine.getScene();
			if (scene == sM.gameScene&&!Values.GAME_PAUSE&&!Values.GAME_OVER) {
				GameScene gs = (GameScene) scene;
				// gs.updateHighScore();
				gs.setGamePause();
				sM.setCurrentScene(Scenes.MENU);
				rM.aM.playVanRunMusic(false, false);
				rM.aM.playVanStartMusic(false);
				rM.aM.playVanStopMusic(false);
			} else
				finish();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (rM != null) {
			rM.aM.playBgMusic(false, false, 0.4f);
			rM.aM.playVanRunMusic(false, false);
			rM.aM.playVanStartMusic(false);
			rM.aM.playVanStopMusic(false);

			Scene scene = this.mEngine.getScene();
			if (scene == sM.gameScene) {
				scene.reset();
				sM.setCurrentScene(Scenes.MENU);
			}
		}
	}

}