package project.cyberproton.atom.gui.context;

import project.cyberproton.atom.item.ItemStack;

public class InputContext {
    private final ItemStack current;
    private ItemStack next;
    private final Object event;

    public InputContext(ItemStack current, ItemStack next, Object event) {
        this.current = current;
        this.next = next;
        this.event = event;
    }

    public ItemStack getCurrent() {
        return current;
    }

    public ItemStack getNext() {
        return next;
    }

    public void setNext(ItemStack item) {
        this.next = item;
    }

    public Object getEvent() {
        return event;
    }
}
