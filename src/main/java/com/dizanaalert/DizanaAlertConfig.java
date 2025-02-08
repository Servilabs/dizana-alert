package com.dizanaalert;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Units;

@ConfigGroup("dizanaalert")
public interface DizanaAlertConfig extends Config
{

	@Alpha
	@ConfigItem(
			keyName = "Color",
			name = "Notificatin Color",
			description = "The background color for the notification.",
			position = 6
	)
	default Color Color() { return new Color(255, 0, 0, 150); }

}