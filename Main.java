package de.stunni.skyslayer.main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import de.stunni.skyslayer.KickCommand;
import de.stunni.skyslayer.UprankCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {

	private static ShardManager shardMan;
	public static boolean isKick;
	public static boolean isUprank;
	public static boolean isCancelledKick;
	public static boolean isCancelledUprank;
	

	public static void main(String[] args) throws LoginException, IllegalArgumentException {
		final String TOKEN = 
				"ODIxMzI3OTM3OTA3MTk1OTM1.GCw5nu.8MCh-F_AXs9ZvZIMyZqXDFHi6DvzuNuq6gUPgg";
		JDABuilder builder = JDABuilder.createDefault(TOKEN);
		JDA jda = builder
				.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
				.addEventListeners(new UprankCommand())
				.addEventListeners(new KickCommand())
				.setStatus(OnlineStatus.ONLINE)
				.setActivity(Activity.playing("⚔️ SkySlayer.DE ⚔️"))
				.build();

		shardMan = jda.getShardManager();

		jda.upsertCommand("uprank", "Upranke jemanden")
		.addOption(OptionType.USER, "user", "Wer soll geupranked werden?", true)
		.addOption(OptionType.ROLE, "role", "Auf welche Rolle soll der User geupranked werden?", true)
		.setGuildOnly(true)
		.queue();

		jda.upsertCommand("kickteam", "Kicke jemanden aus dem Team")
		.addOption(OptionType.USER, "teammitglied", "Wer soll gekickt werden?", true)
		.addOption(OptionType.STRING, "grund", "Gib den Grund für den Kick an", false)
		.setGuildOnly(true)
		.queue();

		System.out.println("Bot is now online.");

		shutdown(jda, builder);
	}

	public static void shutdown(JDA jda, JDABuilder builder) {
		new Thread(() -> {

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line = reader.readLine()) != null) {
					if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e") || line.equalsIgnoreCase("x")) {
						if (shardMan != null) {
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
						}
						jda.shutdown();
						reader.close();
						System.out.println("Bot is now offline.");
						builder.setStatus(OnlineStatus.OFFLINE);
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
	public static List<Button> sendButtonsUprank() {
		List<Button> buttons = new ArrayList();
		buttons.add(Button.success("ja", "Ja").withEmoji(Emoji.fromUnicode("✅")));
		buttons.add(Button.danger("nein", "Abbrechen").withEmoji(Emoji.fromUnicode("❌")));
		return buttons;
	}
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static List<Button> sendButtonsKick() {
		List<Button> buttons = new ArrayList();
		buttons.add(Button.success("ja1", "Ja").withEmoji(Emoji.fromUnicode("✅")));
		buttons.add(Button.danger("nein1", "Abbrechen").withEmoji(Emoji.fromUnicode("❌")));
		return buttons;
	}
	public static void removeRolesExcept(Guild g, Member m, Role r, Role r2) {
		List<Role> roles = new ArrayList<>();
		Role azubi = g.getRoleById("707001290785292349");
		Role sup = g.getRoleById("707001163047895091");
		Role tmod = g.getRoleById("707001018910507099");
		Role mod = g.getRoleById("707000879391440948");
		Role srmod = g.getRoleById("707000686927282177");
		Role leitung = g.getRoleById("1107355501903229038");
		Role admin = g.getRoleById("707000485659541525");
		Role coowner = g.getRoleById("707000159514656778");
		Role owner = g.getRoleById("706999968157794394");
		Role content = g.getRoleById("1107355572518527128");
		Role skyslayerteam = g.getRoleById("1107737275162427523");
		Role spieler = g.getRoleById("707003101881827401");


		roles.add(azubi);
		roles.add(sup);
		roles.add(tmod);
		roles.add(mod);
		roles.add(srmod);
		roles.add(leitung);
		roles.add(admin);
		roles.add(coowner);
		roles.add(owner);
		roles.add(content);
		roles.add(skyslayerteam);
		roles.add(spieler);

		for (Role role : roles) {
			if(role != r && role != r2) {
				g.removeRoleFromMember(m, role).queue();
			}
		}
		return;
	}
	public static void removeAllRoles(Guild g, Member m) {
		List<Role> roles = new ArrayList<>();
		Role azubi = g.getRoleById("707001290785292349");
		Role sup = g.getRoleById("707001163047895091");
		Role tmod = g.getRoleById("707001018910507099");
		Role mod = g.getRoleById("707000879391440948");
		Role srmod = g.getRoleById("707000686927282177");
		Role leitung = g.getRoleById("1107355501903229038");
		Role admin = g.getRoleById("707000485659541525");
		Role coowner = g.getRoleById("707000159514656778");
		Role owner = g.getRoleById("706999968157794394");
		Role content = g.getRoleById("1107355572518527128");
		Role skyslayerteam = g.getRoleById("1107737275162427523");
		Role spieler = g.getRoleById("707003101881827401");


		roles.add(azubi);
		roles.add(sup);
		roles.add(tmod);
		roles.add(mod);
		roles.add(srmod);
		roles.add(leitung);
		roles.add(admin);
		roles.add(coowner);
		roles.add(owner);
		roles.add(content);
		roles.add(skyslayerteam);
		roles.add(spieler);

		for (Role role : roles) {
			if(role != spieler) {
				g.removeRoleFromMember(m, role).queue();
				g.addRoleToMember(m, spieler).queueAfter(1, TimeUnit.SECONDS);
			}
		}
	}
	
	public static EmbedBuilder confirmEmbedUprank(Role r, Member m, String url) {
		EmbedBuilder eb_CONFIRM = new EmbedBuilder();
		eb_CONFIRM.setTitle("Bist du dir sicher?", null);
		eb_CONFIRM.setColor(Color.YELLOW);
		eb_CONFIRM.setDescription("Bist du dir sicher, dass du "+m.getAsMention()+" auf "+r.getAsMention()+" upranken willst?");
		ebAuthor(eb_CONFIRM, url);
		return eb_CONFIRM;
	}
	
	public static EmbedBuilder confirmEmbedKick(Member m, String url) {
		EmbedBuilder eb_CONFIRM = new EmbedBuilder();
		eb_CONFIRM.setTitle("Bist du dir sicher?", null);
		eb_CONFIRM.setColor(Color.YELLOW);
		eb_CONFIRM.setDescription("Bist du dir sicher, dass du "+m.getAsMention()+" aus dem Team kicken willst?");
		ebAuthor(eb_CONFIRM, url);
		return eb_CONFIRM;
	}
	
	public static void ebAuthor(EmbedBuilder eb, String url) {
		eb.setAuthor("SkySlayer", null, url);
	}
	
}
