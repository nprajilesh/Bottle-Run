package com.cetdhwani.managers;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.engine.Engine;

import android.app.Activity;
import android.util.Log;

import com.cetdhwani.bottlejump.Localstore;
import com.cetdhwani.managers.ResManager;

public class AudManager {
	private Activity activity;
	private MusicManager manager;
	private Localstore store;
	private Music jumpMusic, gameOverMusic, bgMusic, hitMusic, pointMusic,
			crashMusic, flagBgMusic,musicMusic;

	public AudManager(Activity activity, MusicManager manager) {
		this.manager = manager;
		this.activity = activity;
		store = new Localstore(activity);
	}


	public void playBgMusic(boolean flag, boolean loop, float value) {
		if (store.getSoundVal())
			if (bgMusic != null) {
				if (flag) {
					bgMusic.play();
					bgMusic.setVolume(value);
					bgMusic.setLooping(loop);
				} else if (bgMusic.isPlaying())
					bgMusic.pause();
			}
	}

	public void playGameOverMusic(boolean flag) {
		if (store.getSoundVal())
			if (gameOverMusic != null) {
				if (flag)
					gameOverMusic.play();
				else if (gameOverMusic.isPlaying())
					gameOverMusic.pause();
			}
	}

	public void playHitMusic(boolean flag) {
		if (store.getSoundVal())
			if (hitMusic != null) {
				if (flag)
					hitMusic.play();
				else if (hitMusic.isPlaying())
					hitMusic.pause();
			}
	}

	public void playCrashMusic(boolean flag) {
		if (store.getSoundVal())
			if (crashMusic != null) {
				if (flag)
					crashMusic.play();
				else if (crashMusic.isPlaying())
					crashMusic.pause();
			}
	}

	public void playJumpMusic(boolean flag) {
		if (store.getSoundVal()){
			if (jumpMusic != null) {
				if (flag)
					jumpMusic.play();
				else if (jumpMusic.isPlaying())
					jumpMusic.pause();
			}
		}
	}

	public void pointMusic(boolean flag) {
		if (store.getSoundVal())
			if (pointMusic != null) {
				if (flag)
					pointMusic.play();
				else if (pointMusic.isPlaying())
					pointMusic.pause();
			}
	}
	
	public void playFlagBgMusic(boolean flag) {
		if (store.getSoundVal())
			if (	flagBgMusic != null) {
				if (flag){
					flagBgMusic.play();
				}
				else if (flagBgMusic.isPlaying())
					flagBgMusic.pause();
			}
	}
	public void playMusicMusic(boolean flag) {
		if (store.getSoundVal())
			if (	musicMusic != null) {
				if (flag){
					musicMusic.play();
				}
				else if (musicMusic.isPlaying())
					musicMusic.pause();
			}
	}
public void stopAllMusic(){
	if(flagBgMusic!=null&&flagBgMusic.isPlaying())
		flagBgMusic.pause();
	if(musicMusic!=null&&!musicMusic.isPlaying())
		musicMusic.pause();
	if(bgMusic!=null&&bgMusic.isPlaying())
		bgMusic.pause();
}
	public void loadMusic() {
		try {
			jumpMusic = MusicFactory.createMusicFromAsset(manager, activity,
					"sound/jump.ogg");
			hitMusic = MusicFactory.createMusicFromAsset(manager, activity,
					"sound/hit.ogg");
			pointMusic = MusicFactory.createMusicFromAsset(manager, activity,
					"sound/point.ogg");
			crashMusic = MusicFactory.createMusicFromAsset(manager, activity,
					"sound/bottlecrash.ogg");
			gameOverMusic = MusicFactory.createMusicFromAsset(manager,
					activity, "sound/gameover.ogg");
	
			bgMusic = MusicFactory.createMusicFromAsset(manager, activity,
					"sound/bgium.wav");
			flagBgMusic=MusicFactory.createMusicFromAsset(manager, activity,
					"sound/flagbgium.ogg");
			musicMusic=MusicFactory.createMusicFromAsset(manager, activity,
					"sound/saddahaq.ogg");
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("music", "not loaded" + e);
			e.printStackTrace();
		}

	}

}
