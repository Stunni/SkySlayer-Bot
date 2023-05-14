package de.stunni.skyslayer.listener;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import de.stunni.skyslayer.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class KickCommand extends ListenerAdapter{

	private String grund;
	private Member m;
	private Role teamupdates;
	private String url;

	public static boolean hasRole(Member member, Role role) {
		List<Role> memberRoles = member.getRoles();
		return memberRoles.contains(role);
	}

	public void ebAuthor(EmbedBuilder eb) {
		eb.setAuthor("SkySlayer", null, url);
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
		if(e.getFullCommandName().equals("kickteam")) {
			Main.isKick = true;
			Main.isUprank = false;
		}
		if(e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			url = e.getGuild().getIconUrl();
			m = e.getOption("user").getAsMember();
			if(e.getOption("grund") != null) grund = e.getOption("grund").getAsString();
			teamupdates = e.getGuild().getRoleById("1107358257812611093");
			if(e.getName().equals("kickteam")) {
				EmbedBuilder eb_CONFIRM = new EmbedBuilder();
				eb_CONFIRM.setTitle("Bist du dir sicher?", null);
				eb_CONFIRM.setColor(Color.YELLOW);
				eb_CONFIRM.setDescription("Bist du dir sicher, dass du "+m.getAsMention()+" aus dem Team kicken willst?");
				ebAuthor(eb_CONFIRM);
				e.replyEmbeds(eb_CONFIRM.build()).setActionRow(Main.sendButtons()).queue(message -> {
					message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
				});

			}
		}
	}

	public void onButtonInteraction(ButtonInteractionEvent e) {
		TextChannel teamupdates_channel = e.getGuild().getTextChannelById("1107357240299618334");
		EmbedBuilder eb_KICK = new EmbedBuilder();
		eb_KICK.setTitle("TEAMUPDATE - KICK", null);
		eb_KICK.setColor(Color.RED);
		if(grund != null) {
			eb_KICK.setDescription("Name: "+m.getAsMention()+"\nGrund: "+grund+"\n\n"+teamupdates.getAsMention());
		}else if(grund == null){
			eb_KICK.setDescription("Name: "+m.getAsMention()+"\nGrund: /"+"\n\n"+teamupdates.getAsMention());
		}
		ebAuthor(eb_KICK);
		EmbedBuilder was_kicked = new EmbedBuilder();
		was_kicked.setTitle("KICK", null);
		was_kicked.setColor(Color.GREEN);
		was_kicked.setDescription(m.getAsMention()+" wurde erfolgreich von "+e.getMember().getAsMention()+" gekicked.");
		ebAuthor(was_kicked);
		
		
		if(!Main.isUprank && Main.isKick) {
			System.out.println(e.getComponentId());
			if(e.getComponentId().equals("ja")) {
				Main.isCancelled = false;
				e.getMessageChannel().sendMessage("```Die Aktion wird ausgeführt...```").queue(message -> {
		            message.delete().queueAfter(3, TimeUnit.SECONDS);
		        });
				e.getMessageChannel().sendMessageEmbeds(was_kicked.build()).queue();
				teamupdates_channel.sendMessageEmbeds(eb_KICK.build()).queue();
			}else if(e.getComponentId().equals("nein")) {
				Main.isCancelled = true;
				e.getMessageChannel().sendMessage("```Die Aktion wird abgebrochen...```").queue(message -> {
		            message.delete().queueAfter(1, TimeUnit.SECONDS);
		        });
				e.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
				return;
			}
		}


		if(Main.isCancelled == false) {

			

			Role azubi = e.getGuild().getRoleById("707001290785292349");
			Role sup = e.getGuild().getRoleById("707001163047895091");
			Role tmod = e.getGuild().getRoleById("707001018910507099");
			Role mod = e.getGuild().getRoleById("707000879391440948");
			Role srmod = e.getGuild().getRoleById("707000686927282177");
			Role leitung = e.getGuild().getRoleById("1107355501903229038");
			Role admin = e.getGuild().getRoleById("707000485659541525");
			Role coowner = e.getGuild().getRoleById("707000159514656778");
			
			e.getGuild().modifyNickname(m, m.getUser().getName()).queue();
			if(m.getRoles().contains(azubi)) {
				
				e.getGuild().removeRoleFromMember(m, azubi).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(sup)) {
				e.getGuild().removeRoleFromMember(m, sup).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(tmod)) {
				e.getGuild().removeRoleFromMember(m, tmod).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(mod)) {
				e.getGuild().removeRoleFromMember(m, mod).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(srmod)) {
				e.getGuild().removeRoleFromMember(m, srmod).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(leitung)) {
				e.getGuild().removeRoleFromMember(m, leitung).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(admin)) {
				e.getGuild().removeRoleFromMember(m, admin).queue();
				Main.isSuccessKick = true;
			}
			if(m.getRoles().contains(coowner)) {
				e.getGuild().removeRoleFromMember(m, coowner).queue();
				Main.isSuccessKick = true;
			}
			Main.isCancelled = false;
		}
	}
}
