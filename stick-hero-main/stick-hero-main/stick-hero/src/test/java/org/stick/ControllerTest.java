package org.stick;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    @Test
    public void testGeneratePillar() {
        Controller controller = new Controller();
        AnchorPane rootPane = new AnchorPane();
        controller.setRootPane(rootPane);
        Rectangle pillar = controller.generatePillar();
        assertNotNull(pillar);
    }
}
