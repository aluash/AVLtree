package com.company;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Main AVLTree = new Main();
        System.out.println("The total number of the elements of the AVL tree");
        int t = sc.nextInt();
        int[] arr;
        arr = new int[t];
        System.out.println("Enter values for branches of the AVL tree");

        try {
            for(int i=0;i<arr.length;i++)
            {
                arr[i]=sc.nextInt();
            }
        }
        catch(NumberFormatException e) {     //если формат ячейки т,е int не соблюдается
            System.out.println("Detected Numberformatexception...");
        }
        catch (InputMismatchException r) {   //если тип int не соблюдается(человек ввел строку вместо циферок)
            System.out.println("Detected InputMismatchException...");
        }


        for(int i=0;i<arr.length;i++){        //заполняем наше АВЛ дерево
            AVLTree.insert(arr[i]);
        }

        AVLTree.traverseInOrder();      //до удаления ячейки если это надобно

        for(int i=0;i<arr.length;i++){
            if (arr[i]==7)AVLTree.delete(6);  // допустим мы ненавидим число 6 или любое другое число
        }

        // AVLTree.traverseInOrder();   //и дерево без ненавистной нам 6

    }
    // далее классы
    //реализуем интерфейс через класс Node

    public static class Node implements AVLTree {    //создаем класс узла
        Node right,left;             //строки потомков
        Node parent;                 //родительская
        int value;                   //заданное число из которых мы и будем делать наше дерево AVL
        int height = 0;              //Высота дерева

        public Node(int data, Node parent){    //конструктор узела
            this.value = data;                 //ПРИРАВНИВАЕМ ЧИСЛО К ЧИСЛУ А РОДИТЕЛЯ К РОДИТЕЛЮ
            this.parent = parent;
        }

        @Override                    //переопределение метода класса джава toString()
        public String toString(){

        /*
        Метод toString(ПИСАТЬ) это метод класса Objects,возвращающий символьную
        строку описывающую объект.Чтобы вывести удобно и красиво на экран мы
        переопределили toString так чтобы напись что выдаст нам среда была нам понятна
        */

            return " |*| VALUE (" + value + ")" + " HEIGHT(" + height + ") PARENT (" + (parent == null ?
                    //?- это условный оператор взаимозаменяемое if
                    "NO PARENT CELL" : /* (:) это else */  parent.value) + ") || ";
        }

        @Override
        public void setRightChild(Node child) {
            //задать правого потомка
            if (child != null) {      //если потомок не равен null т,е если есть поддерево
                child.parent = this;  //то значение начала развилки станет родителем этого поддерева
            }
            this.right = child;       //задаем правого потомка
        }

        @Override
        public void setLeftChild(Node child) {
            //задать левого потомка или левое поддерево,(бұтақ)
            if (child != null) {      //если потомок не равен null т,е если есть поддерево
                child.parent = this;  //то значение начала развилки станет родителем этого поддерева
            }
            this.left = child;        //задаем левого потомка
        }
    }
    //теперь методы в классе Main

    private Node root = null;  //приватная строка нулл будет служить как логический ключ

    public void insert(int data) {//вставка числа
        insert(root, data);    //вызываем одноименный метод
    }

    private int height(Node node) {
        return node == null ? -1 : node.height;  //вычисляем высоту
    }

    private void reHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        //перевычисление высоты
        // этот метод понадобится после удаление каких нибудь ячеек или после того как перевернули
    }

    private void insert(Node node, int value){
        //вставка в дерево
        if (root == null) {
            root = new Node(value, null);
            //вставить каждое число переправленный сюда в дерево
            return;
            //закрыть
        }

        if (value < node.value) {          //если добавленное число меньше чем другая
            if (node.left != null) {       //если левое поддерево не пустое
                insert(node.left, value);  //то вставить в ее место (рекурсия)
            } else {//или оно пустое
                node.left = new Node(value, node);   //то вставить в пустое место
            }

            if (height(node.left) - height(node.right) == 2) { //левая сторона длиннее
                if (value < node.left.value) {   //если число меньше чем левый потомок
                    rotateRight(node);           //повернуть вправо
                } else {
                    rotateLeftThenRight(node);   //или же повернуть налево потом направо чтобы первый элемент был правый второй левый
                }
            }
        } else if (value > node.value) {     //если значение больше чем другая  т,е предыдущие
            if (node.right != null) {       //то вычислить существует ли число т,е не равно ли пустоте
                insert(node.right, value); //вставить вместо него новопоступленное
            } else {
                node.right = new Node(value, node);//или задать как ячейка с предком
            }

            if (height(node.right) - height(node.left) == 2) { //правая сторона длиннее
                if (value > node.right.value)       //если число больше чем значение правого поддерева
                    rotateLeft(node);              //повернуть налево
                else {
                    rotateRightThenLeft(node);    //или же повернуть направо потом налево чтобы первый элемент был правый второй левый                 }
            }
        }

        reHeight(node);   //переопределение высоты узла
    }

    private void rotateRight(Node turn) {  //метод для поворота направо
        Node parent = turn.parent;
        Node leftChild = turn.left;
        Node rightChildOfLeftChild = leftChild.right;
        turn.setLeftChild(rightChildOfLeftChild);
        leftChild.setRightChild(turn);
        if (parent == null) {
            this.root = leftChild;
            leftChild.parent = null;
            return;
        }

        if (parent.left == turn) {
            parent.setLeftChild(leftChild);
        } else {
            parent.setRightChild(leftChild);
        }

        reHeight(turn);
        reHeight(leftChild);
    }

    private void rotateLeft(Node turn) {  //точно такой же метод для поворота налево
        Node parent = turn.parent;
        Node rightChild = turn.right;
        Node leftChildOfRightChild = rightChild.left;
        turn.setRightChild(leftChildOfRightChild);
        rightChild.setLeftChild(turn);
        if (parent == null) {
            this.root = rightChild;
            rightChild.parent = null;
            return;
        }

        if (parent.left == turn) {
            parent.setLeftChild(rightChild);
        } else {
            parent.setRightChild(rightChild);
        }

        reHeight(turn);
        reHeight(rightChild);
    }


    private void rotateLeftThenRight(Node node) {  //это для того чтобы повернуть вправо затем влево
        rotateLeft(node.left);
        rotateRight(node);
    }

    private void rotateRightThenLeft(Node node) {  //это тот же метод но наоборот
        rotateRight(node.right);
        rotateLeft(node);
    }

    public boolean delete(int key) {  //метод удаление ячейки
        Node target = search(key);
        if (target == null) return false;  //если таргета нет то возвратить фолз
        target = deleteNode(target);       //если же число существует т,е равен к чему либо то удалить
        balanceTree(target.parent);        //проверить баланс дерева
        return true;
    }

    private Node deleteNode(Node target) {//удаление какого нибудь объекта
        if (IsDiversion(target)) { //есть ли отвлетвление у удаляемого объекта отвлетвление
            if (isLeftChild(target)) {
                target.parent.left = null;
            } else {
                target.parent.right = null;
            }
        }
        else if (target.left == null ^ target.right == null) { //если один потомок
            Node nonNullChild = target.left == null ? target.right : target.left;
            if (isLeftChild(target)) {
                target.parent.setLeftChild(nonNullChild);
            } else {
                target.parent.setRightChild(nonNullChild);
            }
        }
        else {//если 2 потомка
            Node immediatePuttingInOrder = immediatePuttingInOrder(target);
            target.value = immediatePuttingInOrder.value;
            target = deleteNode(immediatePuttingInOrder);
        }

        reHeight(target.parent);
        return target;
    }

    private Node immediatePuttingInOrder(Node node) {//приведение в порядок
        Node current = node.left;
        while (current.right != null) {
            current = current.right;
        }

        return current;
    }

    private boolean isLeftChild(Node child) {//задать как левый потомок
        return (child.parent.left == child);
    }

    private boolean IsDiversion(Node node) {//проверка наличии листвы т,е проверка чтобы проверить есть ли отвлетвление
        return node.left == null && node.right == null;
    }

    private int calculateDifference(Node node) {//метод вычитания разницы
        int rightHeight = height(node.right);
        int leftHeight = height(node.left);
        return rightHeight - leftHeight;
    }

    private void balanceTree(Node node) {//балансировка дерева
        int difference = calculateDifference(node);//задаем разницу
        Node parent = node.parent;
        if (difference == -2) {//если разница высоты равна -2
            if (height(node.left.left) >= height(node.left.right)) {
                rotateRight(node);//вращаем вправо
            } else {
                rotateLeftThenRight(node);//все правильно так что делаем поворот влево и вправо чтобы вернулось на круги своя

            }
        } else if (difference == 2) {//если разница высоты равна 2
            if (height(node.right.right) >= height(node.right.left)) {
                rotateLeft(node);//вращаем влево
            } else {
                rotateRightThenLeft(node);//все правильно так что делаем поворот вправо и влево чтобы вернулось на круги своя

            }
        }

        if (parent != null) {//если нет родителя то может быть что балансировка дерева пошатнется
            balanceTree(parent);//поэтому ссылаем его к балансировщику дерева
        }

        reHeight(node);//перевысить узел
    }

    public Node search(int key) {//поиск т.е метод чтобы направить на двоичный поиск
        return binarySearch(root, key);
    }

    private Node binarySearch(Node node, int key) {
        // метод бинарный поиск чтобы найти ->
        // -> конкретные числа чтобы построить дерево и чтобы в дальнейшем обозначить с правой ли стороны будет число или с левой
        if (node == null) return null;

        if (key == node.value) {
            return node;
        }

        if (key < node.value && node.left != null) {
            return binarySearch(node.left, key);
        }

        if (key > node.value && node.right != null) {
            return binarySearch(node.right, key);
        }
        return null;
    }

    public void traverseInOrder() {//вывести на экран корень дерева(ағаштың ұшы) и начать поочередное печатывание
        System.out.println("КОРЕНЬ ДЕРЕВА \n"+ root.toString());
        InOrder(root);
        System.out.println();
    }

    private void InOrder(Node juie){
        if (juie != null) {
            InOrder(juie.left);     //рекурсия в этот же метод поддерева левой стороны
            System.out.print(juie.toString());  //печатается
            InOrder(juie.right);    //рекурсия в этот же метод поддерева правой стороны
//простой метод сложность
        }
    }
//массив сложность :О(1)
//АВЛ дерево сложность: О(log(n))
//Бинарный поиск сложность: О(log(n))
}
