package model;

import java.util.Scanner;

import exceptions.DungeonTooSmallException;
import exceptions.MissingEntranceRoomException;
import exceptions.MissingExitRoomException;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	
	
	public static boolean isAnswerRandom() {
		int answer = scanner.nextInt();
		while (answer != 1 && answer != 2) {
				System.out.println("Insert a correct Answer");
				answer = scanner.nextInt();
		}
		return answer == 1 ;
	}

	public static void main(String[] args) {
		System.out.println("1) Random dj\n2) Dungeon from text");
		if (isAnswerRandom()) {
			Play(Dungeon.random_init_min_arg, Dungeon.random_init_max_arg, true);
		} else {
			Play(Dungeon.not_random_init_min_arg, Dungeon.not_random_init_max_arg, false);
		}
		System.out.println("Congratulation ! You have finished the game !");
	}

	private static void Play(int min, int max, boolean random) {
		Dungeon dungeon = null;
		try {
			for (int i = min; i < max; i++) { 
				if(random) {
					dungeon = new Dungeon(i);
				}
				else {
					dungeon = new Dungeon("dj" + i + ".txt");
				}
				System.out.println("Dungeon number " + (i - 3));
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