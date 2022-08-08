package model.identifier;

public class Identifier {
    private static int identifier;

    public static int getId() {
        return identifier;
    }

    public static int getNextId() {
        if (identifier == Integer.MAX_VALUE - 1) {
            return Integer.MAX_VALUE - 1;
        }
        return ++identifier;
    }

    public static int getLastAndMoveId() {
        if (identifier == 0) {
            return 0;
        }
        return identifier--;
    }

    public static int getMoveLastId() {
        if (identifier == 0) {
            return 0;
        }
        return --identifier;
    }
}
