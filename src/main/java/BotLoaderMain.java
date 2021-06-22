import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BotLoaderMain {

    public static JDA jda;
    public static char prefix = '/';

    public static void main(String[] args) throws Exception {
        File tokenFile = new File("token.txt");
        Scanner tokenReader = new Scanner(tokenFile);
        String token = tokenReader.nextLine();
        tokenReader.close();

        jda = JDABuilder.createDefault(token).build();
        //jda.getGuildById(687006008895340562L).updateCommands().addCommands(new CommandData("test", "The epic gamer command")).queue();

        jda.addEventListener(new commands());
    }
}
