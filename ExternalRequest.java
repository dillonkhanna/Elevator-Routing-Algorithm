package cse374ElevatorSystem;


public class ExternalRequest {

	int OriginFloor;
	Direction direction;
	
	public ExternalRequest(int OriginFloor, Direction direction) {
		this.OriginFloor = OriginFloor;
		this.direction = direction;
	}

	public int getOriginFloor() {
		return OriginFloor;
	}

	public void setOriginFloor(int originFloor) {
		OriginFloor = originFloor;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
	
}

