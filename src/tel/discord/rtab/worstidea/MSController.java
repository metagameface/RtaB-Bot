package tel.discord.rtab.worstidea;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import tel.discord.rtab.Player;
import tel.discord.rtab.enums.GameStatus;
import tel.discord.rtab.minigames.SuperBonusRound;

public class MSController
{
	final static int MAX_PLAYERS = 16;
	public TextChannel channel;
	TextChannel resultChannel;
	int boardSize;
	public List<Player> players = new ArrayList<>();
	List<Player> winners = new ArrayList<>();
	int currentTurn = -1;
	public int playersJoined = 0;
	int playersAlive = 0;
	public GameStatus gameStatus = GameStatus.SIGNUPS_OPEN;
	boolean[] pickedSpaces;
	int spacesLeft;
	boolean[] bombs;
	int[] gameboard;
	public static EventWaiter waiter;
	public Timer timer = new Timer();
	Message waitingMessage;
	
	//TimerTasks (these will get lambda'd out someday. not today.)
	private class StartGameTask extends TimerTask
	{
		@Override
		public void run()
		{
			startTheGameAlready();
		}
	}
	private class FinalCallTask extends TimerTask
	{
		@Override
		public void run()
		{
			channel.sendMessage("Thirty seconds before game starts!").queue();
			channel.sendMessage(listPlayers(false)).queue();
		}
	}
	private class PickSpaceWarning extends TimerTask
	{
		@Override
		public void run()
		{
			channel.sendMessage(players.get(currentTurn).getSafeMention() + 
					", ten seconds left to choose a space!").queue();
			displayBoardAndStatus(true,false,false);
		}
	}
	private class WaitForNextTurn extends TimerTask
	{
		@Override
		public void run()
		{
			runTurn();
		}
	}
	private class WaitForEndGame extends TimerTask
	{
		@Override
		public void run()
		{
			runNextEndGamePlayer();
		}
	}
	private class PickSpace extends TimerTask
	{
		final int location;
		private PickSpace(int space)
		{
			location = space;
		}
		@Override
		public void run()
		{
			resolveTurn(location);
		}
	}
	public MSController(TextChannel channelID)
	{
		channel = channelID;
	}
	void setResultChannel(TextChannel channelID)
	{
		resultChannel = channelID;
	}
	public void reset()
	{
		players.clear();
		currentTurn = -1;
		playersJoined = 0;
		playersAlive = 0;
		if(gameStatus != GameStatus.SEASON_OVER)
			gameStatus = GameStatus.SIGNUPS_OPEN;
		timer.cancel();
		timer = new Timer();
	}
}
