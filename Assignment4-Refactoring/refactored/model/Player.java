package model;
import java.util.ArrayList;
import java.util.Map;

import items.Torch;

import items.weapons.Fist;
import items.HealPotion;
import items.Key;
import items.weapons.Weapon;
import monsters.Monster;
import rooms.Room;


public class Player {
	
	public static final int default_health = 100;
	
	private int health;
	private Torch torch;
	private Weapon weapon;
	private ArrayList<HealPotion> support = new ArrayList<>();
	private ArrayList<Key> keyring = new ArrayList<>();

	private Room currentRoom;
	private Room previousRoom;


	public Player(){
		this.health = default_health;
		this.support.add(new HealPotion());
		this.torch = new Torch();
		this.weapon = new Fist();
	}

	public int hit(Monster monster){
		int damage = this.getWeapon().getPower();
		int alea = Randomizer.random.nextInt(21) * this.getWeapon().getPower() / 100;
		if(Randomizer.random.nextInt(101) <= 50) {
			alea = -alea;
		}
		monster.setHealth(monster.getHealth() - (damage + alea) );
		return damage;
	}

	public void useHealthPotion(){
		if(hasHealPotions()){
			HealPotion potion = support.get(0);
			if(!fullHealth()) {
				if(health + potion.getValue() > 100)
					setHealth(100);
				else
					setHealth(health + potion.getValue());
				System.out.println("You used a health potion and restored your health");
				System.out.println("Health : " + health);
				support.remove(0);
			}
			else
				System.out.println("Impossible action : your life is full !");
		}
		else{
			System.out.println("You don't have any heal potion left");
		}
	}

	private boolean fullHealth(){
		return health >= 100;
	}

	private boolean hasHealPotions(){
		return support.isEmpty();
	}


	public boolean hasKeyForRoom(Room room){
		for(Key key : keyring){
			if(room.getNumber() == key.ROOM_NUMBER)
				return true;
		}
		return false;
	}

	public void addkey(Key key){
		keyring.add(key);
	}

	public boolean canGetInRoom(Room room){
		if(!room.isLocked())
			return true;
		else if(hasKeyForRoom(room))
			return true;
		return false;
	}


	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room actual_room){
		if(currentRoom != null)
			setPreviousRoom(currentRoom);
		currentRoom = actual_room;
	}

	public Room getPreviousRoom() {
		return previousRoom;
	}

	public void setPreviousRoom(Room previousRoom) {
		this.previousRoom = previousRoom;
	}

	public ArrayList<Key> getKeyring() {
		return keyring;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public Torch getTorch() {
		return torch;
	}

	public void setT(Torch torch) {
		this.torch = torch;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}

	public void useTorch() {
		if(torch.empty()){
			System.out.println("Your torch is extinguished");
		} else {
			torch.use();
			printDirections();
		}
	}

	private void printDirections(){
		String possibleDirections = "";
		for(Map.Entry<Direction, Room> entry : currentRoom.getNeighbors().entrySet()) {
			Direction direction = entry.getKey();
			possibleDirections += direction +"\n";
		}
		System.out.println(possibleDirections);

	}

	public ArrayList<HealPotion> getSecours() {
		return support;
	}

	public void displayInventory() {
		System.out.println("Health potion : " + support.size());
		System.out.println("Keyring : " + keyring.size() + " key");
		for(Key key : keyring){
			System.out.println("Key n." + key.ROOM_NUMBER);
		}
		System.out.println("Current weapon : "+ weapon.getName()+ " ("+ weapon.getPower()+ " power)");
	}

}
