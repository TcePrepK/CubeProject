package Project.Core;

import enigma.console.TextWindow;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import enigma.event.TextMouseMotionListener;

public class Mouse {
	public int cx, cy = 0; // Cursor x, y in console. Updates when clicked.
	public int x, y = 0; // Mouse x, y. Updates every frame.
	private boolean[] buttonDown = new boolean[3]; // Left-Middle-Right buttons.
	
	private int xScale = 2;
	private int yScale = 1;

	public Mouse(TextWindow textWindow) {
		// These "Listener"s are not important. Just know that they get called whenever "something" happens.
		// Such as when mouse is dragged "mouseDragged" gets called.
		textWindow.addTextMouseMotionListener(new TextMouseMotionListener() {
			@Override
			public void mouseDragged(TextMouseEvent e) {
				x = e.getX() / xScale;
				y = e.getY() / yScale;
			}

			@Override
			public void mouseMoved(TextMouseEvent e) {
				x = e.getX() / xScale;
				y = e.getY() / yScale;
			}
		});
		
		// Same as the other one.
		textWindow.addTextMouseListener(new TextMouseListener() {
			@Override
			public void mouseClicked(TextMouseEvent e) {}

			@Override
			public void mousePressed(TextMouseEvent e) {
				cx = e.getX() / xScale;
				cy = e.getY() / yScale;
				
				// getButton() returns 1 for left click (so -1)
				int button = e.getButton() - 1;
				if (button > 2) {
					return;
				}
				
				buttonDown[button] = true;
			}

			@Override
			public void mouseReleased(TextMouseEvent e) {
				int button = e.getButton() - 1;
				if (button > 2) {
					return;
				}
				
				buttonDown[button] = false;
			}
		});
	}
	
	public boolean isLeftDown() {
		return buttonDown[0];
	}
	
	public boolean isRightDown() {
		return buttonDown[2];
	}
}
