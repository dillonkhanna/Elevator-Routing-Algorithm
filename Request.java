package cse374ElevatorSystem;
public class Request {
    ExternalRequest ex;
    InternalRequest in;

    public Request(ExternalRequest ex, InternalRequest in) {
        this.ex = ex;
        this.in = in;
    }
}
