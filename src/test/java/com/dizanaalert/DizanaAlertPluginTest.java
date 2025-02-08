package com.dizana-alert;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

@Slf4j
@PluginDescriptor(
		name = "Spec Jingle"
)
public class DizanaAlertPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private DizanaAlertConfig config;

	private int specialAttack = 100;
	private boolean unknownSpecial = true;

	public ArrayList<String> thresholdValues = new ArrayList<>();
	public ArrayList<String> ethresholdValues = new ArrayList<>();

	@Override
	protected void startUp() throws Exception
	{

	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (!event.getGroup().equals("specjingle"))
		{
			return;
		}
		thresholdValues.clear();
		String temp = config.thresholdList();
		if( config.selectedPreset() == 2) temp = config.thresholdList2();
		if( config.selectedPreset() == 3) temp = config.thresholdList3();
		if (!temp.equals(""))
		{
			for (String str : temp.split(","))
			{
				if (!str.trim().equals("")) { thresholdValues.add(str.trim());}
			}
		}
		ethresholdValues.clear();
		temp = config.ethresholdList();
		if( config.selectedPreset() == 2) temp = config.ethresholdList2();
		if( config.selectedPreset() == 3) temp = config.ethresholdList3();
		if (!temp.equals(""))
		{
			for (String str : temp.split(","))
			{
				if (!str.trim().equals("")) { ethresholdValues.add(str.trim());}
			}
		}
	}


	@Override
	protected void shutDown() throws Exception
	{
		thresholdValues.clear();
		ethresholdValues.clear();
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged) {
		if( unknownSpecial == true){
			specialAttack = varbitChanged.getValue();
			unknownSpecial = false;
			return;
		}

		if (varbitChanged.getVarpId() != VarPlayer.SPECIAL_ATTACK_PERCENT) {
			return;
		}

		// the previous code checks we are working with a special attack change

		int newSpecialAttack = varbitChanged.getValue();

		boolean playCheck = false;
		int sound = 1;

		for (String entry : thresholdValues){
			int value = -1;
			int soundValue = 1;
			if (entry.contains(":"))
			{
				String[] strArr = entry.split(":");
				if (StringUtils.isNumeric(strArr[0]))
				{
					value = Integer.parseInt(strArr[0]);
				}
				if (StringUtils.isNumeric(strArr[1])) {
					soundValue = Integer.parseInt(strArr[1]);
				}
			}
			else if (StringUtils.isNumeric(entry))
			{
				value = Integer.parseInt(entry);
			}
			if( newSpecialAttack >= value*10 && specialAttack < value*10 ){
				playCheck = true;
				sound = soundValue;
			}
		}

		for (String entry : ethresholdValues){
			int value = -1;
			int soundValue = 1;
			if (entry.contains(":"))
			{
				String[] strArr = entry.split(":");
				if (StringUtils.isNumeric(strArr[0]))
				{
					value = Integer.parseInt(strArr[0]);
				}
				if (StringUtils.isNumeric(strArr[1])) {
					soundValue = Integer.parseInt(strArr[1]);
				}
			}
			else if (StringUtils.isNumeric(entry))
			{
				value = Integer.parseInt(entry);
			}
			if( newSpecialAttack == value*10 && specialAttack < value*10 ){
				playCheck = true;
				sound = soundValue;
			}
		}


		if (playCheck) {
			if( sound == 1) client.playSoundEffect(3924,this.config.volume());
			if( sound == 2) client.playSoundEffect(97,this.config.volume());
			if( sound == 3) client.playSoundEffect(4046,this.config.volume());
			if( sound == 4) client.playSoundEffect(4152,this.config.volume());
			if( sound == 5) client.playSoundEffect(147,this.config.volume());
			if( sound == 6) client.playSoundEffect(202,this.config.volume());
			if( sound == 7) client.playSoundEffect(203,this.config.volume());
			if( sound == 8) client.playSoundEffect(984,this.config.volume());
			if( sound == 9) client.playSoundEffect(115,this.config.volume());
			if( sound >= 10 ) client.playSoundEffect(sound,this.config.volume());

		}

		specialAttack = newSpecialAttack;
	}



	@Provides
	SpecJingleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpecJingleConfig.class);
	}
}