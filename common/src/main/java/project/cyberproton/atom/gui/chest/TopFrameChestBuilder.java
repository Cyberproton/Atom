package project.cyberproton.atom.gui.chest;

import project.cyberproton.atom.gui.AbstractFrameBuilder;
import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.util.Position;

public class TopFrameChestBuilder extends AbstractFrameBuilder {
    private String title;
    private int row;

    private TopFrameChestBuilder() {
    }

    public TopFrameChestBuilder title(String title) {
        this.title = title;
        return this;
    }

    public String title() {
        return title;
    }

    public TopFrameChestBuilder row(int row) {
        validateRow(row);
        this.row = row;
        return this;
    }

    public int row() {
        return row;
    }

    public TopFrameChestBuilder fill(Element element) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 9; j++) {
                slot(j, i).element(element);
            }
        }
        return this;
    }

    public TopFrameChestBuilder fill(Element element, Position from, Position to) {
        int fromX = Math.min(from.x(), to.x());
        int toX = Math.max(from.x(), to.x());
        int fromY = Math.min(from.y(), to.y());
        int toY = Math.max(from.y(), to.y());
        for (int i = fromY; i <= toY; i++) {
            for (int j = fromX; j <= toX; j++) {
                slot(j, i).element(element);
            }
        }
        return this;
    }

    public TopFrameChestBuilder border(Element element, int size) {
        for (int i = 0; i < row; i++) {
            if (i == 0 || i == row - 1) {
                for (int j = 0; j < 9; j++) {
                    slot(i, j).element(element);
                }
                break;
            }
            slot(i, 0).element(element);
            slot(i, 8).element(element);
        }
        return this;
    }

    public void validateRow(int row) {
        if (row <= 0 || row > 6) {
            throw new IllegalArgumentException("Row must higher than 0 and lower than 7, current row = " + row);
        }
    }

    public static TopFrameChestBuilder create() {
        return new TopFrameChestBuilder();
    }
}
