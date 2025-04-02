import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Args {

    @Parameter(names = "--enemiesCount", required = true,
            description = "Количество врагов")
    private int enemiesCount;
    @Parameter(names = "--wallsCount", required = true,
            description = "Количество препятствий")
    private int wallsCount;
    @Parameter(names = "--size", required = true,
            description = "Размер поля")
    private int size;
    @Parameter(names = "--profile", required = true,
            description = "Режим запуска")
    private String profile;

    public int getEnemiesCount() {
        return enemiesCount;
    }

    public int getWallsCount() {
        return wallsCount;
    }

    public int getSize() {
        return size;
    }

    public String getProfile() {
        return profile;
    }

    public void checkArgs() {
        if (size * size - wallsCount - enemiesCount - 2 < 0) {
            throw new IllegalParametersException("Невозможно разместить переданное количество врагов и препятствий на карте");
        }
    }

    @Override
    public String toString() {
        return "Args{" +
                "enemiesCount=" + enemiesCount +
                ", wallsCount=" + wallsCount +
                ", size=" + size +
                ", profile='" + profile + '\'' +
                '}';
    }
}
