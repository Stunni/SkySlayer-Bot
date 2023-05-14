package de.stunni.skyslayer.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.stunni.skyslayer.listener.KickCommand;
import de.stunni.skyslayer.listener.UprankCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {

	private static ShardManager shardMan;
    public static boolean isKick;
    public static boolean isUprank;
    public static boolean isCancelled;
    public static boolean isSuccessUprank;
    public static boolean isSuccessKick;

    public static void main(String[] args) throws LoginException, IllegalArgumentException {
        final String TOKEN = 
        		"ODIxMzI3OTM3OTA3MTk1OTM1.GwDqw0.ZUMdSvo3L5Pr5l4ixM5vgV9t1ZUbwi5TKeFS4Q";
        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        JDA jda = builder
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new UprankCommand())
                .addEventListeners(new KickCommand())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("SkySlayer"))
                .build();

        shardMan = jda.getShardManager();

        jda.upsertCommand("uprank", "Upranke jemanden")
                .addOption(OptionType.USER, "user", "Wer soll geupranked werden?", true)
                .addOption(OptionType.ROLE, "role", "Auf welche Rolle soll der User geupranked werden?", true)
                .setGuildOnly(true)
                .queue();

        jda.upsertCommand("kickteam", "Kicke jemanden aus dem Team")
                .addOption(OptionType.USER, "user", "Wer soll gekickt werden?", true)
                .addOption(OptionType.STRING, "grund", "Gib den Grund für den Kick an", false)
                .setGuildOnly(true)
                .queue();

        System.out.println("Bot is now online.");

        shutdown(jda);
    }

    public static void shutdown(JDA jda) {
        new Thread(() -> {

            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardMan != null) {
                        	 shardMan.setStatus(OnlineStatus.OFFLINE);
                             shardMan.shutdown();
                        }
                        jda.shutdown();
                        reader.close();
                        System.out.println("Bot is now offline.");
                        shardMan.setStatus(OnlineStatus.OFFLINE);
                        shardMan.shutdown();
                    } else {
                        System.out.println("Use 'exit' to shutdown.");
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }

        }).start();
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
	public static List<Button> sendButtons() {
		List<Button> buttons = new ArrayList();
		buttons.add(Button.success("ja", "Ja").withEmoji(Emoji.fromUnicode("✅")));
		buttons.add(Button.danger("nein", "Abbrechen").withEmoji(Emoji.fromUnicode("❌")));
		return buttons;
	}

}
