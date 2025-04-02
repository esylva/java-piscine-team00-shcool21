import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import edu.school21.PathFinder;

import java.util.Random;

public class Graphics {
    private Random random = new Random();
    private ColoredPrinter printer = new ColoredPrinter();

    private int width;
    private int height;
    private int enemiesCount;
    private int wallsCount;

    private char empty;
    private char enemy;
    private char wall;
    private char player;
    private char goal;

    private Ansi.BColor emptyColor;
    private Ansi.BColor enemyColor;
    private Ansi.BColor wallColor;
    private Ansi.BColor playerColor;
    private Ansi.BColor goalColor;

    private String profile;
    private char[][] field;
    private PlayerCoordinates playerCoordinates;

    public Graphics(Args args, Config params) {
        this.width = args.getSize();
        this.height = args.getSize();
        this.enemiesCount = args.getEnemiesCount();
        this.wallsCount = args.getWallsCount();
        this.field = new char[height][width];

        this.empty = params.getSymbol("empty");
        this.enemy = params.getSymbol("enemy");
        this.wall = params.getSymbol("wall");
        this.player = params.getSymbol("player");
        this.goal = params.getSymbol("goal");

        this.emptyColor = params.getColor("empty");
        this.enemyColor = params.getColor("enemy");
        this.wallColor = params.getColor("wall");
        this.playerColor = params.getColor("player");
        this.goalColor = params.getColor("goal");
        this.profile = args.getProfile();
    }

    public void print() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = ' ';
            }
        }
        savePlayer();
        saveEnemies();
        saveWalls();
        saveGoal();
        if (PathFinder.findPath(field).isEmpty()) {
            print(); // если нет пути к цели, генерируем новое поле
        }
        printField();
    }

    protected void printField() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field[i][j] == enemy) print(enemy, enemyColor);
                else if (field[i][j] == wall) print(wall, wallColor);
                else if (field[i][j] == player) print(player, playerColor);
                else if (field[i][j] == goal) print(goal, goalColor);
                else if (field[i][j] == empty) print(empty, emptyColor);
                else {
                    if (profile.equals("dev"))
                        print(field[i][j], Ansi.BColor.NONE);
                    else print(empty, emptyColor);
                }
            }
            System.out.println();
        }
    }

    private void print(char symbol, Ansi.BColor color) {
        printer.print(symbol, Ansi.Attribute.NONE, Ansi.FColor.NONE, color);
    }

    private void saveEnemies() {
        int count = 0;
        int row;
        int col;
        while (count < enemiesCount) {
            row = random.nextInt(height);
            col = random.nextInt(width);

            if (field[row][col] == ' ') {
                field[row][col] = enemy;
                count++;
            }
        }
    }

    private void saveWalls() {
        int count = 0;
        int row;
        int col;
        while (count < wallsCount) {
            row = random.nextInt(height);
            col = random.nextInt(width);

            if (field[row][col] == ' ' && !isBlockingPlayer(row, col)) {
                field[row][col] = wall;
                count++;
            }
        }
    }

    protected boolean isBlockingPlayer(int row, int col) {
        int freePath = 0;

        if (row > 0 && field[row - 1][col] == ' ') freePath++; // вверх
        if (row < height - 1 && field[row + 1][col] == ' ') freePath++; //вниз
        if (col > 0 && field[row][col - 1] == ' ') freePath++; // влево
        if (col < width - 1 && field[row][col + 1] == ' ') freePath++; //вправо

        return freePath == 0;
    }

    private void savePlayer() {
        int row;
        int col;
        do {
            row = random.nextInt(height);
            col = random.nextInt(width);
        } while (row == 0 || row == height - 1 || col == 0 || col == width - 1);

        playerCoordinates = new PlayerCoordinates(row, col);
        field[row][col] = player;
    }

    private void saveGoal() {
        int row;
        int col;
        do {
            row = random.nextInt(height);
            col = random.nextInt(width);
        } while (field[row][col] != ' ');

        field[row][col] = goal;
    }

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }

    public PlayerCoordinates getPlayerCoordinates() {
        return playerCoordinates;
    }

    public void setPlayerCoordinates(PlayerCoordinates playerCoordinates) {
        this.playerCoordinates = playerCoordinates;
        field[playerCoordinates.getRowPlayer()][playerCoordinates.getColPlayer()] = player;
    }
}
