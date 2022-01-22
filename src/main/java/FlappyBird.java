import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

import java.util.HashMap;
import java.util.Map;

import static com.jme3.scene.Spatial.CullHint.Always;
import static com.jme3.scene.Spatial.CullHint.Dynamic;

/**
 * This main class acts as a "hub" for the program. It contains the main method, which is the entry point for the
 * program. It also instantiates all the objects and calls the methods to run the program.
 * <p>
 * This main class extends SimpleApplication, which is a class provided by jMonkeyEngine that provides a basic framework
 * for creating a 3D application.
 * <p>
 * This class implements ActionListener, which is a class provided by jMonkeyEngine that allows for the use of keyboard
 * input. ActionListener requires us to implement the onAction() method, which is called when the user presses a key.
 * <p>
 * The coordinate system used is different from conventional graphics coordinate systems. The lower-left corner of the
 * screen is (0,0) and the upper-right corner is (640, 480). The x-axis points right and the y-axis points up. The
 * z-axis points towards the viewer (since our application is a 2D application, the z-axis acts as a "layer" feature).
 *
 * @author Jacob Wysko
 * @version 2022-01-18
 */
public class FlappyBird extends SimpleApplication implements ActionListener {
	
	/**
	 * Faby is the player of our game.
	 */
	Faby faby;
	
	/**
	 * This array stores each of the barriers. A single barrier consists of the two pipes.
	 */
	final Barrier[] barriers = new Barrier[100];
	
	/**
	 * The scrolling background.
	 */
	Background background;
	
	/**
	 * The scrolling base.
	 */
	Base base;
	
	/**
	 * The score object of the game. Note that this is not a number of the score, but rather the "Score" object that
	 * displays the number to the screen.
	 */
	Score score;
	
	/**
	 * The start screen.
	 */
	Spatial startScreen;
	
	/**
	 * The game over screen.
	 */
	Spatial gameOverScreen;
	
	/**
	 * Maps a name of a sound to the sound itself. This is used to play sounds.
	 */
	final Map<String, AudioNode> sounds = new HashMap<>();
	
	/**
	 * This string holds the current game state. The game should recognize these states: - "ready" - "playing" - "dead"
	 */
	String state = "ready";
	
	/**
	 * On every frame of the game, we check to see if the score has changed. To have something to compare it to, we
	 * write down the score to this variable, then compare it to the current score.
	 */
	private int lastScore = 0;
	
	/**
	 * This is the main method. It is the entry point for the program.
	 */
	public static void main(String[] args) {
		FlappyBird app = new FlappyBird();
		app.setShowSettings(false); // We don't want to see the settings window
		app.setDisplayFps(false); // We don't want to see the FPS
		app.setDisplayStatView(false); // We don't want to see the statistics
		app.start(); // Start the program
	}
	
	/**
	 * This method is called by jMonkeyEngine when the program is started. It instantiates all the objects and gets
	 * everything ready to run.
	 */
	@Override
	public void simpleInitApp() {
		/* Set up the game's camera */
		cam.setParallelProjection(true); // Because our game is 2D, we want to use parallel projection
		cam.setLocation(new Vector3f(0, 0, 10)); // Set the camera's position on layer 10
		flyCam.setEnabled(false); // Because we don't want the user to be able to move the camera around, we disable the flyCam
		
		/* Here, we set up an action listener. This allows jMonkeyEngine to know when we perform an action. Our game
		 * will have one action: the space bar. It will make Faby jump. */
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE)); // Space bar -> "Jump"
		inputManager.addListener(this, "Jump"); // This class will handle the "Jump" action
		
		/* Here, we create the game's player. Its name is Faby. Because we will need to reference Faby later on, we must
		 * store Faby in a class variable. This means that any method in this class can reference it. All the class
		 * variables in the program have been declared just below the class declaration. */
		faby = new Faby(this);
		
		/* We want faby to start in the middle of the screen. Because the origin point of a sprite is in its lower left-
		 * hand corner, we need to move faby to the right by half its width as well. */
		faby.node.setLocalTranslation(centerX() - faby.width / 2F, centerY(), 0);
		
		/* Here, we create 100 pipe barriers. */
		for (int i = 0; i < 100; i++) {
			barriers[i] = new Barrier(this, i);
		}
		
		/* Here, we load the "game over" text. Like how we had to nudge Faby over by its half of its sprite width for it to appear in the center, we do the same here.*/
		gameOverScreen = getSprite("gameover.png");
		guiNode.attachChild(gameOverScreen);
		gameOverScreen.move(centerX() - 96, centerY(), 0);
		
		/* Here, we load the "start" graphic. */
		startScreen = getSprite("message.png");
		guiNode.attachChild(startScreen);
		startScreen.move(centerX() - 92, centerY() - 74, 400);
		
		/* Here, we set up the scrolling background. */
		background = new Background(this);
		
		/* Here, we set up the scrolling base. */
		base = new Base(this);
		
		/* Here, we set up the game's score. */
		score = new Score(this);
		
		/* Here, we set up the game's sound effects. */
		sounds.put("flap", getSoundEffect("wing.wav"));
		sounds.put("hit", getSoundEffect("hit.wav"));
		sounds.put("score", getSoundEffect("score.wav"));
		sounds.put("die", getSoundEffect("die.wav"));
	}
	
	/**
	 * This method is called by jMonkeyEngine on every frame. It is the main game loop.
	 *
	 * @param tpf: the amount of time that has passed since the last frame
	 */
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf); // Call the superclass' update method to ensure that jME's code runs too
		
		/* Here, we tick all the game objects. */
		
		faby.tick(); // Tick Faby
		
		for (Barrier barrier : barriers) { // Tick all the barriers
			barrier.tick();
		}
		
		background.tick(); // Tick the background
		base.tick(); // Tick the base
		score.tick(); // Tick the score
		
		/* We check to see if Faby is intersecting any of the barriers. If so, we play the "hit" and "die" sound effects and end the game. */
		for (Barrier barrier : barriers) {
			if (faby.intersects(barrier) && !state.equals("dead")) {
				sounds.get("hit").play();
				sounds.get("die").play();
				state = "dead";
			}
		}
		
		/* Check to see if we should play a sound. */
		if (score() > lastScore) {
			sounds.get("score").playInstance();
		}
		lastScore = score(); // Always update the last score, regardless
		
		/* We hide/show screens based on the game's state. */
		if (state.equals("dead")) {
			gameOverScreen.setCullHint(Dynamic);
		} else {
			gameOverScreen.setCullHint(Always);
		}
		
		/* We should only show the start screen if the game is not running (the ready state). */
		if (state.equals("ready")) {
			startScreen.setCullHint(Dynamic);
		} else {
			startScreen.setCullHint(Always);
		}
	}
	
	/**
	 * This method loads a sprite from the file system, given its texture file name.
	 *
	 * @param texture: the name of the texture file
	 * @return the sprite
	 */
	Spatial getSprite(String texture) {
		/* It's not important to understand how this method works. It just loads a sprite from the file system. */
		Node node = new Node(texture);
		Picture pic = new Picture(texture);
		Texture2D tex = (Texture2D) assetManager.loadTexture(texture);
		pic.setTexture(assetManager, tex, true);
		pic.setWidth(tex.getImage().getWidth());
		pic.setHeight(tex.getImage().getHeight());
		Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
		picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
		node.setMaterial(picMat);
		node.attachChild(pic);
		return node;
	}
	
	/**
	 * This method loads a sound effect from the file system, given its sound file name.
	 *
	 * @param sound: the name of the sound file
	 * @return the sound effect
	 */
	private AudioNode getSoundEffect(String sound) {
		/* It's not important to understand how this method works. It just loads a sound effect from the file system. */
		AudioNode audio = new AudioNode(assetManager, sound, AudioData.DataType.Buffer);
		audio.setPositional(false);
		audio.setDirectional(false);
		audio.setLooping(false);
		audio.setVolume(1);
		return audio;
	}
	
	/**
	 * This method is called by jMonkeyEngine when the user presses a key. It is used to determine what action to take
	 * when the user performs an action.
	 *
	 * @param name:      the name of the action that was performed
	 * @param isPressed: true if the key was pressed, false if it was released
	 * @param tpf:       the time in seconds since the last frame (we don't need this)
	 */
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Jump") && isPressed) {
			if (state.equals("ready")) {
				state = "playing";
			}
			if (state.equals("playing")) {
				faby.jump();
			}
		}
	}
	
	/**
	 * This method determines and returns the current score of the game. The current score is the number of barriers who
	 * have passed over the center of the screen. For it to have fully passed over, the right edge must be past the
	 * center.
	 *
	 * @return the current score of the game
	 */
	public int score() {
		int count = 0;
		for (Barrier barrier : barriers) {
			if (barrier.xPosition < centerX() - 52) { // 52 is the width of a pipe
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Returns the x-coordinate of the center of the screen.
	 */
	int centerX() {
		return settings.getWidth() / 2;
	}
	
	/**
	 * Returns the y-coordinate of the center of the screen.
	 */
	int centerY() {
		return settings.getHeight() / 2;
	}
}
