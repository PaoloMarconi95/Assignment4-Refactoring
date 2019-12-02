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
	private Torch torch = new Torch();
	private Weapon weapon = new Fist();
	private ArrayList<HealPotion> support = new ArrayList<>();
	private ArrayList<Key> keyring = new ArrayList<>();

	private Room currentRoom;
	private Room previousRoom;


	public Player(){
		this.health = default_health;
		this.support.add(new HealPotion());
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
		if(health < 100)
			return false;
		else
			return true;
	}

	private boolean hasHealPotions(){
		if(support.isEmpty())
			return false;
		else
			return true;
	}


	public boolean hasKeyForRoom(Room r){
		for(Key key : keyring){
			if(r.getNumber() == key.ROOM_NUMBER)
				return true;
		}
		return false;
	}

	public void addkey(Key k){
		keyring.add(k);
	}

	public boolean canGetInRoom(Room r){
		if(!r.isLocked())
			return true;
		else if(hasKeyForRoom(r))
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

	public void setWeapon(Weapon wp) {
		this.weapon = wp;
	}
	
	public Torch getTorch() {
		return torch;
	}

	public void setT(Torch t) {
		this.torch = t;
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
		System.out.println("Health potion : "+support.size());
		System.out.println("Keyring : "+keyring.size()+" key");
		for(Key k : keyring){
			System.out.println("Key n."+k.ROOM_NUMBER);
		}
		System.out.println("Current weapon : "+weapon.getName()+" ("+weapon.getPower()+" power)");
	}

}
