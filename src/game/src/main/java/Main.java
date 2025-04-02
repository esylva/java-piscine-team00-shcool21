import com.beust.jcommander.JCommander;
import edu.school21.ChaseLogic;
import edu.school21.PathFinder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Args jArgs = new Args();
        JCommander.newBuilder()
                .addObject(jArgs)
                .build()
                .parse(args);
        jArgs.checkArgs();

        Config configs = new Config(jArgs.getProfile());
        ChaseLogic.initChaseLogic(configs.getProperties(), jArgs.getProfile());
        PathFinder.initPathFinder(configs.getProperties(), jArgs.getProfile());
        Graphics graphics = new Graphics(jArgs, configs);
        graphics.print();

        GameRun gameRun = new GameRun(graphics, configs, jArgs.getProfile());
        gameRun.run();
    }
}
