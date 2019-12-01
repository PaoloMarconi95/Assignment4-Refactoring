package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import exceptions.DungeonTooSmallException;
import exceptions.MissingEntranceRoomException;
import exceptions.MissingExitRoomException;
import rooms.Room;


public class Dungeon {

	Scanner	scanner = new Scanner(System.in);
	public Player player = new Player();
	private boolean	playerMoved	= true;

	private ArrayList<Room>	rooms = new ArrayList<Room>();
	private String line;
	private Room entrance, exit;
	public static final int random_init_min_arg = 4;
	public static final int random_init_max_arg = 7;
	public static final int not_random_init_min_arg = 1;
	public static final int not_random_init_max_arg = 4;

	public Dungeon(){
	}
	
	public Dungeon(int size) throws DungeonTooSmallException, 
	MissingExitRoomException, MissingEntranceRoomException {
		rooms = RandomGenerate.generate(size);
		initEntranceAndExit();
		initPlayer();
	}
	
	public Dungeon(String s) throws MissingExitRoomException,
	MissingEntranceRoomException {
		rooms = GenerateFromFile.generateDjFromFile(new File(s));
		initEntranceAndExit();
		initPlayer();
	}

	private void initEntranceAndExit() throws MissingExitRoomException,
	MissingEntranceRoomException {
		for (Room r : rooms) {
			if (r.isEntrance()) {
				entrance = r;
			} else if (r.isExit()) {
				exit = r;
			}
		}
		if (exit == null)
			throw new MissingExitRoomException();
		else if (entrance == null)
			throw new MissingEntranceRoomException();
	}

	public void update() {
		System.out.println("Please choose a direction bewteen 'north', 'east', 'south' or 'west'.");
		if (playerMoved) {
			player.useTorch();
		}
		line = scanner.nextLine();
		executeCommand(line.toLowerCase());
		player.getCurrentRoom().act(player);

	}

	public void initPlayer() {
		player = new Player();
		player.setCurrentRoom(entrance);
	}

	public boolean isFinish() {
		return player.getCurrentRoom().isExit();
	}
	
	public boolean isGameOver() {
		return (player.getHealth() < 1) || player.getCurrentRoom().isExit();
	}

	public boolean canPlayerGoTo(Direction dir) {
		if (player.getCurrentRoom().neighbors.containsKey(dir)) {
			// key is needed
			if (player.getCurrentRoom().getNextRoom(dir).isLocked()) {
				if (!player.hasKeyForRoom(player.getCurrentRoom().getNextRoom(dir))) {
					System.out.println("You need a key to enter in this room");
					return false;
				}
			}
		} else {
			System.out.println("There is no way on '" + dir + "' side !");
			return false;
		}

		return true;
	}

	public void executeCommand(String line) {
		Direction direction = null;
		switch (line.charAt(0)) {
		case 'n':
			direction = Direction.NORTH;
			break;
		case 'e':
			direction = Direction.EAST;
			break;
		case 's':
			direction = Direction.SOUTH;
			break;
		case 'w':
			direction = Direction.WEST;
			break;
		case 'p':
			player.useHealthPotion();
			break;
		case 'i':
			player.displayInventory();
			break;
		default:
			System.out.println("No command found");
			break;
		}
		if (direction != null && canPlayerGoTo(direction)) {
			playerMoved = true;
			player.setCurrentRoom(player.getCurrentRoom().getNextRoom(direction));// change the current room of the player
		} else {
			playerMoved = false;
		}
	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}

	public Room getEntrance() {
		return entrance;
	}

	public Room getExit() {
		return exit;
	}

	public void setEntrance(Room entrance) {
		this.entrance = entrance;
	}

}
