package com.cetdhwani.managers;

import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
public class CamManager {
	public Camera mCamera;
	public EngineOptions engineOptions;
	public int height,width;
	public CamManager(int width,int height){
		this.height=height;
		this.width=width;
		mCamera = new Camera(0, 0,width,height);
		engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(
						), mCamera);			
		new LimitedFPSEngine(engineOptions, 20);

		 engineOptions.getRenderOptions().setDithering(true);
	}
}
