package cse374ElevatorSystem;

public class Elevator {

	int ID;
	int currFloor;
	String state;
	Direction currDirection;
	
	public Elevator(int id, int currf, String state, Direction currd) {
		this.ID = id;
		this.currFloor = currf;
		this.state = state;
		this.currDirection = currd;
	}

	public boolean isIdle() {
		if(state.equals("IDLE")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMoving() {
		if(state.equals("MOVING")) {
			return true;
		} else {
			return false;
		}
	}

	public Direction getDirection() {
		return currDirection;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void moveTo(int floor) {
		this.currFloor = floor;
		System.out.println("Elevator has moved to floor: " + floor);
	}
	
}

 
