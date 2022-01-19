import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Faby is the player of our game.
 */
@SuppressWarnings("unused")
public class Faby {
	
	/**
	 * Faby's sprite's width, in pixels.
	 */
	public final int width = 34;
	/**
	 * Faby's sprite's height, in pixels.
	 */
	public final int height = 24;
	/**
	 * The rate, or speed, at which Faby falls.
	 */
	final float acceleration = -0.35F;
	/**
	 * Context to the main class.
	 */
	private final FlappyBird context;
	/**
	 * To control Faby, we place the sprites into a Node. As the node moves, so do all the sprites that have been
	 * attached to it.
	 */
	Node node = new Node("faby");
	/**
	 * Faby's current velocity.
	 */
	float velocity = 0;
	
	/**
	 * Faby's current position.
	 */
	float position;
	
	/**
	 * This is the constructor of the class Faby.
	 *
	 * @param context: We need to access some methods from the main class, so we need reference to it. I've named it
	 *                 "context".
	 */
	public Faby(FlappyBird context) {
		/* Write down the context to the main class. */
		this.context = context;
		
		/* We load the three animation sprites for Faby. Because we know the order we are adding these to the node, we can
		 * reference them later by their index number. */
		node.attachChild(context.getSprite("yellowbird-downflap.png"));
		node.attachChild(context.getSprite("yellowbird-midflap.png"));
		node.attachChild(context.getSprite("yellowbird-upflap.png"));
		
		/* When we create Faby, we need to attach it to the main class's GUI node. This allows it to be seen on the screen. */
		context.getGuiNode().attachChild(node);
		
		/* Faby should start midway above the ground. */
		position = context.centerY();
	}
	
	public void tick() {
		/* We update the sprite's animation. Here's how I'm calculating what frame to show:
		   - velocity < -1				upflap
		   - velocity between -1 and 1	midflap
		   - velocity > 1				downflap
		 */
		if (velocity < -1) {
			node.getChild(0).setCullHint(Spatial.CullHint.Always);      // downflap	X
			node.getChild(1).setCullHint(Spatial.CullHint.Always);      // midflap	X
			node.getChild(2).setCullHint(Spatial.CullHint.Dynamic);     // upflap	✓
		}
		if (velocity > -1 && velocity < 1) {
			node.getChild(0).setCullHint(Spatial.CullHint.Always);      // downflap	X
			node.getChild(1).setCullHint(Spatial.CullHint.Dynamic);     // midflap	✓
			node.getChild(2).setCullHint(Spatial.CullHint.Always);      // upflap	X
		}
		if (velocity > 1) {
			node.getChild(0).setCullHint(Spatial.CullHint.Dynamic);     // downflap	✓
			node.getChild(1).setCullHint(Spatial.CullHint.Always);      // midflap	X
			node.getChild(2).setCullHint(Spatial.CullHint.Always);      // upflap	X
		}
		
		/* We only want to do physics if we are playing, or have just died. */
		if (context.state.equals("playing") || context.state.equals("dead")) {
			/* Since acceleration is the rate of change of velocity, we add the acceleration to the velocity vector each frame. */
			velocity += acceleration;
			
			/* Since velocity is the rate of change of position, we add the velocity to the position vector each frame. */
			position += velocity;
		}
		
		/* We don't want Faby to go off the screen, so we check if Faby is touching the base. If it is, we set the state
		 * to dead. Faby is touching the base if its y-position is less than the base's y-position, plus its height. */
		if (position < (112 - 50)) { // 112 is the height of the base, and I moved it down by 50 pixels.
			position = 62; // 62 = 112 - 50
			velocity = 0;
			context.state = "dead";
		}
		
		/* We set the sprite's position to reflect our physics calculations. */
		node.setLocalTranslation(
				context.centerX() - 18, // We want Faby to be centered horizontally on-screen
				position, // Faby's y-coordinate is its position
				200 // We put Faby on layer 200 so that it should be in front of everything else
		);
		
		/* We can rotate the sprite to make it look more realistic. We rotate it by a multiple of its current velocity. */
		node.setLocalRotation(new Quaternion().fromAngles(0, 0, velocity * 0.05F));
	}
	
	/**
	 * To make Faby jump, we simply make its velocity positive. I found 5 to be a good number.
	 */
	public void jump() {
		velocity = 5;
	}
	
	/**
	 * This method determines if Faby is touching one of the pipes in a given barrier. If it is, we return true. If it
	 * is not touching a pipe, we return false.
	 * <p>
	 * The way we calculate an intersection is by comparing the coordinates of both sprites and performing some
	 * inequalities.
	 *
	 * @param barrier: the barrier to check for intersection
	 * @return true if Faby is intersecting the barrier, false otherwise
	 */
	public boolean intersects(Barrier barrier) {
		/* I write down node.getWorldTranslation() into a variable so that I don't have to write that whole thing every time */
		Vector3f faby = node.getWorldTranslation();
		for (Spatial x : barrier.node.getChildren()) { // Check all children (should just be two; one for each pipe)
			Vector3f pipe = x.getWorldTranslation();
			/* If the sprites overlap in any way */
			if (faby.x + 34 > pipe.x && faby.x < pipe.x + 52 && faby.y + 24 > pipe.y && faby.y < pipe.y + 320) {
				return true; // We are intersecting
			}
		}
		/* If we checked both pipes and neither of them intersected, we can return false. */
		return false;
	}
}
