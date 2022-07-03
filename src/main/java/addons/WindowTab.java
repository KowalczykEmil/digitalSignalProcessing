package addons;

import javafx.scene.Node;
import javafx.scene.control.Tab;

public class WindowTab extends Tab {

    public WindowTab(String text, Node content, boolean isClosable) {
        super(text, content);
        super.setClosable(isClosable);
    }
}
