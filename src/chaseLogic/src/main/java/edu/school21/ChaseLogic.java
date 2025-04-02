package edu.school21;
// package edu.school21.chaseLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * runMode - режим работы программы (пользовательский или разработчика)
 * mode - режим разработчика или пользовательского режима (принимает значения 'd' или 'p')
 * trace - символ для отслеживания движения врага
 */
public class ChaseLogic {
    private static String ALPHABETH = "abcdefghijklmnopqrstuvwxyz";
    private static char empty;
    private static char enemy;
    private static char wall;
    private static char player;
    private static char goal;
    private static char mode;

    public static char trace;

    public static void initChaseLogic(Properties properties, String runMode) {

        empty = getCharProperty(properties, "empty.char", ' ');
        enemy = getCharProperty(properties, "enemy.char", ' ');
        wall = getCharProperty(properties, "wall.char", ' ');
        player = getCharProperty(properties, "player.char", ' ');
        goal = getCharProperty(properties, "goal.char", ' ');

        char [] tmp = {empty, enemy, wall, player, goal};

        for (char c : tmp) {
            try {
                ALPHABETH = ALPHABETH.replace("" + c, "");
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }
        }
        mode = runMode.charAt(0);
        trace = ALPHABETH.charAt(0);
    }

    private static char getCharProperty(Properties properties, String key, char defaultValue) {
        String propValue = properties.getProperty(key);
        if (propValue == null || propValue.trim().isEmpty()) {
            return defaultValue;
        }
        return propValue.charAt(0);
    }

    public static void chase(char[][] grid) {
        if (mode == 'd') {
            return;
        }
        List <Node> enemiesList = enemiesListBuilder(grid, enemy);
        Node nodePlayer = nodePlayerBuilder(grid);
        for (Node node : enemiesList) {
            moveEnemy(grid, node, nodePlayer);
        }
    }

    public static void moveEnemy(char[][] grid, Node nodeEnemy, Node nodePlayer) {
        List <Node> path = PathFinder.findPath(grid, nodeEnemy, nodePlayer);
        if (path.isEmpty()) {
            randomMove(grid, nodeEnemy);
        } else {
            if (path.size() > 1) {
                Node nextStep = path.get(1);
                grid[nextStep.getX()][nextStep.getY()] = enemy;
                grid[nodeEnemy.getX()][nodeEnemy.getY()] = trace;
            
            }
        }

    }

    public static List<Node> enemiesListBuilder(char[][] grid, char c) {
        List <Node> enemiesList = new ArrayList<Node>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == c) {
                    enemiesList.add(new Node(i, j));
                }
            }
        }
        return enemiesList;
    }

    public static Node nodePlayerBuilder(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == player) {
                    return new Node(i, j);
                }
            }

        }
        return null;
    }

    private static void randomMove(char[][] grid, Node nodeEnemy) {
        final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] direction : DIRECTIONS) {
            int neighborX = nodeEnemy.x + direction[0];
            int neighborY = nodeEnemy.y + direction[1];
            if (neighborX >= 0 && neighborX < grid.length && neighborY >= 0 && neighborY < grid[neighborX].length && 
                        grid[neighborX][neighborY] != wall &&
                        grid[neighborX][neighborY] != enemy &&
                        grid[neighborX][neighborY] != goal) {
                grid[neighborX][neighborY] = enemy;
                grid[nodeEnemy.x][nodeEnemy.y] = empty;
                return;
            }
        }
    }
}
