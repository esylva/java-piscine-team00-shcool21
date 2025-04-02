package edu.school21;
// package edu.school21.chaseLogic;

import java.util.Objects;

/**
 * Класс узла сетки, содержащий координаты узла и информацию о стоимости пути.
 * fScore - Оценочная стоимость пути через этот узел
 * gScore - Стоимость перемещения от стартового узла до текущего
 * hScore - Эвристическая стоимость перемещения от текущего узла до целевого узла
 * parent - Родительский узел
 * x и y  - Координаты узла
 * typeOfNode - Тип узла (стена, пустое место, враг, игрок, финиш)
 */


public class Node {
    public int x;
    public int y;
    public int hScore;
    public int gScore;
    public Node parent;
    public char typeOfNode;
    
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Node(int x, int y, char typeOfNode) {
        this.x = x;
        this.y = y;
        this.typeOfNode = typeOfNode;
    }
    
    public int fScore() {
        return gScore + hScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

       // Геттеры
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

   public char getTypeOfNode() {
       return typeOfNode;
   }

    public int getHScore() {
        return hScore;
    }

    public int getGScore() {
        return gScore;
    }

    public Node getParent() {
        return parent;
    }

    // Сеттеры
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHScore(int hScore) {
        this.hScore = hScore;
    }

    public void setGScore(int gScore) {
        this.gScore = gScore;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setTypeOfNode(char typeOfNode) {
        this.typeOfNode= typeOfNode;
   }
}