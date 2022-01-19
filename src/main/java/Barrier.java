import com.jme3.scene.Node;

public class Barrier {

	private final FlappyBird context;

	/**
	 * This node contains both the bottom pipe and the bottom pipe. Within this node, the pipes have been moved apart,
	 * but this node moves to move both pipes up and down.
	 */
	final Node node;

	/**
	 * The current x-position of the barrier.
	 */
	float xPosition;

	/**
	 * The y-position of the barrier.
	 */
	float yPosition;

	/**
	 * Creates a barrier, which consists of both the bottom and top pipe.
	 *
	 * @param context: context to the main class
	 * @param index: this pipe's "number", where the first pipe is 0, the second pipe is 1, etc.
	 */
	public Barrier(FlappyBird context, int index) {
		/* Write down the context to the main class. */
		this.context = context;

		/* Create the node that is going to contain the pipes. */
		node = new Node("barrier " + index);
		
		/* Move the node back so that it is not in front of other nodes. */
		node.setLocalTranslation(0, 0, -1);

		/* Load both up and down pipes. */
		node.attachChild(context.getSprite("pipe-green-up.png"));
		node.attachChild(context.getSprite("pipe-green-down.png"));

		/* We need to move the down pipe upwards so that there is space in between the pipes. The height of a pipe is 320,
		 * so I'm going to move it up by that amount, plus 100. */
		node.getChild(1).move(0, 420, 0);

		/* We adjust the y-position of the pipes so that the middle-point of the gap in between the pipes is in the
		 * center of the screen vertically. Since we know this point is 320 + 50 = 370, we can take the vertical center
		 * of the screen and subtract this amount, giving a difference for which we can adjust for. */
		yPosition = context.centerY() - 370;

		/* We can then adjust this number randomly so that the middle-point of the gap in the pipes varies. Since
		 *  Math.random() returns a decimal from 0 to 1, we can subtract 0.5 so that our number end up in the range
		 * -0.5 to 0.5. We do this because we would like some random numbers as well. We then multiply this result by a
		 * number like 200 to make the effect more dramatic. */
		yPosition += (float) ((Math.random() - 0.5) * 200);

		/* Attach this barrier to the GUI node so that it is seen on-screen. */
		context.getGuiNode().attachChild(node);

		/* We start the first pipe at position 600, then add each pipe afterwards at 200-pixel intervals. */
		xPosition = 600 + 200 * index;
	}

	public void tick() {
		/* We only want to move the pipes left if the game is playing. Using a value of 3 seems like a reasonable speed. */
		if (context.state.equals("playing")) {
			xPosition -= 3;
		}

		/* We update the position of the node to reflect that change. */
		node.setLocalTranslation(xPosition, yPosition, -1); // -1 is the z-position of the node to move it back
	}
}
