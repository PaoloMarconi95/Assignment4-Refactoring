package model;
import java.util.Random;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;
	
	public static Direction chooseDirection(int index) {
		Direction direction;
		switch (index) {
		case 1:
			direction = Direction.NORTH;
			break;
		case 2:
			direction = Direction.EAST;
			break;
		case 3:
			direction = Direction.SOUTH;
			break;
		case 4:
			direction = Direction.WEST;
			break;
		default:
			direction = null;
			break;
		}
		return direction;
	}
	
    public static Direction getRandomDirection() {
    	Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
