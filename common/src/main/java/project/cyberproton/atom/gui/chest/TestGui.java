package project.cyberproton.atom.gui.chest;

import net.kyori.adventure.text.Component;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.gui.GuiBuilder;
import project.cyberproton.atom.gui.GuiManager;
import project.cyberproton.atom.gui.PlayerInventoryFrameBuilder;
import project.cyberproton.atom.gui.element.Button;
import project.cyberproton.atom.gui.element.Node;
import project.cyberproton.atom.item.ItemStack;
import project.cyberproton.atom.item.ItemType;
import project.cyberproton.atom.state.State;
import project.cyberproton.atom.util.Position;

public class TestGui extends ChestGui {
    private final State<Integer> count = useState(Integer.class, 10);

    public TestGui(GuiManager<?> guiManager) {
        super(guiManager);
    }

    @Override
    public GuiBuilder<TopFrameChestBuilder, PlayerInventoryFrameBuilder> onBuild() {
        return gui(
            topFrame(b -> {
                b.title("Test").row(6);
                b.fill(Node.of(ItemStack.ofDefault()), Position.of(2), Position.of(4));
                b.slot(1, 2).element(
                    Button.builder()
                        .item(ItemStack.builder()
                                  .type(ItemType.EMERALD)
                                  .customName(Component.text("Clicked " + count.getUnboxed() + " times"))
                                  .build())
                        .anyClickHandler(c -> {
                            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Count: " + count.getUnboxed());
                            count.updateSync(count.getUnboxed() + 1);
                        }).build()
                );
            }),
            bottomFrame(c -> {
                c.slot(2, 3).element(Button.builder().build());
            })
        );
    }
}
