package ui;

import java.io.IOException;

import javax.swing.JFrame;

import model.GameLogic;

public class GameWindow {

	public static void show() {
		GameLogic model = new GameLogic();


		JFrame frame = new JFrame("CSSE220 Final Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		try {
			frame.add(new GameComponent(model));
		} catch (IOException e) {
			e.printStackTrace();
		}


		frame.setSize(800, 800);
		frame.setVisible(true);
		}

}
