package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import exceptions.DungeonTooSmallException;
import exceptions.UnknowRoomTypeException;
import items.Key;
import monsters.Glouton;
import rooms.Room;
import rooms.RoomFactory;

public class RandomGenerate {

	public static ArrayList<Room> generate(int size) throws DungeonTooSmallException{
		if(size<4)
			throw new DungeonTooSmallException();
		ArrayList<Room> rooms = new ArrayList<>();
		generateLinearDj(size,rooms);
		generateLabyPath(size/2,rooms);
		generateKey(rooms);
		return rooms;
	}


	private static ArrayList<Room> getLockedRoom(ArrayList<Room> rooms) {
		ArrayList<Room> roomlist = new ArrayList<>();
		for (Room room : rooms) {
			if(room.isLocked())
				roomlist.add(room);
		}
		return roomlist;
	}


	public static void getFirstRoomLockedFrom(Room room, ArrayList<Room> list, ArrayList<Room> parcours){	
		parcours.add(room);
		if(list.isEmpty()){
			for(Entry<Direction, Room> entry : room.getNeighbors().entrySet()) {
				Room next = entry.getValue();
				if(next.isLocked())
					list.add(next);
				else if(!parcours.contains(next)){
					getFirstRoomLockedFrom(next, list, parcours);
				}
			}
		}

	}

	public static void getConnectedRoom(Room room, ArrayList<Room> list){

		for(Entry<Direction, Room> entry : room.getNeighbors().entrySet()) {
			Room current = entry.getValue();
			if(!list.contains(current) && !current.isLocked() ){
				list.add(current);
				getConnectedRoom(current, list);
			}

		}
	}

	
	private static void generateKey(ArrayList<Room> rooms) {

		ArrayList<Room> list = new ArrayList<>();
		ArrayList<Room> roomsLocked = getLockedRoom(rooms);
		Room entrance = rooms.get(getEntranceRoomNumber(rooms));
		Room exit = rooms.get(getExitRoomNumber(roomsLocked)); 

		ArrayList<Room> parcours = new ArrayList<>();
		ArrayList<Room> oneRoom = new ArrayList<>();
		RandomGenerate.getFirstRoomLockedFrom(entrance, oneRoom, parcours);
		Room locked;
		boolean impossible = false;

		if(!oneRoom.isEmpty())
			locked = oneRoom.get(0);
		else
			locked = null;

		while(locked != null){

			Key k = new Key(locked.getNumber());

			list.clear();
			RandomGenerate.getConnectedRoom(entrance, list); // stocke les room accessibles depuis l'entree dans list
			if(list.isEmpty()){
				impossible = true;
				break;
			}

			int alea = Randomizer.random.nextInt(list.size());
			while(list.get(alea).getKey() != null && (list.get(alea)!= exit) ){
				alea = Randomizer.random.nextInt(list.size());
			}

			list.get(alea).setKey(k);
			locked.setLocked(false);

			oneRoom.clear();
			parcours.clear();
			RandomGenerate.getFirstRoomLockedFrom(entrance, oneRoom, parcours);
			if(!oneRoom.isEmpty())
				locked = oneRoom.get(0);
			else
				locked = null;
		}
		if(!impossible)
			for (Room room : roomsLocked) {
				room.setLocked(true);
			}
		else
			for (Room room : roomsLocked) {
				room.setLocked(false);
			}


	}


	private static void generateLabyPath(int size, ArrayList<Room> rooms) {
		for (int i = 0; i < size; i++) {
			extendDj(rooms, i%2);
		}
	}
	

	private static void extendDj(ArrayList<Room> rooms, int i) {
		ArrayList<Room> toExtends = new ArrayList<>();
		boolean monster = false;
		boolean sphinx = false;
		Collections.shuffle(rooms);
		for (int j = 0; j < rooms.size(); j++) {
			Room current = rooms.get(j);
			//if i == 0 we look for the rooms with 2 neigbours
			if(i == 0 && current.getNeighborsCount() == 2 )
				toExtends.add(current);
			//if i == 1 we look for the room with 1 neigbours and not the exit
			else if(i == 1 && current.getNeighborsCount() == 1 && !current.isExit() )
				toExtends.add(current);
		}

		for (int j = 0; j < toExtends.size(); j++) {
			Room current = toExtends.get(j);
			Direction dir = Direction.getRandomDirection();
			//while we don't find a empty direction to connect the current room to the previous room
			while(current.getNextRoom(dir)!= null)
				dir = Direction.getRandomDirection();
			try {
				Room newRoom;
				if(!monster){
					newRoom = RoomFactory.generateRandomMonsterRoom(rooms);
					monster = true;
				}
				else if(!sphinx){
					newRoom = RoomFactory.generateEnigmaRoom(rooms);
					sphinx = true;
				}
				else
					newRoom = RoomFactory.generateRandomRoom(rooms);
				if(Math.random()*101<20)
					newRoom.setLocked(true);

				RoomFactory.connectRoom(current, dir, newRoom);
			} catch (UnknowRoomTypeException e) {
				e.printStackTrace();
			}
		}
	}


	private static int getExitRoomNumber(ArrayList<Room> rooms) {
		for(Room r : rooms){
			if(r.isExit())
				return r.getNumber();
		}
		return 0;
	}

	private static int getEntranceRoomNumber(ArrayList<Room> rooms) {
		for(Room r : rooms){
			if(r.isEntrance())
				return r.getNumber();
		}
		return 0;
	}

	/**
	 * @param size the size from the entrance to the exit
	 * @param rooms all the rooms of the dungeon
	 * This function create start by the end of the dungeon. It's create the exit and the room just before
	 * then it basically add a new room connected to the previous one till we get the rooms size requiered 
	 * 
	 */
	public static void generateLinearDj(int size, ArrayList<Room> rooms) {

		Room exit = RoomFactory.generateExitRoom(rooms);
		exit.setLocked(true);
		Room beforeExit;

		beforeExit = Math.random()*101 > 50 ? 	RoomFactory.generateRoom(rooms) :
			RoomFactory.generateMonsterRoom(rooms, new Glouton());

		RoomFactory.connectRoom(exit, Direction.getRandomDirection(), beforeExit);

		for (int i = 2; i < size; i++) {
			//TODO maybe add a random for different room type
			Room current = RoomFactory.generateRoom(rooms);
			//get the room before in the arraylist (to make one path to the exit)
			Room before = rooms.get(current.getNumber()-2);
			Direction direction = Direction.getRandomDirection();
			//while we don't find an empty direction to connect the current room to the previous room
			while(before.getNextRoom(direction)!= null)
				direction = Direction.getRandomDirection();
			RoomFactory.connectRoom(before, direction, current);
			if(i == size-1)
				current.setEntrance(true);
		}
	}	
}