package project.cyberproton.atom.util;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() {
        return this.x;
    }

    public Position withX(int x) {
        return new Position(x, y());
    }

    public Position withY(int y) {
        return new Position(x(), y);
    }

    public int y() {
        return this.y;
    }

    public Position add(final Position v) {
        return this.add(v.x, v.y);
    }

    public Position add(final int x, final int y) {
        return new Position(this.x + x, this.y + y);
    }

    public Position sub(final Position v) {
        return this.sub(v.x, v.y);
    }

    public Position sub(final int x, final int y) {
        return new Position(this.x - x, this.y - y);
    }

    public Position mul(final int a) {
        return this.mul(a, a);
    }

    public Position mul(final Position v) {
        return this.mul(v.x, v.y);
    }

    public Position mul(final int x, final int y) {
        return new Position(this.x * x, this.y * y);
    }

    public Position div(final Position v) {
        return this.div(v.x, v.y);
    }

    public Position div(final int x, final int y) {
        return new Position(this.x / x, this.y / y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }

    public static Position zero() {
        return new Position(0, 0);
    }

    public static Position of(int i) {
        return new Position(i, i);
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }
}
