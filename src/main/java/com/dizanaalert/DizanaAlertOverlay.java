package com.dizanaalert;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;


public class DizanaAlertOverlay extends OverlayPanel{

    private final DizanaAlertConfig config;
    private final Client client;

    @Inject
    private DizanaAlertOverlay( DizanaAlertConfig config, Client client)
    {
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add((LineComponent.builder())
                .left("QUIVER SLOT EMPTY")
                .build());
        panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("QUIVER SLOT EMPTY") + 10, 0));
        panelComponent.setBackgroundColor(config.Color());
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        return panelComponent.render(graphics);
    }
}
