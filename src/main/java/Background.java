import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * The background is composed of several instances of a single background image. We can tile the image horizontally and
 * make the background seem larger.
 * <p>
 * To make the background, we use a seamless image that we can tile. So that we don't have to make 100 different sprites
 * of the same background image, we can reuse them as they go off-screen.
 */
public class Background {
	
	/**
	 * Context to the main class.
	 */
	private final FlappyBird context;
	
	/**
	 * The node that contains all the background image instances.
	 */
	private final Node node = new Node("background");
	
	/**
	 * Creates the background.
	 *
	 * @param context: context to the main class
	 */
	public Background(FlappyBird context) {
		/* Write down context to the main class. */
		this.context = context;
		
		/* We create the node that is going to contain each instance of the background image. */
		
		
		/* We need four instances of the background image so that we can repeat them without running off the screen. Why
		 * four? I tested it out and three is just a little short, so four is good. */
		for (int i = 0; i < 4; i++) {
			node.attachChild(context.getSprite("background-day.png"));
			node.getChild(i).setLocalTranslation(i * 288, 0, 0); // 288 is the width of the background image
		}
		
		node.move(0, 0, -10); // Move the node to the back so that it is behind everything else
		
		/* When we create the background, we need to attach it to the main class's GUI node. */
		context.getGuiNode().attachChild(node);
	}
	
	public void tick() {
		/* We only want to move the background layer if the game is running. A speed of 1 feels nice. However, we do not
		 * want to move the node, only the sprites in it. */
		if (context.state.equals("playing")) {
			for (Spatial child : node.getChildren()) { // For each instance of the background image
				child.move(-1, 0, 0); // Move it over by one to the left
				if (child.getLocalTranslation().x < -288) { // If the x-coordinate is less than the width, it is off-screen
					child.move(288 * 4, 0, 0); // Move it four spaces to the right so that it is in the front of the line
				}
			}
		}
	}
}
