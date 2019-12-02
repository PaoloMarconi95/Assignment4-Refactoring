package rooms;
import java.util.HashMap;

import model.Direction;
import model.Player;
import items.HealPotion;
import items.Key;


public class Room {

	private final int number;

	private boolean locked = false;
	private boolean isExit = false;
	private boolean isEntrance = false;
	private boolean hasTorch = false;
	private boolean hasPotion = false;
	private Key key = null;

	private HashMap<Direction, Room> neighbors;

	public HashMap<Direction, Room> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(HashMap<Direction, Room> neighbors) {
		this.neighbors = neighbors;
	}

	public Room(int number){
		this.number = number;
		neighbors = new HashMap<>();
	}

	public void act(Player p){
		checkRoom(p);
	}

	/**
	 * Check the exit and all the items in the room
	 * @param p
	 */
	public void checkRoom(Player p){
		displayNum();
		if(isExit())
			System.out.println("Congratulation ! You escaped from the dungeon");
		else 
			checkForItems(p);
	}

	protected void displayNum() {
		System.out.println("Room n."+number);
	}

	public void checkForItems(Player p){
		checkForKey(p);
		checkForTorch(p);
		checkForPotion(p);
	}

	public void checkForKey(Player p){
		if(getKey() != null ){
			System.out.println("You picked up a key ! ("+getKey().ROOM_NUMBER+")");
			p.getKeyring().add(getKey());
			setKey(null);
		}
	}

	public void checkForTorch(Player p){
		if(hasTorch){
			hasTorch = false;
			System.out.println("You picked up a torch !");
			p.getTorch().reload();
		}
	}

	public void checkForPotion(Player p){
		if(hasPotion){
			hasPotion = false;
			System.out.println("You picked up a health potion !");
			p.getSecours().add(new HealPotion());
		}
	}

	public Room getNextRoom(Direction dir){
		Room r = null;
		if(neighbors.containsKey(dir))
			r = neighbors.get(dir);
		return r;
	}

	public boolean alreadyConnected(Room r){
		if(neighbors.containsValue(r))
			return true;
		else
			return false;
	}
	public boolean isANumber(String line) {
		if(line.isEmpty())
			return false;
		for(int i = 0; i < line.length(); i++){
			if(line.charAt(i) < '0' || line.charAt(i) > '9')
				return false;
		}
		return true;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean needKey) {
		this.locked = needKey;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public boolean isEntrance() {
		return isEntrance;
	}

	public void setEntrance(boolean isEntrance) {
		this.isEntrance = isEntrance;
	}
	public boolean hasTorch() {
		return hasTorch;
	}

	public void setHasTorch(boolean hasTorch) {
		this.hasTorch = hasTorch;
	}

	public int getNumber() {
		return number;
	}

	public String toString(){
		String s;
		s="Room : "+number;
		s = checkIfLocked();
        s = checkHasKey();
		System.out.println(s);
		return s;
	}

	private String checkHasKey(){
	    String s = "";
        if(key != null)
            s+=" key n."+key.ROOM_NUMBER;
        else
            s+=" no key";
        return s;
    }

	private String checkIfLocked(){
	    String s = "";
        if(isLocked())
            s+= " LOCKED";
        else
            s+= " UNLOCKED";
        return s;
    }

	public int getNeighborsCount(){
		return neighbors.size();
	}

	public void setHasPotion(boolean hasPotion) {
		this.hasPotion = hasPotion;
	}

}
