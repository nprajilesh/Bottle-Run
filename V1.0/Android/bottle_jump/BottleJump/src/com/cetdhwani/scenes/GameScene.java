package com.cetdhwani.scenes;

import java.util.Iterator;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.cetdhwani.bottlejump.AfterGame;
import com.cetdhwani.bottlejump.Localstore;
import com.cetdhwani.managers.AudManager;
import com.cetdhwani.managers.CamManager;
import com.cetdhwani.managers.ImgManager;
import com.cetdhwani.managers.ResManager;
import com.cetdhwani.managers.SceneManager;
import com.cetdhwani.managers.SceneManager.Scenes;
import com.cetdhwani.sprites.BallSprite;
import com.cetdhwani.sprites.BottleSprite;
import com.cetdhwani.sprites.CreatureSprite;
import com.cetdhwani.sprites.LeafSprite;
import com.cetdhwani.sprites.PlayerSprite;

public class GameScene extends Scene implements IOnSceneTouchListener {
	private enum Wallpaper {
		one, two, three
	};

	// private enum State {
	// before, visiting, visited
	// }

	private Wallpaper wallpaper;
	// private State state;
	private SceneManager sM;
	private final int MARGIN = 10;
	private PhysicsWorld physicsWorld;
	public static PlayerSprite playerSprite;
	private AnimatedSprite blinkSprite, vanSprite, flagSprite;
	private Sprite dhwaniSprite;
	private AutoParallaxBackground autoParallaxBackground;

	private TimerHandler spriteTimerHandler;
	private Random random;
	private HUD gameHUD;
	private Text scoreText, cardText, hsText;
	private TimerHandler handler;
	private TimerHandler speedHandler;
	private ParallaxEntity one, two, college;
	private AnimatedSprite loadSprite;
	private Rectangle topBox;
	private Rectangle bottomBox;
	private TimerHandler busHandler;
	private Rectangle myRectangle;
	private TimerHandler restartHandler;
	private Localstore store;
	private Vibrator vibrator;
	public AudManager aM;
	public ResManager rM;
	public CamManager cM;
	public ImgManager iM;
	private Listener l;
	private VertexBufferObjectManager vbo;
	private TimerHandler resetHandler;
	private ButtonSprite pauseButton;
	private AnimatedSprite remSprite;

	public GameScene(SceneManager sceneManager) {
		this.sM = sceneManager;
		this.rM = sceneManager.rM;
		this.aM = rM.aM;
		this.cM = rM.cM;
		this.iM = rM.iM;
		this.vbo = rM.engine.getVertexBufferObjectManager();
		setBack();
		initList();
		createHUD();
		attachPauseSprite();
		loadSprite();
		setReadyGo();
		attachDhwaniSprite();
		Values.loadInitVals();
		loadGame();
		createHsText();
		Log.d("game", "load");

	}

	// public void updateHighScore(){
	// int version=store.getv();
	// if(version==1||version==2)
	// store.changehighscore(score);
	//
	// }

	public void createPlusPoint() {

		float left = cM.mCamera.getWidth() / 2 - 20;
		final Sprite plusPointSprite = new Sprite(left, 0, iM.plus2Region,
				rM.engine.getVertexBufferObjectManager()) {
		};
		MoveYModifier mod1 = new MoveYModifier(1f, cM.height - 64,
				-iM.plus2Region.getHeight()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				rM.engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						detachChild(plusPointSprite);
					}
				});
			}

		};

		plusPointSprite.registerEntityModifier(mod1);
		attachChild(plusPointSprite);
		scoreText.setText("" + (Values.score += 2));
		cardText.setText("X " + (++Values.cards));

	}

	public void createPlus10Point() {
		scoreText.setText("" + (Values.score += 10));
		Log.d("score", "" + Values.score);
		float left = cM.mCamera.getWidth() / 2 - 20;
		final Sprite plusPointSprite = new Sprite(left, 0, iM.plus10Region,
				rM.engine.getVertexBufferObjectManager()) {
		};
		MoveYModifier mod1 = new MoveYModifier(2f, cM.height - 64,
				-iM.plus10Region.getHeight()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				rM.engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						detachChild(plusPointSprite);
					}
				});
			}

		};

		plusPointSprite.registerEntityModifier(mod1);
		attachChild(plusPointSprite);

	}

	public SceneManager getSceneManager() {
		return this.sM;
	}

	private void setBack() {
		one = new ParallaxEntity(-5.0f, new Sprite(0, 2, iM.mParallaxLayerMid,
				rM.engine.getVertexBufferObjectManager()));

		two = new ParallaxEntity(-5.0f, new Sprite(0, 2, iM.mParallaxLayerBack,
				rM.engine.getVertexBufferObjectManager()));

		college = new ParallaxEntity(-5.0f, new Sprite(0, 2, iM.collegeRegion,
				rM.engine.getVertexBufferObjectManager()));
		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 30);

		autoParallaxBackground.attachParallaxEntity(two);

		setBackground(autoParallaxBackground);

	}

	private void setReadyGo() {
		float left = cM.width / 2 - iM.remRegion.getWidth() / 2;
		float top = cM.height / 2 - iM.remRegion.getHeight() / 2;
		remSprite = new AnimatedSprite(left, top, iM.remRegion, vbo);
		remSprite.setVisible(false);
		attachChild(remSprite);

	}

	private void attachFlagSprite() {
		float left, top;
		left = cM.width - iM.landRegion.getWidth() / 4;

		top = cM.height - iM.landRegion.getHeight() - iM.flagRegion.getHeight();

		flagSprite = new AnimatedSprite(left, top, iM.flagRegion,
				rM.engine.getVertexBufferObjectManager());
		attachChild(flagSprite);
		flagSprite.setVisible(false);
	}

	private void loadGame() {
		aM.playBgMusic(true, true, 0.4f);
		registerUpdateHandler(new TimerHandler(1.8f, new ITimerCallback() {

			private MoveYModifier mod1;
			private MoveYModifier mod2;

			public void onTimePassed(TimerHandler pTimerHandler) {
				float height = (cM.mCamera.getHeight() / 2);
				loadSprite.setVisible(false);
				cardText.setVisible(true);
				scoreText.setVisible(true);
				scoreText.setText("" + Values.score);
				cardText.setText("X " + (Values.cards));
				hsText.setText("High Score: " + store.getscore());
				mod1 = new MoveYModifier(0.4f, height, 2 * height) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						rM.engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								topBox.unregisterEntityModifier(mod1);

							}
						});
					}
				};
				mod2 = new MoveYModifier(0.4f, 0, -height) {
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						rM.engine.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								topBox.unregisterEntityModifier(mod2);

							}
						});
					}
				};

				topBox.registerEntityModifier(mod2);
				bottomBox.registerEntityModifier(mod1);

				createPhysicsWorld();
				physicsWorld.setContactListener(l);

				attachLand();
				attachPlayer();
				setOnSceneTouchListener(GameScene.this);
				generateBottles();
				setSpeedHandler();
				attachBlinkSprite();
				loadVanSprite();
				attachFlagSprite();

			}
		}));
	}

	private void loadSprite() {

		float left = (cM.mCamera.getWidth() - iM.loaderRegion.getWidth()) / 2;
		float height = (cM.mCamera.getHeight() / 2);
		float width = (cM.mCamera.getWidth());
		float top = (cM.mCamera.getHeight() - iM.loaderRegion.getHeight()) / 2;

		topBox = new Rectangle(0, 0, width, height,
				rM.engine.getVertexBufferObjectManager());
		topBox.setColor(Color.WHITE);
		bottomBox = new Rectangle(0, height, width, height,
				rM.engine.getVertexBufferObjectManager());
		bottomBox.setColor(Color.WHITE);
		attachChild(topBox);
		attachChild(bottomBox);
		loadSprite = new AnimatedSprite(left, top, iM.loaderRegion,
				rM.engine.getVertexBufferObjectManager());
		loadSprite.animate(72);
		attachChild(loadSprite);
	}

	private void loadVanSprite() {
		float left, top;
		left = iM.playerRegion.getWidth() * 3;

		top = cM.height - iM.landRegion.getHeight()
				- iM.vehicleRegion.getHeight();
		vanSprite = new AnimatedSprite(-iM.vehicleRegion.getWidth(), top,
				iM.vehicleRegion, rM.engine.getVertexBufferObjectManager());
		vanSprite.animate(100);
		attachChild(vanSprite);
		vanSprite.setVisible(false);
	}

	private void initList() {
		// bottleList = new ArrayList();
		random = new Random();
		// leafList = new ArrayList();
		wallpaper = Wallpaper.one;
		store = new Localstore(rM.activity);
		l = new Listener(this);

	}

	public int getDhwaniCardCount() {
		return Values.cards;
	}

	private void attachDhwaniSprite() {
		float top, left;
		left = iM.cardRegion.getWidth() + 15;
		top = cM.mCamera.getHeight() - iM.cardRegion.getHeight();
		cardText = new Text(left, top - 10, this.rM.font1, "X 0123456789",
				this.rM.engine.getVertexBufferObjectManager());
		cardText.setText("X " + Values.cards);
		cardText.setColor(Color.WHITE);
		cardText.setSize(10, 10);
		dhwaniSprite = new Sprite(10, top - 15, iM.cardRegion,
				rM.engine.getVertexBufferObjectManager());
		attachChild(dhwaniSprite);
		attachChild(cardText);
		cardText.setVisible(false);
	}

	private void attachBlinkSprite() {
		blinkSprite = new AnimatedSprite(0, 0, iM.blinkRegion,
				rM.engine.getVertexBufferObjectManager());
		attachChild(blinkSprite);
		blinkSprite.setVisible(false);

	}

	private void createHsText() {
		hsText = new Text(10, 10, this.rM.font1, "High Score: 0123456789",
				this.rM.engine.getVertexBufferObjectManager());
		attachChild(hsText);
		hsText.setText("High Score: " + store.getscore());
	}

	private void createHUD() {
		gameHUD = new HUD();
		scoreText = new Text(cM.mCamera.getWidth() / 2 - 20,
				cM.mCamera.getHeight() / 2 - 150, this.rM.font,
				"Score: 0123456789",
				this.rM.engine.getVertexBufferObjectManager());
		scoreText.setText("" + Values.score);
		scoreText.setColor(Color.RED);
		gameHUD.attachChild(scoreText);
		gameHUD.setColor(Color.BLACK);
		scoreText.setVisible(false);
		this.cM.mCamera.setHUD(gameHUD);
	}

	private void setSpeedHandler() {
		speedHandler = new TimerHandler(0.5f, true, new ITimerCallback() {

			public void onTimePassed(TimerHandler pTimerHandler) {
				if (Values.state != Values.State.visiting) {
					if (Values.bottleSpeed >= 30f) {
						unregisterUpdateHandler(speedHandler);
						return;
					}
					if (Values.jmp_counter >= 2) {
						if(Values.jump > 60 )
							Values.bottleSpeed += 0.05f;
						else if(Values.jump > 50  ) 
							Values.bottleSpeed += 0.1f;
						else if(Values.jump > 46 ) 
							Values.bottleSpeed += 0.2f;
						else if(Values.jump > 35 ) 
							Values.bottleSpeed += 0.25f;
						else if(Values.jump > 24 ) 
							Values.bottleSpeed += 0.3f;
						else if(Values.jump > 14 ) 
							Values.bottleSpeed += 0.35f;
						else
							Values.bottleSpeed += 0.5f;
						
						
						
							Values.jmp_counter = 0;
						Values.AIR_TIME = Values.MIN_WIDTH / (Values.bottleSpeed*2);
						while(true){
							Values.UP_VELOCITY+=Values.LIMIT;
							Values.GRAVITY=Values.UP_VELOCITY/Values.AIR_TIME;
							if((Values.MIN_HEIGHT-(Values.AIR_TIME*Values.UP_VELOCITY/2))<=4*Values.LIMIT)
									break;
						}
						Log.d("physics1", "VELOCITY X " + Values.bottleSpeed);
						Log.d("physics1", "GRAVITY " + Values.GRAVITY);
						Log.d("physics1", "UP VELOCITY " + Values.UP_VELOCITY);
						Log.d("physics1", "MIN HEIGHT " + Values.UP_VELOCITY*Values.UP_VELOCITY/(2*Values.GRAVITY));
						Log.d("physics1", "MIN WIDTH " + 2*Values.AIR_TIME*Values.bottleSpeed);

						
						Log.d("dbug",
								"h"
										+ (Values.UP_VELOCITY
												* Values.UP_VELOCITY / (2 * Values.GRAVITY)));
						Log.d("dbug", "w" + (Values.bottleSpeed * Values.AIR_TIME));
						
						rM.engine.runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
					
						physicsWorld.setGravity(new Vector2(0, Values.GRAVITY));
						}});
						if (Values.ALLOWANCE > 2) {
							Values.ALLOWANCE -= 2;
							Values.MIN_OFFSET += 2;
						}
						Log.d("factor all",""+Values.ALLOWANCE);
						Log.d("factor offset",""+	Values.MIN_OFFSET);
					}
				}
			}
		});
		registerUpdateHandler(speedHandler);
	}

	private void hostFlag() {
		Log.d("start", "host");
		registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				Log.d("start", "host" + "timer");

				flagSprite.setCurrentTileIndex(0);
				flagSprite.animate(200, false);
				vanSprite.setVisible(true);
				Values.CAN_JUMP = false;
				rM.engine.runOnUpdateThread(new Runnable() {
					public void run() {
						playerSprite.setZIndex(10);
						playerSprite.getParent().sortChildren();
					}
				});
				moveBus();

			}
		}));

	}

	public void updateScoreBoard() {
		scoreText.setText("" + (++Values.score));

	}

	private void generateBottles() {
		spriteTimerHandler = new TimerHandler(0.1f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (!Values.PAUSE_WORLD) {
					Values.secs++;
					Log.d("speed count", "body count:" + (physicsWorld.getBodyCount()-2));
					if (Values.secs * Values.bottleSpeed >= Values.MIN_OFFSET) {
						Values.secs = 0;
						if (Values.state == Values.State.before) {
							int no = random.nextInt(26);
							if (no > 18) {
								if (no > 23)
									generateKanja();
								else
									genCreature();

							} 
							else {
								attachBottle(random
										.nextInt((int) Values.ALLOWANCE));
							}
						}
					}
				}
			}
		});
		registerUpdateHandler(spriteTimerHandler);
	}

	private void generateKanja() {
		float left = cM.width;
		float top = cM.height / 2 - 30;
		new LeafSprite(left, top, iM.leafRegion, 4, 13,
				rM.engine.getVertexBufferObjectManager(), physicsWorld,
				Values.bottleSpeed, rM.engine, this);
	}

	private void genCreature() {
		int no = random.nextInt(3);
		String userData;
		TiledTextureRegion region;
		if (no == 0) {
			region = iM.fireRegion;
			userData = CreatureSprite.DANGER;
		} else if (no == 1) {
			region = iM.wheelRegion;
			userData = CreatureSprite.DHWANI;
		} else {
			region = iM.grunRegion;
			userData = CreatureSprite.DANGER;
		}
		float left = cM.width - iM.grunRegion.getWidth(0);
		float top = cM.height - iM.landRegion.getHeight()
				- iM.grunRegion.getHeight();
		new CreatureSprite(left, top, region, vbo, physicsWorld, this,
				Values.bottleSpeed, userData);
	}

	private void generateBall(String userData) {
		float left = cM.width - iM.devilRegion.getWidth();
		float top = cM.height - iM.landRegion.getHeight()
				- iM.devilRegion.getHeight();
		ITextureRegion region = null;
		if (userData.equals(BallSprite.DEVIL)) {
			Values.countQueue.add(1);
			Log.d("bottlecount", "" + Values.countQueue.peek());
			region = iM.devilRegion;
		} else if (userData.equals(BallSprite.DHWANI))
			region = iM.logoRegion;
		new BallSprite(left, top, region,
				rM.engine.getVertexBufferObjectManager(), physicsWorld,
				Values.bottleSpeed, userData, rM.engine, this);
	}

	private void createPhysicsWorld() {
		physicsWorld = new PhysicsWorld(new Vector2(0, Values.GRAVITY), false);
		registerUpdateHandler(physicsWorld);
	}

	private PhysicsWorld getphysicsWorld() {
		return physicsWorld;
	}

	private void attachPauseSprite() {
		float left = this.cM.width - iM.pauseRegion.getWidth() - MARGIN;// this.cM.width
																		// / 2
		// - iM.musicRegion.getWidth() / 2;
		float top = MARGIN;// this.cM.height/2+iM.musicRegion.getHeight()+20;
		pauseButton = new ButtonSprite(left, top, iM.pauseRegion, vbo) {
			@Override
			public boolean onAreaTouched(TouchEvent pTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				switch (pTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					setCurrentTileIndex(1);
					break;
				case TouchEvent.ACTION_UP:
					setGamePause();
					setCurrentTileIndex(0);
					break;
				default:
					setCurrentTileIndex(0);

				}
				return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}

		};
		attachChild(pauseButton);
		registerTouchArea(pauseButton);
		MoveXModifier mod2 = new MoveXModifier(0.2f, cM.width,
				pauseButton.getX());
		pauseButton.registerEntityModifier(mod2);

	}

	private void attachPlayer() {
		float landPos = cM.height - iM.landRegion.getHeight()
				- iM.playerRegion.getHeight();

		float left = iM.playerRegion.getWidth() * 3;
		playerSprite = new PlayerSprite(left, landPos, cM.height / 2,
				iM.playerRegion, rM.engine.getVertexBufferObjectManager(),
				this.physicsWorld);
		attachChild(playerSprite);
	}

	private void attachLand() {
		float top = cM.height - 64;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(4.0f, 0.0f, 0.0f);
		myRectangle = new Rectangle(0, top, cM.width, 2,
				rM.engine.getVertexBufferObjectManager());
		myRectangle.setColor(Color.BLACK);
		Body body = PhysicsFactory.createBoxBody(physicsWorld, myRectangle,
				BodyType.StaticBody, fixture);
		body.setUserData("land");
		attachChild(myRectangle);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(myRectangle,
				body, true, true));

	}

	private void attachBottle(int offset) {
		Log.d("game", "attached bottle");
		int count = random.nextInt(rM.BOTTLES);		
		int arr[] = new int[count + 1];
		Log.d("game", "count:" + count);
		float left, top;
		Values.countQueue.add(count + 1);
		Log.d("bottlecount", "" + Values.countQueue.peek());
		for (int i = 0; i < count + 1; ++i) {
			arr[i] = random.nextInt(rM.BOTTLES);
			left = cM.width - iM.bottleRegion[arr[i]].getWidth()
					+ (i * iM.bottleRegion[0].getWidth()) + offset;
			top = cM.height - iM.landRegion.getHeight()
					- iM.bottleRegion[arr[i]].getHeight();
			new BottleSprite(left, top, iM.bottleRegion[arr[i]],
					rM.engine.getVertexBufferObjectManager(),
					this.physicsWorld, Values.bottleSpeed, rM.engine, this,
					"bottle" + i);
		}
	}

	public void restartFromPause() {
		Values.GAME_OVER = true;
		Values.GAME_PAUSE = false;
		playerSprite.stopAnimation();
		playerSprite.setCurrentTileIndex(0);
		sM.menuScene.setGameState();
		enableWorld(false);
		unregisterUpdateHandler(spriteTimerHandler);
		unregisterUpdateHandler(speedHandler);
		reset();

	}

	public void setGameOver() {

		Values.GAME_OVER = true;
		playerSprite.stopAnimation();
		playerSprite.setCurrentTileIndex(0);
		sM.menuScene.setGameState();

		enableWorld(false);
		unregisterUpdateHandler(spriteTimerHandler);
		unregisterUpdateHandler(speedHandler);
		setResetTimer();
		aM.playBgMusic(false, false, 0.4f);
	}

	public void setResume() {

		Values.GAME_PAUSE = false;
		remSprite.setVisible(true);
		remSprite.setCurrentTileIndex(0);
		remSprite.animate(1000, false);
		registerUpdateHandler(new TimerHandler(4f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (!Values.GAME_PAUSE) {
					pauseButton.setVisible(true);
					autoParallaxBackground.setParallaxChangePerSecond(30);
					playerSprite.animate(100);
					remSprite.stopAnimation();
					remSprite.setVisible(false);
					aM.playBgMusic(true, true, 0.4f);
					registerUpdateHandler(spriteTimerHandler);
					registerUpdateHandler(speedHandler);
					enableWorld(true);
				}
			}
		}));
	}

	public void setGamePause() {
		autoParallaxBackground.setParallaxChangePerSecond(0);

		Values.GAME_PAUSE = true;
		playerSprite.stopAnimation();
		playerSprite.setCurrentTileIndex(0);
		sM.menuScene.setGameState();
		pauseButton.setVisible(false);
		enableWorld(false);
		unregisterUpdateHandler(spriteTimerHandler);
		unregisterUpdateHandler(speedHandler);
		sM.setCurrentScene(Scenes.MENU);
		aM.playBgMusic(false, false, 0.4f);
	}

	public float getBottleSpeed() {
		return Values.bottleSpeed;
	}

	public void enableWorld(final boolean flag) {
		rM.engine.runOnUpdateThread(new Runnable() {
			public void run() {
				Iterator<Body> localIterator = physicsWorld.getBodies();
				while (true) {
					if (!localIterator.hasNext()) {
						return;
					}
					try {
						final Body localBody = (Body) localIterator.next();
						if (!localBody.getUserData().equals("player")
								&& !localBody.getUserData().equals("land"))
							localBody.setActive(flag);

					} catch (Exception localException) {
						// Debug.e(localException);
					}
				}
			}
		});
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown() && Values.CAN_JUMP
				&& !Values.GAME_OVER) {
			
			Log.d("dbug1",
					"h"
							+ (Values.UP_VELOCITY
									* Values.UP_VELOCITY / (2 * Values.GRAVITY)));
//			Log.d("dbug", "w" + (Values.bottleSpeed * CONSTANT));

			
			
			playerSprite.stopAnimation();
			playerSprite.setCurrentTileIndex(PlayerSprite.MOVING);
			Values.CAN_JUMP = false;
			Values.jmp_counter++;
			Values.jump++;
			aM.playJumpMusic(true);
			if (playerSprite != null) {
				rM.engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						playerSprite.getBody().setLinearVelocity(
								new Vector2(0, -Values.UP_VELOCITY));
					}
				});
			}
			return true;
		}
		return false;
	}

	public void setBlinkDelay() {
		blinkSprite.setVisible(true);
		blinkSprite.animate(80);
		registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				Log.d("blink", "yes");
				blinkSprite.stopAnimation();
				blinkSprite.setVisible(false);
			}
		}));
	}

	private void setResetTimer() {

		registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (vibrator != null)
					vibrator.cancel();
				sM.setCurrentScene(Scenes.MENU);
				// updateHighScore();
				Log.d("hs1", "" + Values.score);
				aM.playGameOverMusic(true);
				reset();

			}
		}));
	}

	public void moveBus() {
		Log.d("start", "movebus");

		registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				createPlus10Point();
				Log.d("start", "movebus" + "timer");

				MoveXModifier mod2 = new MoveXModifier(1.4f, -iM.vehicleRegion
						.getWidth(), playerSprite.getX() - 40) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						vanSprite.stopAnimation();
						playerSprite.setCurrentTileIndex(PlayerSprite.MOVING);
						visitCollege();
					}

				};
				vanSprite.registerEntityModifier(mod2);

			}
		}));
	}

	private void visitCollege() {
		Log.d("start", "cllgvisit");

		registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				{
					Log.d("start", "cllgvisit" + "timer");

					// aM.playVanRunMusic(true, true);
					playerSprite.setVisible(false);
					vanSprite.animate(100);
					MoveXModifier mod2 = new MoveXModifier(0.9f,
							vanSprite.getX(), cM.mCamera.getWidth()
									- vanSprite.getWidth()) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							super.onModifierFinished(pItem);
							flagSprite.setVisible(false);
							autoParallaxBackground
									.setParallaxChangePerSecond(30);
							autoParallaxBackground.detachParallaxEntity(two);
							autoParallaxBackground
									.attachParallaxEntity(college);
							MoveXModifier mod2 = new MoveXModifier(1.2f,
									-vanSprite.getWidth(), iM.playerRegion
											.getWidth() * 3) {
								protected void onModifierFinished(IEntity pItem) {
									super.onModifierFinished(pItem);
									stepDownBus();
								}
							};
							vanSprite.registerEntityModifier(mod2);

						}

					};
					vanSprite.registerEntityModifier(mod2);

				}

			}
		}));
	}

	private void stepDownBus() {
		registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				autoParallaxBackground.setParallaxChangePerSecond(0);
				playerSprite.animate(100);
				playerSprite.setVisible(true);
				busPasses();
			}
		}));

	}

	private void busPasses() {
		registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				// aM.playVanStartMusic(true);

				MoveXModifier mod2 = new MoveXModifier(0.8f, vanSprite.getX(),
						cM.width) {

					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
					}

				};
				vanSprite.registerEntityModifier(mod2);
				autoParallaxBackground.setParallaxChangePerSecond(30);
				changestateOfGame();
			}
		}));
	}

	private void changestateOfGame() {
		registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				autoParallaxBackground.setParallaxChangePerSecond(30);
				autoParallaxBackground.detachParallaxEntity(college);
				autoParallaxBackground.attachParallaxEntity(two);
				Values.CAN_JUMP = true;
				Values.PAUSE_WORLD = false;
				restartGame();
			}
		}));

	}

	private void restartGame() {
		registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				Values.state = Values.State.before;
			}
		}));

	}

	private void vibrate() throws IllegalStateException {
		vibrator = (Vibrator) this.rM.activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator != null) {
			try {
				vibrator.vibrate(new long[] { 0, 500, 110, 500, 110, 450, 110,
						200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500 },
						-1);
			} catch (Exception e) {

			}
		} else {
			throw new IllegalStateException(
					"You need to enable the Vibrator before you can use it!");
		}
	}

	public void playDemo() {
		Log.d("start", "visiting");

		Values.PAUSE_WORLD = true;
		playerSprite.stopAnimation();
		playerSprite.setCurrentTileIndex(0);
		aM.playFlagBgMusic(true);
		flagSprite.setCurrentTileIndex(0);
		flagSprite.setVisible(true);
		autoParallaxBackground.setParallaxChangePerSecond(0);
		MoveXModifier mod2 = new MoveXModifier(0.4f, cM.width, cM.width - 100);
		flagSprite.registerEntityModifier(mod2);
		hostFlag();
	}

}