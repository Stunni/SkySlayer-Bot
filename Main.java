package de.stunni.skyslayer.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import de.stunni.skyslayer.listener.KickCommand;
import de.stunni.skyslayer.listener.UprankCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {

	public static ShardManager shardMan;


	public static void main(String[] args) throws LoginException, IllegalArgumentException{
		final String TOKEN = "ODIxMzI3OTM3OTA3MTk1OTM1.Gjc9T1.qssudz_iZpuO6lut23Abi2mtdqreywJcdlI5N8";
		JDABuilder builder = JDABuilder.createDefault(TOKEN);
		JDA jda = builder
				.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
				.addEventListeners(new UprankCommand())
				.addEventListeners(new KickCommand())
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.watching("SkySlayer"))
				.build();

		jda.upsertCommand("uprank", "Upranke jemanden")
		.addOption(OptionType.USER, "user", "Wer soll geupranked werden?", true)
		.addOption(OptionType.ROLE, "role", "Auf welche Rolle soll der user geupranked werden?", true)
		.setGuildOnly(true).queue();
		
		jda.upsertCommand("kickteam", "Kicke jemanden aus dem Team")
		.addOption(OptionType.USER, "user", "Wer soll gekickt werden?", true)
		.addOption(OptionType.STRING, "grund", "Gib den Grund für den Kick an", false)
		.setGuildOnly(true).queue();

		System.out.println("Bot is now online.");

		shutdown();
	}

	public static void shutdown() {
		new Thread(() -> {

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					if(line.equalsIgnoreCase("exit")) {
							if(shardMan != null) {
								shardMan.setStatus(OnlineStatus.OFFLINE);
								shardMan.shutdown();
								System.out.println("Bot is now offline.");
							}
						reader.close();
					}
					else {
						System.out.println("Use 'exit' to shutdown.");
					}
				}
			}catch (IOException e) {
				e.printStackTrace();
			}

		}).start();
	}

}
