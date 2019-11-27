package model;

import java.util.Scanner;

import exceptions.DungeonTooSmallException;
import exceptions.MissingEntranceRoomException;
import exceptions.MissingExitRoomException;

public class Main {

	static Scanner sc = new Scanner(System.in);
	public static final int random_init_min_arg = 4;
	public static final int not_random_init_min_arg = 4;

	public static int getAnwser() {
		int response = 0;
		while (sc.hasNext() && 
				(Integer.parseInt(sc.nextLine()) != 1 || Integer.parseInt(sc.nextLine()) != 2)) {
			System.out.println("Insert a correct Answer");
		}
		return response;
	}

	public static void main(String[] args) {
		System.out.println("1) Random dj\n2) Dungeon from text");
		Dungeon dungeon = new Dungeon();
		int response = Main.getAnwser();
		if (response == 1) {
			initializeRandom(dungeon);
		} else {
			initializeNotRandom(dungeon);
		}
		System.out.println("Congratulation ! You have finished the game !");
	}

	private static void initializeNotRandom(Dungeon dungeon) {
		try {
			for (int i = not_random_init_min_arg; i < 4; i++) {
				dungeon.initFromFile("dj" + i + ".txt");
				System.out.println("Dungeon number " + i);
				while (!dungeon.isGameOver()) {
					dungeon.update();
				}
				if (!dungeon.isFinish()) {// if the player is dead, he can retry the level
					i--;
					System.out.println("You loose ! The level is restarted !");
				}
			}

		} catch (MissingExitRoomException | MissingEntranceRoomException e) {
			e.printStackTrace();
		}

	}

	private static void initializeRandom(Dungeon dungeon) {
		try {
			for (int i = random_init_min_arg; i < 7; i++) { 
				System.out.println("Dungeon number " + (i - 3));
				dungeon.randomInit(i);
				while (!dungeon.isGameOver())
					dungeon.update();
				if (!dungeon.isFinish()) {// if the player is dead, he can retry the level
					i--;
					System.out.println("You loose ! The level is restarted randomly... !");
				}
			}
		} catch (MissingExitRoomException | MissingEntranceRoomException | DungeonTooSmallException e) {
			e.printStackTrace();
		}
	}

}
