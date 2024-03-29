package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import items.Torch;
import items.weapons.Baton;
import items.HealPotion;
import items.Key;
import items.weapons.Mace;
import items.weapons.Spike;
import items.weapons.Sword;
import items.weapons.Weapon;

import java.io.File;
import java.util.ArrayList;

import model.Direction;
import model.Dungeon;
import model.GenerateFromFile;
import model.Player;
import model.RandomGenerate;
import model.Randomizer;
import monsters.Arakne;
import monsters.Glouton;
import monsters.Monster;
import monsters.Vampire;
import rooms.*;
import exceptions.CorruptedFileException;
import exceptions.DungeonTooSmallException;
import exceptions.MissingEntranceRoomException;
import exceptions.MissingExitRoomException;
import exceptions.UnknowRoomTypeException;


public class Test {


	
	@org.junit.Test
	public void testGetFirstLockedRoom() throws UnknowRoomTypeException{
		ArrayList<Room> list = new ArrayList<>();
		
		ArrayList<Room> locked = new ArrayList<>();
		ArrayList<Room> parcours = new ArrayList<>();
		
		Room r2 = RoomFactory.generateRoom(list);
		Room r3 = RoomFactory.generateRoom(list);
		Room r4 = RoomFactory.generateRoom(list);
		Room r5 = RoomFactory.generateRoom(list);
		Room r1 = RoomFactory.generateRoom(list);
		Room r6 = RoomFactory.generateRoom(list);

		RoomFactory.connectRoom(r1, Direction.NORTH, r2);
		RoomFactory.connectRoom(r2, Direction.NORTH, r3);
		RoomFactory.connectRoom(r3, Direction.NORTH, r4);
		RoomFactory.connectRoom(r4, Direction.NORTH, r5);
		RoomFactory.connectRoom(r3, Direction.EAST, r6);

		r6.setLocked(true);
		RandomGenerate.getFirstRoomLockedFrom(r1, locked, parcours);
		Room r ;
		if(!locked.isEmpty())
			r = locked.get(0);
		else
			r = new Room(1);
		assertTrue(r != null);
		assertEquals(6, r.getNumber());

	}




	@org.junit.Test
	public void testdungeonRandomInit() throws DungeonTooSmallException,
	MissingExitRoomException, MissingEntranceRoomException {
		for(int i = 4; i < 20; i++){
			Dungeon dungeon = new Dungeon(7);
			assertTrue(dungeon.getExit() != null);
			assertTrue(dungeon.getEntrance() != null);
		}
	}

	@org.junit.Test
	public void testInitFromFile() throws MissingExitRoomException,
	MissingEntranceRoomException {
		Dungeon dungeon = new Dungeon("dj1.txt");
		assertTrue(dungeon.getExit() != null);
		assertTrue(dungeon.getEntrance() != null);
	}

	@org.junit.Test(expected = MissingExitRoomException.class)
	public void testNoExit() throws MissingExitRoomException,
	MissingEntranceRoomException {
		Dungeon dungeon = new Dungeon("testDjNoExit.txt");
	}

	@org.junit.Test(expected = MissingEntranceRoomException.class)
	public void testNoEntrance() throws MissingExitRoomException,
	MissingEntranceRoomException {
		Dungeon dungeon = new Dungeon("testDjNoEntrance.txt");
	}

	@org.junit.Test
	public void testCanPlayerGoDir() {
		Dungeon dungeon = new Dungeon();

		Room room1 = new Room(1);
		Room room2 = new Room(2);

		room1.setEntrance(true);
		dungeon.setEntrance(room1);

		dungeon.getRooms().add(room1);
		dungeon.getRooms().add(room2);
		dungeon.player.setCurrentRoom(dungeon.getEntrance());

		RoomFactory.connectRoom(room1, Direction.EAST, room2);
		assertTrue(dungeon.canPlayerGoTo(Direction.EAST));
		assertFalse(dungeon.canPlayerGoTo(Direction.NORTH));
		assertFalse(dungeon.canPlayerGoTo(Direction.SOUTH));
		assertFalse(dungeon.canPlayerGoTo(Direction.WEST));
	}

	@org.junit.Test
	public void testQuestionParse() throws CorruptedFileException {
		ArrayList<Question> q = GenerateFromFile.getAllQuestions("lib_questions.txt");
		assertEquals(6, q.size());
	}

	@org.junit.Test
	public void testPlayerUsePotion() {
		Player player = new Player();
		Arakne a = new Arakne();
		// the arakne hit~10
		a.hit(player);
		player.getSecours().add(new HealPotion());
		player.getSecours().add(new HealPotion());
		// player has 3 potions (one from the init)

		int playerLife = player.getHealth();// save the player life after get hit
		player.useHealthPotion();// 2potions left
		// the life of the player has increase after use the potion
		assertTrue(playerLife < player.getHealth());
		// after using a +40 pdv potion, the life max is limit to 100
		assertEquals(player.getHealth(), 100);
		player.useHealthPotion();
		// When full life, player can't use any more potions
		assertEquals(player.getSecours().size(), 2);
		a.hit(player);
		playerLife = player.getHealth();
		player.getSecours().clear();
		// if player dont have health potion, he can't use it
		player.useHealthPotion();
		assertEquals(player.getHealth(), playerLife);

		player.getSecours().add(new HealPotion());
		player.setHealth(30);
		player.useHealthPotion();
		assertEquals(player.getHealth(), 70);
	}

	@org.junit.Test
	public void testPlayerHit() {
		Player player = new Player();
		Arakne a = new Arakne();
		int arakneLife = a.getHealth();
		int playerLife = player.getHealth();
		player.hit(a);
		a.hit(player);
		// the life of the player has decrease
		assertTrue(arakneLife > a.getHealth());
		// the life of the player has decrease
		assertTrue(playerLife > player.getHealth());
	}

	@org.junit.Test
	public void testIsANumber() {
		Room r = new Room(1);
		assertFalse(r.isANumber(""));
		assertFalse(r.isANumber("sqdqdq"));
		assertFalse(r.isANumber("  1   ef"));
		assertFalse(r.isANumber(" 12"));
		assertFalse(r.isANumber(" 1 5 "));
		assertTrue(r.isANumber("1"));
		assertTrue(r.isANumber("2"));
		assertTrue(r.isANumber("15"));
	}

	@org.junit.Test
	public void testRandomWeapon() {
		Player player = new Player();
		EnigmaRoom er = new EnigmaRoom(1);
		for (int i = 0; i < 20; i++) {
			er.giveRandomWeapon(player);
			Weapon w = player.getWeapon();
			assertTrue((w instanceof Sword) || (w instanceof Baton)
					|| (w instanceof Mace) || (w instanceof Spike));
		}

	}

	@org.junit.Test
	public void testEnigmaIsCorrectNumber() {
		EnigmaRoom er = new EnigmaRoom(1);
		assertFalse(er.isACorrectNumber(0));
		assertFalse(er.isACorrectNumber(-1));
		assertTrue(er.isACorrectNumber(2));
	}

	@org.junit.Test
	public void testRoomConnection() {
		Dungeon dungeon = new Dungeon();
		Room r1;
		Room r2;
		r1 = RoomFactory.generateRoom(dungeon.getRooms());
		r2 = RoomFactory.generateRoom(dungeon.getRooms());
		assertFalse(r1.getNeighbors().containsKey(Direction.NORTH));
		assertFalse(r1.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r1.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r1.getNeighbors().containsKey(Direction.SOUTH));

		assertFalse(r2.getNeighbors().containsKey(Direction.NORTH));
		assertFalse(r2.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r2.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r2.getNeighbors().containsKey(Direction.SOUTH));

		RoomFactory.connectRoom(r1, Direction.NORTH, r2);

		assertTrue(r1.getNeighbors().containsKey(Direction.NORTH));
		assertFalse(r1.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r1.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r1.getNeighbors().containsKey(Direction.SOUTH));
		assertTrue(r2.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r2.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r2.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r2.getNeighbors().containsKey(Direction.NORTH));

		assertTrue(r1.getNeighbors().containsKey(Direction.NORTH));
		assertTrue(r2.getNeighbors().containsKey(Direction.SOUTH));

		assertFalse(r1.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r1.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r1.getNeighbors().containsKey(Direction.WEST));

		assertFalse(r2.getNeighbors().containsKey(Direction.NORTH));
		assertFalse(r2.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r2.getNeighbors().containsKey(Direction.WEST));

		// can't connect two room two time
		RoomFactory.connectRoom(r1, Direction.SOUTH, r2);
		assertTrue(r1.getNeighbors().size() < 2);
	}

	@org.junit.Test
	public void testPlayerMove() {
		Dungeon dungeon = new Dungeon();
		Room r1, r2;
		r1 = RoomFactory.generateRoom( dungeon.getRooms());
		r2 = RoomFactory.generateRoom( dungeon.getRooms());
		RoomFactory.connectRoom(r1, Direction.NORTH, r2);
		dungeon.setEntrance(r1);
		// init player in room 1
		dungeon.player.setCurrentRoom(dungeon.getEntrance());
		assertTrue(dungeon.player.getCurrentRoom() == r1);
		assertTrue(dungeon.player.canGetInRoom(r2));
		dungeon.executeCommand("n");
		assertTrue(dungeon.player.getCurrentRoom() == r2);
		dungeon.executeCommand("s");
		assertTrue(dungeon.player.getCurrentRoom() == r1);
	}

	@org.junit.Test
	public void testPlayerOpenDoor() {
		Dungeon dungeon = new Dungeon();
		Room r1, r2;
		r1 = RoomFactory.generateRoom( dungeon.getRooms());
		r2 = RoomFactory.generateRoom( dungeon.getRooms());
		r2.setLocked(true);
		RoomFactory.connectRoom(r1, Direction.EAST, r2);
		dungeon.player.setCurrentRoom(dungeon.getEntrance());
		assertFalse(dungeon.player.canGetInRoom(r2));
		dungeon.player.addkey(new Key(2));
		assertTrue(dungeon.player.canGetInRoom(r2));

	}


	@org.junit.Test
	public void testRoomFactory() throws UnknowRoomTypeException {
		Room r = RoomFactory.generateRoom( new ArrayList<>());
		boolean isNormal = false;
		if (r instanceof Room) {
			isNormal = true;
		}
		assertTrue(isNormal);

		Room r1 = RoomFactory.generateEnigmaRoom( new ArrayList<>());
		boolean isEnigma = false;
		if (r1 instanceof EnigmaRoom) {
			isEnigma = true;
		}
		assertTrue(isEnigma);

		Room r2 = RoomFactory.generateMonsterRoom(new ArrayList<>(), new Arakne());
		boolean isMonster = false;
		if (r2 instanceof MonsterRoom) {
			isMonster = true;
		}
		assertTrue(isMonster);

		r2 = RoomFactory.generateMonsterRoom(new ArrayList<>(), new Glouton());
		isMonster = false;
		if (r2 instanceof MonsterRoom) {
			isMonster = true;
		}
		assertTrue(isMonster);

		Room r3 = RoomFactory.generateExitRoom(new ArrayList<>());
		boolean isExit = false;
		if ((r3 instanceof Room) && r3.isExit()) {
			isExit = true;
		}
		assertTrue(isExit);

		Room r4 = RoomFactory.generateEntranceRoom(new ArrayList<>());
		boolean isEntrance = false;
		if ((r4 instanceof Room) && r4.isEntrance()) {
			isEntrance = true;
		}
		assertTrue(isEntrance);

		Room r5 = RoomFactory.generateTrapRoom(new ArrayList<>());
		boolean isTrap = false;
		if (r5 instanceof TrapRoom) {
			isTrap = true;
		}
		assertTrue(isTrap);

	}

	@org.junit.Test
	public void testParseFile() {
		Dungeon dungeon = new Dungeon();
		dungeon.setRooms(GenerateFromFile.generateDjFromFile(new File("testDjNoExit.txt")));
		// Room1
		Room r1 = dungeon.getRooms().get(0);
		assertTrue(r1.getNeighbors().containsKey(Direction.NORTH));
		assertTrue(r1.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r1.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r1.getNeighbors().containsKey(Direction.SOUTH));

		// Room2
		Room r2 = dungeon.getRooms().get(1);
		assertTrue(r2.getNeighbors().containsKey(Direction.NORTH));
		assertTrue(r2.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r2.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r2.getNeighbors().containsKey(Direction.WEST));

		// Room3
		Room r3 = dungeon.getRooms().get(2);
		assertTrue(r3.getNeighbors().containsKey(Direction.NORTH));
		assertTrue(r3.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r3.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r3.getNeighbors().containsKey(Direction.SOUTH));

		// Room4
		Room r4 = dungeon.getRooms().get(3);
		assertTrue(r4.getNeighbors().containsKey(Direction.NORTH));
		assertTrue(r4.getNeighbors().containsKey(Direction.WEST));
		assertTrue(r4.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r4.getNeighbors().containsKey(Direction.EAST));

		// Room5
		Room r5 = dungeon.getRooms().get(4);
		assertTrue(r5.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r5.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r5.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r5.getNeighbors().containsKey(Direction.NORTH));

		// Room6
		Room r6 = dungeon.getRooms().get(5);
		assertTrue(r6.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r6.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r6.getNeighbors().containsKey(Direction.NORTH));
		assertFalse(r6.getNeighbors().containsKey(Direction.SOUTH));

		// Room7
		Room r7 = dungeon.getRooms().get(6);
		assertTrue(r7.getNeighbors().containsKey(Direction.SOUTH));
		assertFalse(r7.getNeighbors().containsKey(Direction.WEST));
		assertFalse(r7.getNeighbors().containsKey(Direction.EAST));
		assertFalse(r7.getNeighbors().containsKey(Direction.NORTH));

	}

	@org.junit.Test
	public void testQuestion() {
		Question q1 = new Question("qui est le plus puissant sith", "revan", "vador");
		assertTrue(q1.isCorrectAnswer(q1.getPossibleAnswer().indexOf("revan") + 1));
		assertFalse(q1.isCorrectAnswer(q1.getPossibleAnswer().indexOf("vador") + 1));
		q1.addAnswer("empereur");
		assertTrue(q1.getPossibleAnswer().contains(new String("empereur")));
	}

	@org.junit.Test(expected = DungeonTooSmallException.class)
	public void testRandomdungeonException() throws DungeonTooSmallException,
	UnknowRoomTypeException {
		RandomGenerate.generate(3);
		RandomGenerate.generate(2);
		RandomGenerate.generate(1);
		RandomGenerate.generate(0);
	}

	@org.junit.Test
	public void testRandomdungeonSize() {
		ArrayList<Room> rooms = new ArrayList<>();
		RandomGenerate.generateLinearDj(4, rooms);
		assertEquals(rooms.size(), 4);

		ArrayList<Room> rooms2 = new ArrayList<>();
		RandomGenerate.generateLinearDj(12, rooms2);
		assertEquals(rooms2.size(), 12);

		ArrayList<Room> rooms3 = new ArrayList<>();
		RandomGenerate.generateLinearDj(47, rooms3);
		assertEquals(rooms3.size(), 47);
	}

	@org.junit.Test
	public void testLineardungeonConnection() {
		for (int i = 0; i < 10; i++) {
			ArrayList<Room> rooms = new ArrayList<>();
			RandomGenerate.generateLinearDj(Randomizer.random.nextInt(22) + 4, rooms);
			assertTrue(rooms.get(0).getNeighborsCount() == 1);
			for (int j = 1; j < (rooms.size() - 1); j++) {
				assertEquals(rooms.get(j).getNeighborsCount(), 2);
			}
			assertEquals(rooms.get(rooms.size() - 1).getNeighborsCount(), 1);
		}
	}

	@org.junit.Test
	public void testRandomdungeonConnection() {
		try {
			for (int i = 0; i < 10; i++) {
				ArrayList<Room> rooms = RandomGenerate.generate(4);
				assertEquals(rooms.size(), 9);
				// rooms = RandomGenerate.generate(5);
				// assertTrue(rooms.size() == 1);
			}
		} catch (DungeonTooSmallException e) {
			e.printStackTrace();
		}
	}

	@org.junit.Test
	public void testTorchReload() {
		Torch t = new Torch();
		t.reload();
		assertEquals(20, t.getFire());
	}

	@org.junit.Test
	public void testTorcheUse() {
		Room r1 = new Room(0);
		Torch t = new Torch();
		while (t.getFire() > 0) {
			int buff = t.getFire();
			t.use();
			assertEquals(buff - 1, t.getFire());
		}
		assertEquals(0, t.getFire());

	}

	@org.junit.Test
	public void testActivatedRoom() {
		TrapRoom trap = new TrapRoom(0);
		Player player = new Player();
		assertEquals(false, trap.isActivated());
		trap.act(player);
		assertEquals(player.getHealth(), 99);
		assertEquals(true, trap.isActivated());
		trap.act(player);
		assertEquals(player.getHealth(), 99);

	}

	@org.junit.Test
	public void testGameOver() {
		Dungeon dungeon = new Dungeon();
		dungeon.player.setHealth(0);
		assertEquals(false, dungeon.player.getHealth() > 0);
		assertEquals(true, dungeon.isGameOver());
		dungeon.player.setHealth(100);
		assertEquals(true, dungeon.player.getHealth() > 0);
		try {
			dungeon = new Dungeon(4);
		} catch (DungeonTooSmallException e) {

			e.printStackTrace();
		} catch (MissingExitRoomException e) {

			e.printStackTrace();
		} catch (MissingEntranceRoomException e) {

			e.printStackTrace();
		}
		dungeon.player.setCurrentRoom(dungeon.getExit());
		assertEquals(true, dungeon.isGameOver());
	}

	@org.junit.Test
	public void testMonsterRoomIsCorrectNumber() {
		MonsterRoom er = new MonsterRoom(1, new Vampire());
		assertFalse(er.isACorrectNumber(5));
		assertFalse(er.isACorrectNumber(-1));
		assertTrue(er.isACorrectNumber(2));
	}

	@org.junit.Test
	public void testMonsterIsAlive() {
		Monster m = new Arakne();
		m.setHealth(0);
		assertEquals(false, m.isAlive());
		m.setHealth(100);
		assertEquals(true, m.isAlive());
	}

	@org.junit.Test
	public void testCheckItems() {
		Player player = new Player();
		player.getTorch().extinguish();
		assertEquals(player.getKeyring().size(),0);
		Room room = new Room(1);
		room.setKey(new Key(1));
		room.setHasPotion(true);
		room.setHasTorch(true);
		room.act(player);
		assertTrue(player.getKeyring().size()>0);
		assertEquals(player.getSecours().size(),2);
		assertEquals(player.getTorch().getFire(),20);
	}


	@org.junit.Test(expected = CorruptedFileException.class)
	public void testCorruptedFileExceptions() throws CorruptedFileException {
		GenerateFromFile.getAllQuestions("testCorruptedQuestionFile.txt");
	}
}
