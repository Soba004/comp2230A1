
/* Name: William Stewart
 * Student Number: c3282367
 * File: MazeGenerator.java
 * Description: 
 * Generates a maze and saves to a file
 * Must input amount of row_count and col_count
 */
import java.util.Random;
import java.util.Stack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MazeGenerator {
    //All static potential state values
    private static final int both_closed = 0;
    private static final int right_only = 1;
    private static final int bottom_only = 2;
    private static final int both_open = 3;
    private static int row_count;
    private static int col_count;
    private static int num_nodes;
    private static int nodes_visited = 0;
    private static String cell_connectivity_list = "";
    private static Stack<Node> stack;
    static Random rand = new Random();
    //All nodes
    private static Node[][] grid;
    private static Node starting_node;
    private static Node finishing_node;

    public static void main(String[] args) {
        //Read the arg for inputted size - row_count + col_count
        try {
            row_count = Integer.parseInt(args[0]);
            col_count = Integer.parseInt(args[1]);
            if (row_count < 1 || col_count < 1) {
                System.out.println("ERROR: ROW AND COLUMN VALUE MUST BE > 1");
                System.exit(1);
            }
            if (row_count * col_count < 2) {
                System.out.println("ERROR: MUST BE MORE THAN 1 CELL");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("ERROR: ARGUMENT MUST BE AN INTEGER");
            System.exit(1);
        }

        //Set the file name of the maze
        String file_name = "";
        try {
            file_name = args[2];
        } catch (Exception e) {
            System.out.println("ERROR: PLEASE SPECIFY A FILE NAME");
            System.exit(-1);
        }
        
        //Generate a stack which will store nodes
        stack = new Stack<Node>();
        //Generate the total nodes from provided values
        num_nodes = row_count * col_count;
        //Run the generate node method 
        generateNodes();
        connectNodes();
        generateMaze();
        //If the maze is less than 5*5 we will display it
       // if (row_count * col_count <= 25) {
       //     displayMaze();
       // }
        //Get cell connectivity
        getCellConnectivity();
        //Save the maze
        saveMazeFile(file_name);
    }

    public static void getCellConnectivity() {
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                cell_connectivity_list += grid[i][j].getCellOpenness();
            }
        }
    }

    // method that picks a random node as the start and then selects a random path
    public static void generateMaze() {
        // get a random starting point
        int startX = rand.nextInt(row_count);
        int startY = rand.nextInt(col_count);
        starting_node = grid[startX][startY];
        // walk a random path from the start node
        generateCell(starting_node);
        // once random path is completed backtrack to visit the remaining unvisited
        // nodes
        while (!stack.isEmpty()) {
            backtrack();
        }
    }

    public static void generateCell(Node cell) {
        if (cell.getVisited() == false) {
            nodes_visited++;
            // check if node visiting is the last node to be visited
            if (nodes_visited == num_nodes) {
                finishing_node = cell;
            }
        }
        cell.setVisited(true);

        if (cell.hasUnvisitedNeighbor()) {
            // add node to stack to be revisted when backtracking
            stack.push(cell);
            Node randomNeighbor = cell.getRandomUnvistedNeighbor();
            cell.calculateCellOpenness(cell, randomNeighbor);
            // call generateCell with the randomNeighbor
            generateCell(randomNeighbor);
        }
    }

    public static void backtrack() {
        // visit nodes that had more than one neighbor
        if (!stack.isEmpty()) {
            generateCell(stack.pop());
        }
    }

    // method that will connect the nodes to each other based on their location on
    // the grid
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

    public static void generateNodes() {
        // generates a multidimensional array of Nodes
        grid = new Node[row_count][col_count];
        int count = 1;
        // instantiates a new node for each cell in the grid
        for (int i = 0; i < row_count; i++) {
            for (int j = 0; j < col_count; j++) {
                grid[i][j] = new Node(count);
                count++;
            }
        }
    }

    

    public static void saveMazeFile(String file_name) {
        String line;
        //Format the output as per assignment spec
        line = row_count + ":" + col_count + ":" + starting_node.getValue() + ":" + finishing_node.getValue() + ":" + cell_connectivity_list;
        //Create the maze file
        try {
            File file = new File(file_name);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write the output to the file
        try {
            FileWriter myWriter = new FileWriter(file_name);
            myWriter.write(line);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
