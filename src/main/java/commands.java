import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class commands implements EventListener {


    private void createCommands(Guild Server) {
        Server.updateCommands().queue();

        CommandData mute = new CommandData("mute", "For taking away Matthew's First Amendment Right");
        mute.setDefaultEnabled(true);
        mute.addOption(OptionType.USER, "user", "user to be muted", true);

        CommandData test = new CommandData("test", "YOU SHOULDN'T BE SEEING THIS COMMAND");

        Server.upsertCommand(mute).queue();
        Server.upsertCommand(test).queue();
    }

    public void sendMessage(String msg, MessageChannel c) {
        c.sendMessage(msg).queue();
    }

    public boolean isAdmin(String authorId) {
        return authorId.equals("206677266111922176"); //Update this later
    }

    public void deleteMessage(Message msg) {
        msg.delete().queue();
    }

    //TODO: Legacy method
    private String getCommandContent(String[] args, int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent) { //Legacy commands TODO: Get rid of these.
            MessageReceivedEvent mEvent = (MessageReceivedEvent) event;
            if (mEvent.getMessage().getContentRaw().charAt(0) != BotLoaderMain.prefix)
                return;

            String[] args = mEvent.getMessage().getContentRaw().split(" ");
            String command = args[0].substring(1).toLowerCase();
            MessageChannel channel = mEvent.getChannel();
            String authorId = mEvent.getAuthor().getId();
            Message msg = mEvent.getMessage();

            if (command.equals("addcommand") && isAdmin(authorId)) { //Add basic one-liners.
                deleteMessage(msg);
                mEvent.getGuild().upsertCommand(args[1], getCommandContent(args, 2)).queue();
            }
            if (command.equals("wipecommands") && isAdmin(authorId)) {
                deleteMessage(msg);
                mEvent.getGuild().updateCommands().queue();
            }
            if (command.equals("resetcommands") && isAdmin(authorId)) {
                deleteMessage(msg);

                createCommands(mEvent.getGuild());
            }

        }
        if (event instanceof SlashCommandEvent) { //Newer and snazier commands TODO: Get rid of the other guy.
            SlashCommandEvent mEvent = (SlashCommandEvent) event;
            //String[] args = mEvent.getMessage().getContentRaw().split(" ");
            String command = mEvent.getName();
            MessageChannel channel = mEvent.getChannel();
            //String authorId = mEvent.getAuthor().getId();
            //Message msg = mEvent.getMessage();
            Guild guild = mEvent.getGuild();

            System.out.println("Chat message recieved");

            //if (command.charAt(0) == BotLoaderMain.prefix) {
            //    command = command.substring(1);

            //    System.out.println(String.format("Command issued by %s in %s: %c%s", authorId,msg.getGuild().getId(), BotLoaderMain.prefix, command));

            //Commands start here
            if (command.equals("test")) {
                //deleteMessage(msg);
                //sendMessage("Hello gamers!", channel);
                mEvent.reply("Hello gamers!").queue();
            }

            if (command.equals("mute")) {
                Member user = mEvent.getOption("user").getAsMember();
                Role mutedRole = guild.getRoleById(832345882585989130L);
                guild.addRoleToMember(user.getIdLong(), mutedRole).queue();
                user.mute(true).queue();
                System.out.println(user.getEffectiveName() + " has been muted!");
                mEvent.reply(String.format("Server muting %s...", user.getEffectiveName())).queue();
            }

            //Commands end here
        }
    }
}
