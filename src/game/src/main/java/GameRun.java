import edu.school21.ChaseLogic;
import edu.school21.Node;

import java.util.List;
import java.util.Scanner;

import static edu.school21.ChaseLogic.chase;

public class GameRun {
    private Graphics graphics;
    private Config config;
    private Scanner scanner = new Scanner(System.in);

    private String profile;
    private char[][] field;
    private boolean finish = false;

    public GameRun(Graphics graphics, Config config, String profile) {
        this.graphics = graphics;
        this.field = graphics.getField();
        this.config = config;
        this.profile = profile;
    }

    public void run() {
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("9")) {
                System.out.println("Вы вышли. Проигрыш");
                break;
            }

            if (!processPlayerMove(command)) {
                System.out.println("Повторите команду");
                continue;
            }

            if (profile.equals("dev")) {
                profileDev();
            } else {
                chase(field);
                graphics.printField();
            }

            if (finish) {
                System.out.println("Вы победили!!!");
                break;
            }

            if (!checkGameOver()) {
                System.out.println("Враг догнал вас. Вы проиграли");
                break;
            }

        }
    }

    private void profileDev() {
        char enemyChar = config.getSymbol("enemy");
        Node player = ChaseLogic.nodePlayerBuilder(field);
        List<Node> enemies = ChaseLogic.enemiesListBuilder(field, enemyChar);
        for (Node enemy : enemies) {
            System.out.println("Подтвердите ход врага (введите \"8\")");
            String input = scanner.nextLine();
            if (input.equals("9")) {
                System.out.println("Вы вышли. Проигрыш");
                return;
            }
            if (input.equals("8")) {
                ChaseLogic.moveEnemy(field, enemy, player);
                graphics.printField();
            } else {
                continue;
            }
        }
    }

    private boolean checkGameOver() {
        char playerSymbol = config.getSymbol("player");
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == playerSymbol) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean processPlayerMove(String command) {
        PlayerCoordinates coordinatesCurrent = graphics.getPlayerCoordinates();
        PlayerCoordinates coordinatesNew = new PlayerCoordinates(coordinatesCurrent.getRowPlayer(), coordinatesCurrent.getColPlayer());
        switch (command) {
            case "a":
            case "A":
                coordinatesNew.setColPlayer(coordinatesCurrent.getColPlayer() - 1);
                break;
            case "w":
            case "W":
                coordinatesNew.setRowPlayer(coordinatesCurrent.getRowPlayer() - 1);
                break;
            case "d":
            case "D":
                coordinatesNew.setColPlayer(coordinatesCurrent.getColPlayer() + 1);
                break;
            case "s":
            case "S":
                coordinatesNew.setRowPlayer(coordinatesCurrent.getRowPlayer() + 1);
                break;
            default:
                System.out.println("Неверная команда. Введите одну из команд AWSD");
                return false;
        }

        if (isOutOfBounds(coordinatesNew.getRowPlayer(), coordinatesNew.getColPlayer()))
            return false;

        if (checkNextCoordinates(coordinatesNew.getRowPlayer(), coordinatesNew.getColPlayer())) {
            finish = isFinish(coordinatesNew.getRowPlayer(), coordinatesNew.getColPlayer());
            field[coordinatesCurrent.getRowPlayer()][coordinatesCurrent.getColPlayer()] = ' ';
            graphics.setPlayerCoordinates(coordinatesNew);
            return true;
        }

        if (checkEnemyCord(coordinatesNew.getRowPlayer(), coordinatesNew.getColPlayer())) {
            System.out.println("Враг догнал вас. Вы проиграли");
            System.exit(0);
        }
        if (isBlockingPlayer(coordinatesNew.getRowPlayer(), coordinatesNew.getColPlayer())) {
            System.out.println("Игрок заблокирован. Вы проиграли");
            System.exit(0);
        }
        return false;
    }

    protected boolean isBlockingPlayer(int row, int col) {
        int freePath = 0;

        if (row > 0 && field[row - 1][col] == ' ') freePath++; // вверх
        if (row < field.length - 1 && field[row + 1][col] == ' ') freePath++; //вниз
        if (col > 0 && field[row][col - 1] == ' ') freePath++; // влево
        if (col < field.length - 1 && field[row][col + 1] == ' ') freePath++; //вправо

        return freePath == 0;
    }

    private boolean checkNextCoordinates(int row, int col) {
        char cell = field[row][col];
        return cell != config.getSymbol("wall") && cell != config.getSymbol("enemy");
    }

    private boolean checkEnemyCord(int row, int col) {
        return field[row][col] == config.getSymbol("enemy");
    }

    private boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= field.length || col < 0 || col >= field[0].length;
    }

    private boolean isFinish(int row, int col) {
        return field[row][col] == config.getSymbol("goal");
    }

}
