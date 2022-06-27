package project.cyberproton.atom.gui;

import project.cyberproton.atom.Log;
import project.cyberproton.atom.gui.element.Element;
import project.cyberproton.atom.gui.element.Node;
import project.cyberproton.atom.gui.pane.Pane;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.util.Position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class Pagination implements Pane {
    private final UUID uniqueId = UUID.randomUUID();
    private final int width;
    private final int height;
    private final int currentPage;
    private final int maxPages;
    private final List<Element> elements;
    private final ItemStack background;

    Pagination(int width, int height, int currentPage, int maxPages, List<Element> elements, ItemStack background) {
        this.width = width;
        this.height = height;
        this.currentPage = currentPage;
        this.maxPages = maxPages;
        this.elements = elements;
        this.background = background;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @NotNull
    public static Builder builder() {
        return Builder.create();
    }

    @Nullable
    @Override
    public Map<Position, Element> onBuild() {
        int slotPerPage = width * height;
        int startIdx = slotPerPage * currentPage;
        int endIdx = startIdx + slotPerPage - 1;
        if (startIdx >= elements.size()) {
            if (background == null) {
                return new HashMap<>();
            }
            return Utils.fill(Node.of(background), Position.of(0, 0), Position.of(width, height));
        }
        Map<Position, Element> res = new HashMap<>();
        int currentIdx = startIdx;
        boolean shouldBreak = false;
        for (int i = 0; i < height; i++) {
            if (shouldBreak) {
                break;
            }
            for (int j = 0; j < width; j++) {
                if (currentIdx >= elements.size()) {
                    if (background == null) {
                        shouldBreak = true;
                        break;
                    }
                    res.put(Position.of(j, i), Node.of(background));
                    continue;
                }
                Element item = elements.get(currentIdx);
                res.put(Position.of(j, i), item);
                currentIdx++;
            }
        }
        return res;
    }

    public static final class Builder {
        private int width;
        private int height;
        private int currentPage;
        private int maxPages = -1;
        private List<Element> elements;
        private ItemStack background;

        private Builder() {
        }

        @NotNull
        public static Builder create() {
            return new Builder();
        }

        @NotNull
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        @NotNull
        public Builder height(int height) {
            this.height = height;
            return this;
        }

        @NotNull
        public Builder currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        @NotNull
        public Builder maxPages(int maxPages) {
            this.maxPages = maxPages;
            return this;
        }

        @NotNull
        public Builder elements(@Nullable List<Element> elements) {
            this.elements = elements;
            return this;
        }

        @NotNull
        public Builder background(@Nullable ItemStack background) {
            this.background = background;
            return this;
        }

        @NotNull
        public Pagination build() {
            if (width < 0) {
                throw new IllegalArgumentException("Width must not lower than 0");
            }
            if (height < 0) {
                throw new IllegalArgumentException("Height must not lower than 0");
            }
            if (maxPages < 0) {
                maxPages = currentPage + 1;
            }
            if (maxPages < 1) {
                throw new IllegalArgumentException("Max pages must not lower than 1");
            }
            if (currentPage > maxPages) {
                currentPage = maxPages - 1;
            }
            if (currentPage < 0) {
                currentPage = 0;
            }
            return new Pagination(width, height, currentPage, maxPages, elements == null ? new ArrayList<>() : elements, background);
        }
    }
}
