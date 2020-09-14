package oop.advanced;

public class Wall implements Obstacle {
    private final int height;

    public Wall(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean goThrough(Participant participant) {
        return participant.jump(this);
    }

    @Override
    public String toString() {
        return "the wall with height " + height;
    }
}
