package model;

import java.io.File;
import java.util.ArrayList;

import exceptions.CorruptedFileException;
import items.Key;
import monsters.*;
import rooms.Question;
import rooms.Room;
import rooms.RoomFactory;

public class GenerateFromFile {

	public static ArrayList<Room> generateDjFromFile(File f) {
		ArrayList<Room> rooms_to_set = new ArrayList<Room>();
		FileParser fp = new FileParser(f);
		ArrayList<String[]> rooms_from_file = fp.parseLines(" +");
		createRooms(rooms_from_file, rooms_to_set);  // create all the rooms
		connectRooms(rooms_from_file, rooms_to_set); // connect all the rooms
		return rooms_to_set;
	}

	public static Room getRoomNumber(int i, ArrayList<Room> rooms) {
		for (Room room : rooms) {
			if (room.getNumber() == i)
				return room;
		}
		return null;
	}

	public static ArrayList<Question> getAllQuestions(String file) throws CorruptedFileException {
		ArrayList<Question> questions = new ArrayList<>();
		FileParser fp = new FileParser(new File(file));
		ArrayList<String[]> list = fp.parseLines("/");
		for (String[] line : list) {
			if (line.length < 3)
				throw new CorruptedFileException();
			Question new_question = new Question(line[0], line[1], line[2]);
			for (int i = 3; i < line.length; i++)
				new_question.addAnswer(line[i]);
			questions.add(new_question);
		}
		return questions;
	}

	private static void createRooms(ArrayList<String[]> roomList, ArrayList<Room> rooms) {
		for (String[] strings : roomList) {
			Room r = checkRoomType(strings[5], rooms);
			if (!strings[6].equals("*"))
				r.setKey(new Key(Integer.parseInt(strings[6])));
			if (strings[7].equals("TRUE"))
				r.setHasTorch(true);
			if (strings[8].equals("TRUE"))
				r.setLocked(true);
		}
	}

	private static Room checkRoomType(String s, ArrayList<Room> rooms){
		Room r;
		// String[5] must contains the type of the room
		if(s.equals("Entrance")){
			r = RoomFactory.generateEntranceRoom(rooms);
		} else if(s.equals("Exit")){
			r = RoomFactory.generateExitRoom(rooms);
		} else if(s.equals("Enigma")){
			r = RoomFactory.generateEnigmaRoom(rooms);
		} else if(s.equals("Trap")){
			r = RoomFactory.generateTrapRoom(rooms);
		} else if(s.equals("Arakne")){
			r = RoomFactory.generateMonsterRoom(rooms, new Arakne());
		} else if(s.equals("Glouton")){
			r = RoomFactory.generateMonsterRoom(rooms, new Glouton());
		} else if(s.equals("Gnome")){
			r = RoomFactory.generateMonsterRoom(rooms, new Gnome());
		} else if(s.equals("Troll")){
			r = RoomFactory.generateMonsterRoom(rooms, new Troll());
		} else if(s.equals("Vampire")){
			r = RoomFactory.generateMonsterRoom(rooms, new Vampire());
		} else {
			r = RoomFactory.generateRoom(rooms);
		}
		return r;
	}

	private static void connectRooms(ArrayList<String[]> roomList, ArrayList<Room> rooms) {
		for (String[] list : roomList) {
			String room = list[0];
			for (int i = 1; i < 5; i++) {
				String room2 = list[i];
				if (!room2.equals("*")) {
					Direction direction = Direction.chooseDirection(i);
					int numRoom1 = Integer.parseInt(room);
					int numRoom2 = Integer.parseInt(room2);
					Room r1 = getRoomNumber(numRoom1, rooms);
					Room r2 = getRoomNumber(numRoom2, rooms);
					RoomFactory.connectRoom(r1, direction, r2);
				}
			}
		}
	}
	
}
