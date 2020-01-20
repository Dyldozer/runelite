package net.runelite.client.plugins.speedruntimer.tob;

import lombok.Getter;

public class MaidenHandler
{
	int current;
	@Getter
	String[] rows;

	public MaidenHandler()
	{
		this.rows = new String[]
				{
						"70%:",
						"50%:",
						"30%:",
						"Killed:",
				};
		this.current = 0;
	}
}
