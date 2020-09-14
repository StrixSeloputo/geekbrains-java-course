package oop.advanced;

import java.util.Random;

public class Race {
    // Создайте два массива: с участниками и препятствиями, и заставьте всех участников пройти этот набор препятствий.
    // Если участник не смог пройти одно из препятствий, то дальше по списку препятствий он не идет.
    private Participant[] participants;
    private Obstacle[] obstacles;

    public Race(int participantsCount, int obstaclesCount) {
        this.participants = new Participant[participantsCount];
        this.obstacles = new Obstacle[obstaclesCount];
    }

    public static void main(String[] args) {
        Race race = new Race(5, 10);
        race.fillRandom(15, 15);
        race.start();
    }

    public void start() {
        int i = 1;
        for (Obstacle obstacle : obstacles) {
            System.out.println("== Round #" + (i++) + " ==");
            for (Participant participant : participants) {
                if (participant.isOnRace()) {
                    obstacle.goThrough(participant);
                }
            }
        }
    }

    void fillRandom(int p, int o) {
        Random random = new Random();
        for (int i = 0; i < participants.length; i++) {
            if (random.nextInt() % 2 == 0) {
                participants[i] = new Human(Math.abs(random.nextInt()) % p + 1, Math.abs(random.nextInt()) % p + 1);
            } else {
                participants[i] = new Cat(Math.abs(random.nextInt()) % p + 1, Math.abs(random.nextInt()) % p + 1);
            }
        }
        for (int i = 0; i < obstacles.length; i++) {
            if (random.nextInt() % 2 == 0) {
                obstacles[i] = new Track(Math.abs(random.nextInt()) % o + 1);
            } else {
                obstacles[i] = new Wall(Math.abs(random.nextInt()) % o + 1);
            }
        }
    }
}
