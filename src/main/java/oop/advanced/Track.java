package oop.advanced;

public class Track implements Obstacle {
    private final int distance;

    public Track(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean goThrough(Participant participant) {
        return participant.run(this);
    }

    @Override
    public String toString() {
        return "the track with distance " + distance;
    }
}
