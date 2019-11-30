package model;
import java.util.ArrayList;
import java.util.Map;

import items.Torch;
import rooms.Room;

import items.weapons.Fist;
import items.HealPotion;
import items.Key;
import items.weapons.Weapon;
import monsters.Monster;


public class Player {
	
	public static int default_health = 100;
	
	private int health;
	private Torch t = new Torch();
	private Weapon wp = new Fist();
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
		int alea = (int) ((Math.random()*21) * this.getWeapon().getPower() / 100);
		if(Math.random()*101 <= 50) {
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
			if(r.getNumero() == key.ROOM_NUMBER)
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

	public void setCurrentRoom(Room r){
		if(currentRoom != null)
			setPreviousRoom(currentRoom);
		currentRoom = r;
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
		return wp;
	}

	public void setWeapon(Weapon wp) {
		this.wp = wp;
	}
	
	public Torch getTorch() {
		return t;
	}

	public void setT(Torch t) {
		this.t = t;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}

	public void useTorch() {
		if(t.empty()){
			System.out.println("Your torch is extinguished");
		} else {
			t.use();
			printDirections();
		}
	}

	private void printDirections(){
		String possibleDirections = "";
		for(Map.Entry<Direction, Room> entry : currentRoom.neighbors.entrySet()) {
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
		System.out.println("Current weapon : "+wp.getName()+" ("+wp.getPower()+" power)");
	}

}
