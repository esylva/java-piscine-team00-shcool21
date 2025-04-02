package edu.school21;

import java.util.*;


/**
 * Класс для поиска пути на карте
 *
 */

public class PathFinder {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static char empty;
    private static char enemy;
    private static char wall;
    private static char player;
    private static char goal;
    private static char mode;


    public static void initPathFinder(Properties properties, String runMode) {
        empty = getCharProperty(properties, "empty.char", ' ');
        enemy = getCharProperty(properties, "enemy.char", ' ');
        wall = getCharProperty(properties, "wall.char", ' ');
        player = getCharProperty(properties, "player.char", ' ');
        goal = getCharProperty(properties, "goal.char", ' ');

        mode = runMode.charAt(0);
    }
    private static char getCharProperty(Properties properties, String key, char defaultValue) {
        String propValue = properties.getProperty(key);
        if (propValue == null || propValue.trim().isEmpty()) {
            return defaultValue;
        }
        return propValue.charAt(0);
    }
    
    public static List<Node> findPath(char[][] grid) {
        int startX = 0, startY = 0, endX = 0, endY = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == player) {
                    startX = i;
                    startY = j;
                } else if (grid[i][j] == goal) {
                    endX = i;
                    endY = j;
                }
            }
        }

        Node startNode = new Node(startX, startY, player);
        Node endNode = new Node(endX, endY, goal);

        return findPath(grid, startNode, endNode);
    }

    public static List<Node> findPath(char[][] grid, Node startNode, Node endNode) {
        
        if (startNode == null || endNode == null) {
            return new ArrayList<>();
        }

        Set<Node> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::fScore));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            if (currentNode.equals(endNode)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode);
            for (int[] direction : DIRECTIONS) {
                int neighborX = currentNode.x + direction[0];
                int neighborY = currentNode.y + direction[1];
                if (startNode.getTypeOfNode() == player && isValid(neighborX, neighborY, grid) || 
                    startNode.getTypeOfNode() != player && isValidEnemy(neighborX, neighborY, grid)) {
                    Node neighbor = new Node(neighborX, neighborY);
                    if (closedSet.contains(neighbor)) {
                        continue;
                    }

                    int tentativeGCost = currentNode.gScore + 1;
                    if (openSet.contains(neighbor) && tentativeGCost >= neighbor.gScore) {
                        continue;
                    }

                    neighbor.gScore = tentativeGCost;
                    neighbor.hScore = manhattanDistance(neighbor, endNode);
                    neighbor.parent = currentNode;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }   
        }
        return new ArrayList<>();
    }

    private static List<Node> reconstructPath(Node currentNode) {
        List<Node> path = new ArrayList<>();
        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = currentNode.parent;
        }
        return path;
    }

    private static boolean isValid(int x, int y, char[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[x].length && grid[x][y] != wall && grid[x][y] != enemy;
    }

    private static boolean isValidEnemy(int x, int y, char[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[x].length && grid[x][y] != wall && grid[x][y] != enemy && grid[x][y] != goal;
    }

    private static int manhattanDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}