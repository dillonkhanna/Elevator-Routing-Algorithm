package cse374ElevatorSystem;

import java.util.*;

public class RunElevator {
    public static Queue<ExternalRequest> newRequests = new 
        LinkedList<ExternalRequest>();
    public static PriorityQueue<ExternalRequest> reqUp = new 
        PriorityQueue<ExternalRequest>(30, new UpComparator());
    public static PriorityQueue<ExternalRequest> reqDown = new
        PriorityQueue<ExternalRequest>(30, new DownComparator());
    public static PriorityQueue<InternalRequest> scheduleUp = new
        PriorityQueue<InternalRequest>(30, new scheduleUpComparator());
    public static PriorityQueue<InternalRequest> scheduleDown = new
        PriorityQueue<InternalRequest>(30, new scheduleDownComparator());

    public static Elevator elevator = new Elevator(0000, 1, "IDLE", new Direction(true));

    public static Scanner in = new Scanner(System.in);

    public static void checkState() {
        System.out.println("Elevator is idle. ");
        while(elevator.isIdle()) {
            sortRequests();
            if(!reqUp.isEmpty()) {
                serviceUp();
            } else if(!reqDown.isEmpty()) {
                serviceDown();
            } else {
                continue;
            }
        }
    }

    public static void sortRequests() {
        if(elevator.isIdle()) {
            while(!newRequests.isEmpty()) {
                ExternalRequest req = newRequests.poll();
                if(req.getDirection().isUp()) {
                    reqUp.offer(req);
                }
                if(!req.getDirection().isUp()) {
                    reqDown.offer(req);
                }
            }
        }
        if(elevator.isMoving()) {
            int counter = 0;
            while(!newRequests.isEmpty()) {
                counter++;
                ExternalRequest req = newRequests.poll();
                Direction direc = elevator.getDirection();
                if(req.getDirection().isUp() == direc.isUp()) {
                    if(req.getDirection().isUp()) {
                        if(elevator.currFloor < req.OriginFloor) {
                            reqUp.offer(req);
                        } else {
                            newRequests.offer(req);
                        }
                    }
                    if(!req.getDirection().isUp()) {
                        if(elevator.currFloor > req.OriginFloor) {
                            reqDown.offer(req); 
                        } else {
                            newRequests.offer(req);
                        }
                    }
                }
                if(!(req.getDirection().isUp() == direc.isUp())) {
                    if(req.getDirection().isUp()) {
                        reqUp.offer(req);
                    }
                    if(!req.getDirection().isUp()) {
                        reqDown.offer(req);
                    }
                }
                if(counter > 40) {
                    break;
                }
            }
        }
    }

    public static void serviceUp() {
        elevator.setState("MOVING");
        boolean checker = true;
        while(!reqUp.isEmpty() || !scheduleUp.isEmpty()) {
            checker = true;
            if(scheduleUp.isEmpty()) {
                elevator.moveTo(reqUp.peek().getOriginFloor());
                System.out.println("A request in the up direction has been picked up. ");
                System.out.print("Please enter the destination floor: ");
                scheduleUp.offer(new InternalRequest(Integer.parseInt(in.next())));
                int counter = 0;
                while(checker) {
                    counter++;
                    if(counter > 20) {
                        checker = false;
                    }
                    if(!reqUp.isEmpty()) {
                        if(reqUp.peek().getOriginFloor() == elevator.currFloor) {
                            reqUp.poll();
                        }
                    } else if(!scheduleUp.isEmpty()) {
                        if(scheduleUp.peek().getDestinationFloor() == elevator.currFloor) {
                            System.out.println("A request was dropped off at " + elevator.currFloor);
                            scheduleUp.poll();
                        }
                    } else {
                        checker = false;
                    }
               }
            } else if(!reqUp.isEmpty()) {
                if(reqUp.peek().getOriginFloor() <= scheduleUp.peek().getDestinationFloor()) {
                    elevator.moveTo(reqUp.peek().getOriginFloor());
                    System.out.println("A request in the up direction has been picked up. ");
                    System.out.print("Please enter the destination floor: ");
                    scheduleUp.offer(new InternalRequest(Integer.parseInt(in.next())));
                }
                if(reqUp.peek().getOriginFloor() > scheduleUp.peek().getDestinationFloor()) {
                    elevator.moveTo(scheduleUp.peek().getDestinationFloor());
                }
        
                sortRequests();
                int counter = 0;
                while(checker) {
                    counter++;
                    if(counter > 20) {
                        checker = false;
                    }
                    
                    if(!reqUp.isEmpty()) {
                        if(reqUp.peek().getOriginFloor() == elevator.currFloor) {
                            reqUp.poll();
                        }
                    }
                    if(!scheduleUp.isEmpty()) {
                        if(scheduleUp.peek().getDestinationFloor() == elevator.currFloor) {
                            System.out.println("A request was dropped off at " + elevator.currFloor);
                            scheduleUp.poll();
                        }
                    } else {
                        checker = false;
                    }
                
               }
            } else {
                while(!scheduleUp.isEmpty()) {
                    elevator.moveTo(scheduleUp.poll().getDestinationFloor());
                    System.out.println("A request was dropped off at " + elevator.currFloor);
                }
            }
            if(scheduleUp.isEmpty() && reqUp.isEmpty()) {
                if(!reqDown.isEmpty()) {
                    serviceDown();
                } else {
                    elevator.setState("IDLE");
                    checkState();
                }
            }
        
        }

    }

    public static void serviceDown() {
        elevator.setState("MOVING");
        boolean checker = true;
        while((!reqDown.isEmpty()) || !scheduleDown.isEmpty()) {
            checker = true;
            if(scheduleDown.isEmpty()) {
                elevator.moveTo(reqDown.peek().getOriginFloor());
                System.out.println("A request in the down direction has been picked up. ");
                System.out.print("Please enter the destination floor: ");
                scheduleDown.offer(new InternalRequest(Integer.parseInt(in.next())));
                int counter = 0;
                while(checker) {
                    counter++;
                    if(counter > 20) {
                        checker = false;
                    }
                    if(!reqDown.isEmpty()) {
                        if(reqDown.peek().getOriginFloor() == elevator.currFloor) {
                            reqDown.poll();
                        }
                    } 
                    if(!scheduleDown.isEmpty()) {
                        if(scheduleDown.peek().getDestinationFloor() == elevator.currFloor) {
                            System.out.println("A request was dropped off at " + elevator.currFloor);
                            scheduleDown.poll();
                        }
                    } else {
                        checker = false;
                    }
                }
            } else if(!reqDown.isEmpty()) {
                if(reqDown.peek().getOriginFloor() >= scheduleDown.peek().getDestinationFloor()) {
                    elevator.moveTo(reqDown.peek().getOriginFloor());
                    System.out.println("A request in the down direction has been picked up. ");
                    System.out.print("Please enter the destination floor: ");
                    scheduleDown.offer(new InternalRequest(Integer.parseInt(in.next())));
                }
                if(reqDown.peek().getOriginFloor() < scheduleDown.peek().getDestinationFloor()) {
                    elevator.moveTo(scheduleDown.peek().getDestinationFloor());
                }
        
                sortRequests();
                int counter = 0;
                while(checker) {
                    counter++;
                    if(counter > 20) {
                        checker = false;
                    }
                    if(!reqDown.isEmpty()) {
                        if(reqDown.peek().getOriginFloor() == elevator.currFloor) {
                            reqDown.poll();
                        }
                    }
                    if(!scheduleDown.isEmpty()) {
                        if(scheduleDown.peek().getDestinationFloor() == elevator.currFloor) {
                            System.out.println("A request was dropped off at " + elevator.currFloor);
                            scheduleDown.poll();
                        }
                    } else {
                        checker = false;
                    }
               }
            } else {
                while(!scheduleDown.isEmpty()) {
                    elevator.moveTo(scheduleDown.poll().getDestinationFloor());
                    System.out.println("A request was dropped off at " + elevator.currFloor);
                }
            }
            if(scheduleDown.isEmpty() && reqDown.isEmpty()) {
                if(!reqUp.isEmpty()) {
                    serviceUp();
                } else {
                    elevator.setState("IDLE");
                    checkState();
                }
            }
        
        }

    }

    static class UpComparator implements Comparator<ExternalRequest> {
        public int compare(ExternalRequest e1, ExternalRequest e2) {
            if(e1.getOriginFloor() > e2.getOriginFloor()) {
                return 1;
            } else if(e1.getOriginFloor() < e2.getOriginFloor()) {
                return -1;
            }
            return 0;
        }
    }

    static class DownComparator implements Comparator<ExternalRequest> {
        public int compare(ExternalRequest e1, ExternalRequest e2) {
            if(e1.getOriginFloor() < e2.getOriginFloor()) {
                return 1;
            } else if(e1.getOriginFloor() > e2.getOriginFloor()) {
                return -1;
            }
            return 0;
        }
    }

    static class scheduleUpComparator implements Comparator<InternalRequest> {
        public int compare(InternalRequest i1, InternalRequest i2) {
            if(i1.getDestinationFloor() > i2.getDestinationFloor()) {
                return 1;
            } else if(i1.getDestinationFloor() < i2.getDestinationFloor()) {
                return -1;
            }
            return 0;
        }
    }

    static class scheduleDownComparator implements Comparator<InternalRequest> {
        public int compare(InternalRequest i1, InternalRequest i2) {
            if(i1.getDestinationFloor() < i2.getDestinationFloor()) {
                return 1;
            } else if(i1.getDestinationFloor() > i2.getDestinationFloor()) {
                return -1;
            }
            return 0;
        }
    }


    public static void main(String[] args) {
        newRequests.add(new ExternalRequest(2, new Direction(true)));
        newRequests.add(new ExternalRequest(19, new Direction(true)));
        newRequests.add(new ExternalRequest(4, new Direction(true)));
        newRequests.add(new ExternalRequest(5, new Direction(true)));
        newRequests.add(new ExternalRequest(14, new Direction(true)));
        newRequests.add(new ExternalRequest(3, new Direction(true)));
        newRequests.add(new ExternalRequest(3, new Direction(false)));
        newRequests.add(new ExternalRequest(5, new Direction(false)));
        newRequests.add(new ExternalRequest(12, new Direction(false)));
        newRequests.add(new ExternalRequest(11, new Direction(false)));
        newRequests.add(new ExternalRequest(5, new Direction(false)));
        newRequests.add(new ExternalRequest(13, new Direction(false)));


        checkState();
    }

}
