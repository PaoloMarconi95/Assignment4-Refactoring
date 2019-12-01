package model;

import java.util.Scanner;

import exceptions.DungeonTooSmallException;
import exceptions.MissingEntranceRoomException;
import exceptions.MissingExitRoomException;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	
	
	public static int getAnwser() {
		int answer = scanner.nextInt();
		while (answer != 1 && answer != 2) {
				System.out.println("Insert a correct Answer");
				answer = scanner.nextInt();
		}
		return answer;
	}

	public static void main(String[] args) {
		System.out.println("1) Random dj\n2) Dungeon from text");
		int response = Main.getAnwser();
		if (response == 1) {
			initializeRandom();
		} else {
			initializeNotRandom();
		}
		System.out.println("Congratulation ! You have finished the game !");
	}

	private static void initializeNotRandom() {
		try {
			for(int i = Dungeon.not_random_init_min_arg; i < Dungeon.not_random_init_max_arg; i++) {
				Dungeon dungeon = new Dungeon("dj" + i + ".txt");
				System.out.println("Dungeon number " + i);
				while (!dungeon.isGameOver()) {
					dungeon.update();
				}
				if (!dungeon.isFinish()) {// if the player is dead, he can retry the level
					i--;
					System.out.println("You loose ! The level is restarted !");
				}
			} // end of for
		} catch (MissingExitRoomException | MissingEntranceRoomException e) {
			e.printStackTrace();
		}
	}

	private static void initializeRandom() {
		try {
			for (int i = Dungeon.random_init_min_arg; i < Dungeon.random_init_max_arg; i++) { 
				System.out.println("Dungeon number " + (i - 3));
				Dungeon dungeon = new Dungeon(i);
				while (!dungeon.isGameOver())
					dungeon.update();
				if (!dungeon.isFinish()) {// if the player is dead, he can retry the level
					i--;
					System.out.println("You loose ! The level is restarted randomly... !");
				}
			}
		} catch (DungeonTooSmallException | MissingExitRoomException | MissingEntranceRoomException e) {
			e.printStackTrace();
		}
	}

}