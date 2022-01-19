import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 */
public class Base {
	
	/**
	 * Context to the main class.
	 */
	private final FlappyBird context;
	
	/**
	 * The node that contains all the background image instances.
	 */
	private final Node node;
	
	/**
	 * Creates the background.
	 *
	 * @param context: context to the main class
	 */
	public Base(FlappyBird context) {
		/* Write down context to the main class. */
		this.context = context;
		
		/* We create the node that is going to contain each instance of the background image. */
		node = new Node("base");
		
		/* Move the node forwards so that it overlaps the pipes, and down some since we don't need the whole base. */
		node.move(0, -50, 1);
		
		/* We need three instances of the base image so that we can repeat them without running off the screen. Why
		 * three? I tested it out and two is just a little short, so three is good. */
		for (int i = 0; i < 3; i++) {
			node.attachChild(context.getSprite("base.png"));
			node.getChild(i).setLocalTranslation(i * 336, 0, 0); // 336 is the width of the background image
		}
		
		/* When we create the background, we need to attach it to the main class's GUI node. */
		context.getGuiNode().attachChild(node);
	}
	
	public void tick() {
		/* We only want to move the background layer if the game is running. A speed of 1 feels nice. However, we do not
		 * want to move the node, only the sprites in it. */
		if (context.state.equals("playing")) {
			for (Spatial child : node.getChildren()) { // For each instance of the background image
				child.move(-3, 0, 0); // Move it over by one to the left
				if (child.getLocalTranslation().x < -336) { // If the x-coordinate is less than the width, it is off-screen
					child.move(336 * 3, 0, 0); // Move it four spaces to the right so that it is in the front of the line
				}
			}
		}
	}
}
