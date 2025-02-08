package com.dizanaalert;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.game.ItemManager;
import net.runelite.api.events.VarbitChanged;

@Slf4j
@PluginDescriptor(
		name = "Dizana Alert",
		description = "Alerts players if the quiver slot is empty",
		tags = {"quiver","empty","alert","ammo"}
)
public class DizanaAlertPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private DizanaAlertConfig config;

	@Inject
	private DizanaAlertOverlay overlay;


	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}


	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayer.DIZANAS_QUIVER_ITEM_COUNT || event.getVarpId() == VarPlayer.DIZANAS_QUIVER_ITEM_ID)
		{
			checkQuiver();
		}
	}




	private void checkQuiver()
	{


		final int quiverAmmoId = client.getVarpValue(VarPlayer.DIZANAS_QUIVER_ITEM_ID);
		final int quiverAmmoCount = client.getVarpValue(VarPlayer.DIZANAS_QUIVER_ITEM_COUNT);
		if (quiverAmmoId == -1 || quiverAmmoCount == 0)
		{
			overlayManager.add(overlay);
		}
		else {
			overlayManager.remove(overlay);
		}

	}

	@Provides
	DizanaAlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DizanaAlertConfig.class);
	}

}