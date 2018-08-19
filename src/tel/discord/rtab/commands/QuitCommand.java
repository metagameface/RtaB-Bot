package tel.discord.rtab.commands;

import tel.discord.rtab.GameController;
import tel.discord.rtab.RaceToABillionBot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class QuitCommand extends Command {
	public QuitCommand()
	{
		this.name = "quit";
		this.aliases = new String[]{"leave","out"};
		this.help = "leaves the game, if it hasn't started yet";
	}
	@Override
	protected void execute(CommandEvent event) {
		for(GameController game : RaceToABillionBot.game)
		{
			if(game.channel == event.getChannel())
			{
				game.removePlayer(event.getMember());
				//We found the right channel, so
				return;
			}
		}
	}
}