
/* Name: William Stewart    
 * Student Number: c3282367
 * File: MazeSolverBFS.java
 * Description: 
 * Retrieves a given maze file that reads then solves the maze using breadth first search and depth first search
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolver {
    //All static potential state values
    private static final int both_closed = 0;
    private static final int right_open = 1;
    private static final int bottom_open = 2;
    private static final int both_open = 3;
    private static int row_count;
    private static int col_count;
    private static Queue<Node> current_layer;
    private static Queue<Node> next_layer;
    private static Queue<Node> visited_nodes;
    private static Queue<Node> adjacent_nodes;
    private static int bfs_total_steps;
    private static int dfs_total_steps;
    private static int bfs_solution_steps;
    private static int dfs_solution_steps;
    private static long start_time;
    private static long bfs_time_taken;
    private static long dfs_time_taken;
    private static String bfs_solution ="";
    private static String dfs_solution ="";
        
        
    //All nodes
    private static Node grid[][];
    private static Node starting_node;
    private static Node finishing_node;

    public static void main(String[] args) {
        // Scan the maze file
        String input = "";
        String output = args[1];
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }

        // two layers to emulate BFS
        current_layer = new LinkedList<Node>();
        next_layer = new LinkedList<Node>();
        visited_nodes = new LinkedList<Node>();
        adjacent_nodes = new LinkedList<Node>();
        bfs_solution_steps = 0;
        bfs_total_steps = 0;
        bfs_solution_steps = 0;
        dfs_total_steps = 0;
        start_time = System.currentTimeMillis();
        createGrid(input);
        connectNodes();
        solveMazeUsingBFS();
        start_time = System.currentTimeMillis();
        solveMazeUsingDFS();
        outputBFSSolution();
        outputDFSSolution();

        //Need to make outputs write to the solution file
        saveSolutionFile(output);


        //TO-DO: write dfs solution
        //write output to output file

    }

    public static void outputBFSSolution() {
        //Specify BFS
        System.out.println("BFS");
        //Display the solution
        generateBFSPathSolution();
        //Display steps in solution
        System.out.println(bfs_solution_steps);
        //Display total steps
        System.out.println(bfs_total_steps);
        //Display time taken
        System.out.println(bfs_time_taken);
    }

    public static void outputDFSSolution() {
        //Specify DFS
        System.out.println("DFS");
        //Display the solution
        generateDFSPathSolution();
        //Display steps in solution
        System.out.println(dfs_solution_steps);
        //Display total steps
        System.out.println(dfs_total_steps);
        //Display time taken
        System.out.println(dfs_time_taken);
    }

    public static void generateBFSPathSolution() {
        String path = "(";
        Stack<Integer> stack = new Stack<>();
        Node tempNode = finishing_node;
        // start at the end node and trace back to the start node
        while (tempNode != starting_node) {
            tempNode.setCorrectStep(true);
            // push visited node to a stack that will then be read to output the results in
            // the right order
            stack.push(tempNode.getValue());
            tempNode = tempNode.getParent();
            bfs_solution_steps++;
        }
        String value;
        // output the path taken to the end node
        while (!stack.isEmpty()) {
            value = Integer.toString(stack.pop());
            if (Integer.parseInt(value) != finishing_node.getValue()) {
                path += value + ",";
            } else {
                path += value + ")";
            }
        }
        bfs_solution = path;
    }

        public static void generateDFSPathSolution() {
        String path = "(";
        Stack<Integer> stack = new Stack<>();
        Node tempNode = finishing_node;
        // start at the end node and trace back to the start node
        while (tempNode != starting_node) {
            tempNode.setCorrectStep(true);
            // push visited node to a stack that will then be read to output the results in
            // the right order
            stack.push(tempNode.getValue());
            tempNode = tempNode.getParent();
            dfs_solution_steps++;
        }
        String value;
        // output the path taken to the end node
        while (!stack.isEmpty()) {
            value = Integer.toString(stack.pop());
            if (Integer.parseInt(value) != finishing_node.getValue()) {
                path += value + ",";
            } else {
                path += value + ")";
            }
        }
        dfs_solution = path;
    }


    // solves the maze using BFS
    public static void solveMazeUsingBFS() {
        boolean foundEnd = false;
        current_layer.add(starting_node);
        // while loop that will finish once the end node is found
        while (!foundEnd) {
            for (Node nextNode : current_layer) {
                foundEnd = checkNodeBFS(nextNode);
                if (foundEnd) {
                    bfs_time_taken = System.currentTimeMillis() - start_time;
                    break;
                }
            }
            // start searching the next layer
            current_layer = next_layer;
            next_layer = new LinkedList<Node>();
        }
    }

    // solves the maze using DFS
    public static void solveMazeUsingDFS() {
        boolean foundEnd = false;
        current_layer.add(starting_node);
        // while loop that will finish once the end node is found
        while (!foundEnd) {
            for (Node nextNode : current_layer) {
                foundEnd = checkNodeDFS(nextNode);
                if (foundEnd) {
                    dfs_time_taken = System.currentTimeMillis() - start_time;
                    break;
                }
            }
            // start searching the next layer
            current_layer = next_layer;
            next_layer = new LinkedList<Node>();
        }
    }


    // checks the current node to see if it is the end node otherwise add
    // neighboring available nodes to the next layer
    public static boolean checkNodeBFS(Node nextNode) {
        visited_nodes.add(nextNode);
        nextNode.setVisited(true);
        bfs_total_steps++;

        if (nextNode == finishing_node) {
            return true;
        } else {
            // add neighbouring available nodes to the next layer
            for (Node tempNode : nextNode.getAvailableNodes()) {
                tempNode.setParent(nextNode);
                next_layer.add(tempNode);
            }
        }
        return false;

    }

        // checks the current node to see if it is the end node otherwise add
    // neighboring available nodes to the next layer
    public static boolean checkNodeDFS(Node nextNode) {
        visited_nodes.add(nextNode);
        nextNode.setVisited(true);
        dfs_total_steps++;

        if (nextNode == finishing_node) {
            return true;
        } else {
            // add neighbouring available nodes to the next layer
            for (Node tempNode : nextNode.getAvailableNodes()) {
                tempNode.setParent(nextNode);
                next_layer.add(tempNode);
            }
        }
        return false;

    }

    public static void connectNodes() {
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {

                // if node has right neighbor
                if (col_count > 1 && j != (col_count - 1)) {
                    grid[i][j].setRight(grid[i][j + 1]);
                }
                // if node has top neighbor
                if (i != 0) {
                    grid[i][j].setTop(grid[i - 1][j]);
                }
                // if node has left neighbor
                if (col_count > 1 && j != 0) {
                    grid[i][j].setLeft(grid[i][j - 1]);
                }
                // if node has bottom neighbor
                if (i != (row_count - 1)) {
                    grid[i][j].setBottom(grid[i + 1][j]);
                }
            }
        }
    }

    // method that recieves the data from the given file that then creates a maze
    // using a multidimensional array of nodes
    public static void createGrid(String input) {
        try {
            String[] arrSplit = input.split("");
            int i = 0;
            String row_countStr = "";
            // get n
            while (!arrSplit[i].equals(",")) {
                row_countStr += arrSplit[i];
                i++;
            }
            row_count = Integer.parseInt(row_countStr);
            i++;
            // get m
            String col_countStr = "";
            while (!arrSplit[i].equals(":")) {
                col_countStr += arrSplit[i];
                i++;
            }
            col_count = Integer.parseInt(col_countStr);
            i++;
            // get startValue
            String startValueStr = "";
            while (!arrSplit[i].equals(":")) {
                startValueStr += arrSplit[i];
                i++;
            }
            int startValue = Integer.parseInt(startValueStr);
            i++;
            // get endValue
            String endValueStr = "";
            while (!arrSplit[i].equals(":")) {
                endValueStr += arrSplit[i];
                i++;
            }
            int endValue = Integer.parseInt(endValueStr);
            i++;

            // create grid from cell_openness_list
            grid = new Node[row_count][col_count];

            int rowIndex = 0;
            int columnIndex = 0;
            int value = 1;
            int cellOpenness;
            for (int j = i; j < arrSplit.length; j++) {
                cellOpenness = Integer.parseInt(arrSplit[j]);
                Node tempNode = new Node(value, cellOpenness);
                // check if node is starting_node or finishing_node
                if (value == startValue) {
                    starting_node = tempNode;
                } else if (value == endValue) {
                    finishing_node = tempNode;
                }
                grid[rowIndex][columnIndex] = tempNode;
                columnIndex++;
                if ((columnIndex + 1) % col_count == 1) {
                    rowIndex++;
                    columnIndex = 0;
                }
                value++;
            }
        } catch (Exception e) {
            System.out.println("Error: Input file has incorrect format");
            System.out.println(e);
            System.exit(1);
        }

    }

    public static void saveSolutionFile(String output) {
        String bfs_line;
        String dfs_line;
        //Format the output as per assignment spec
        bfs_line = bfs_solution_steps + " : " + bfs_solution;
        dfs_line = dfs_solution_steps + " : " + dfs_solution;
        //Create the maze file
        try {
            File file = new File(output);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write the output to the file
        try {
            FileWriter myWriter = new FileWriter(output);
            myWriter.write(bfs_line);
            myWriter.write(dfs_line);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
