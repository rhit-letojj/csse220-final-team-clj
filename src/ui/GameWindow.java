package ui;

import java.io.IOException;

import javax.swing.JFrame;

import model.GameLogic;

public class GameWindow {

	public static void show() {
		// Minimal model instance (empty for now, by design)
		GameLogic model = new GameLogic();


		JFrame frame = new JFrame("CSSE220 Final Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		try {
			frame.add(new GameComponent(model));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null); // center on screen (nice UX, still minimal)
		frame.setVisible(true);
		}

}
