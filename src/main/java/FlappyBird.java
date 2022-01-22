import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

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
	
	}
	
	/**
	 * This method is called by jMonkeyEngine on every frame. It is the main game loop.
	 *
	 * @param tpf: the amount of time that has passed since the last frame
	 */
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf); // Call the superclass' update method to ensure that jME's code runs too
		
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
