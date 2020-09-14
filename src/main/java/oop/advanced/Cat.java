package oop.advanced;

public class Cat implements Participant {
    private static int count;
    private final int maxHeight;
    private final int maxDistance;
    private final int id;
    private boolean isOnRace;

    public Cat(int maxHeight, int maxDistance) {
        this.maxHeight = maxHeight;
        this.maxDistance = maxDistance;
        this.isOnRace = true;
        this.id = ++count;
    }


    @Override
    public boolean isOnRace() {
        return isOnRace;
    }

    @Override
    public boolean run(Track track) {
        if (maxDistance < track.getDistance()) {
            System.out.println("" + this + " can't run " + track);
            return isOnRace = false;
        } else {
            System.out.println("" + this + " runs " + track);
            return true;
        }
    }

    @Override
    public boolean jump(Wall wall) {
        if (maxHeight < wall.getHeight()) {
            System.out.println("" + this + " can't jump " + wall);
            return isOnRace = false;
        } else {
            System.out.println("" + this + " jumps " + wall);
            return true;
        }
    }

    @Override
    public String toString() {
        return "Cat #" + id;
    }
}
