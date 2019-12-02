package rooms;

import java.util.ArrayList;

import exceptions.UnknowRoomTypeException;
import model.Direction;
import model.Randomizer;
import monsters.*;


public class RoomFactory {

			
	public static Room generateRandomRoom(ArrayList<Room> rooms) throws UnknowRoomTypeException{
		int alea = Randomizer.random.nextInt(101);
		Room room;
		if(alea < 15)
			room = RoomFactory.generateTrapRoom(rooms);
		else if(alea < 25)
			room = RoomFactory.generateEnigmaRoom(rooms);
		else if(alea < 35)
		room = generateRandomMonsterRoom(rooms);
		else
			room = RoomFactory.generateRoom(rooms);
		if(Randomizer.random.nextInt(101) > 65)
			room.setHasTorch(true);
		if(Randomizer.random.nextInt(101) > 85)
			room.setHasPotion(true);
		return room;
	}


	/**
	 * Generate NORMAL room
	 * @param rooms: list of rooms already existing
	 * @return new room
	 */
	public static Room generateRoom(ArrayList<Room> rooms){
		Room room = new Room(rooms.size() + 1);
		rooms.add(room);
		return room;
	}

	/**
	 * Generate MONSTER room
	 * @param rooms: list of rooms already existing
	 * @param monster: monster in the room
	 * @return new monster room
	 */
	public static Room generateMonsterRoom(ArrayList<Room> rooms, Monster monster){
		Room room = new MonsterRoom(rooms.size() + 1, monster);
		rooms.add(room);
		return room;
	}

	/**
	 * Generate ENIGMA room
	 * @param rooms: list of rooms already existing
	 * @return new enigma room
	 */
	public static Room generateEnigmaRoom(ArrayList<Room> rooms){
		Room room = new EnigmaRoom(rooms.size() + 1);
		rooms.add(room);
		return room;
	}

	/**
	 * Generate TRAP room
	 * @param rooms: list of rooms already existing
	 * @return new trap room
	 */
	public static Room generateTrapRoom(ArrayList<Room> rooms){
		Room room = new TrapRoom(rooms.size() + 1);
		rooms.add(room);
		return room;
	}

	/**
	 * Generate EXIT room
	 * @param rooms: list of rooms already existing
	 * @return new exit room
	 */
	public static Room generateExitRoom(ArrayList<Room> rooms){
		Room room = new Room(rooms.size() + 1);
		room.setExit(true);
		rooms.add(room);
		return room;
	}

	/**
	 * Generate ENTRANCE room
	 * @param rooms: list of rooms already existing
	 * @return new entrance room
	 */
	public static Room generateEntranceRoom(ArrayList<Room> rooms){
		Room room = new Room(rooms.size() + 1);
		room.setEntrance(true);
		rooms.add(room);
		return room;
	}


	/**
	 * @param room the first room we have
	 * @param dir the direction from the first room where we want the connection
	 * @param room2 -> the room we want to connect to another room
	 * ex :  (room1, NORTH, room2) will connect the room2 on the north side of the room1
	 * 
	 */
	public static void connectRoom(Room room, Direction dir, Room room2 ){
		if(room == room2){
			System.out.println("Can't connect a room with itself");
		}
		//usefull when we load the dj from a file
		if(!room.alreadyConnected(room2) && room.getNextRoom(dir) == null){
			room.getNeighbors().put(dir, room2);
			if(dir == Direction.NORTH)
				room2.getNeighbors().put(Direction.SOUTH, room);
			else if(dir == Direction.EAST)
				room2.getNeighbors().put(Direction.WEST, room);

			else if(dir == Direction.SOUTH)
				room2.getNeighbors().put(Direction.NORTH, room);

			else if(dir == Direction.WEST)
				room2.getNeighbors().put(Direction.EAST, room);
		}
	}


	public static Room generateRandomMonsterRoom(ArrayList<Room> rooms) {
		int alea = Randomizer.random.nextInt(101);
		if(alea < 30)
			return RoomFactory.generateMonsterRoom(rooms, new Glouton());
		else if(alea < 55)
			return RoomFactory.generateMonsterRoom(rooms, new Arakne());
		else if(alea < 75)
			return RoomFactory.generateMonsterRoom(rooms, new Gnome());
		else if(alea < 90)
			return RoomFactory.generateMonsterRoom(rooms, new Vampire());
		else
			return RoomFactory.generateMonsterRoom(rooms, new Troll());
	}



}
