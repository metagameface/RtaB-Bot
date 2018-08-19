package tel.discord.rtab;

import net.dv8tion.jda.core.entities.Member;

public interface Controller
{
	void reset();
	void addPlayer(Member playerID);
	void removePlayer(Member playerID);
}
