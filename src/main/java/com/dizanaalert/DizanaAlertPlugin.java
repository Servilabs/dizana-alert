package com.dizanaalert;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Dizana Alert",
		description = "Alerts players if the quiver slot is empty, only if a quiver is owned",
		tags = {"quiver", "empty", "alert", "ammo"}
)
public class DizanaAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private DizanaAlertOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		clientThread.invokeLater(this::checkQuiver);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// Tracks internal ammo changes
		if (event.getVarpId() == VarPlayer.DIZANAS_QUIVER_ITEM_COUNT ||
				event.getVarpId() == VarPlayer.DIZANAS_QUIVER_ITEM_ID)
		{
			clientThread.invokeLater(this::checkQuiver);
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		// Tracks if we just added/removed the quiver from Inventory or Equipment
		if (event.getContainerId() == InventoryID.INVENTORY.getId() ||
				event.getContainerId() == InventoryID.EQUIPMENT.getId())
		{
			clientThread.invokeLater(this::checkQuiver);
		}
	}

	private void checkQuiver()
	{
		// 1. Check if the player owns the quiver (Inventory or Equipped)
		if (!hasQuiverInPossession())
		{
			overlayManager.remove(overlay);
			return;
		}

		// 2. Check internal ammo state
		final int quiverAmmoId = client.getVarpValue(VarPlayer.DIZANAS_QUIVER_ITEM_ID);
		final int quiverAmmoCount = client.getVarpValue(VarPlayer.DIZANAS_QUIVER_ITEM_COUNT);

		if (quiverAmmoId == -1 || quiverAmmoCount == 0)
		{
			overlayManager.add(overlay);
		}
		else
		{
			overlayManager.remove(overlay);
		}
	}

	private boolean hasQuiverInPossession()
	{
		return isQuiverIn(client.getItemContainer(InventoryID.INVENTORY)) ||
				isQuiverIn(client.getItemContainer(InventoryID.EQUIPMENT));
	}

	private boolean isQuiverIn(ItemContainer container)
	{
		if (container == null) return false;

		for (Item item : container.getItems())
		{
			if (item.getId() <= 0) continue;

			String name = itemManager.getItemComposition(item.getId()).getName().toLowerCase();
			if (name.contains("dizana"))
			{
				return true;
			}
		}
		return false;
	}

	@Provides
	DizanaAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DizanaAlertConfig.class);
	}
}