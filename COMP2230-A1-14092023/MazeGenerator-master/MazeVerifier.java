import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;


public class MazeVerifier {

    //All static potential state values
    private static final int both_closed = 0;
    private static final int right_open = 1;
    private static final int bottom_open = 2;
    private static final int both_open = 3;
    private static String row_count_str = "";
    private static String col_count_str = "";
    private static String starting_node_str = "";
    private static String finishing_node_str = "";
    private static String cell_connectivity_list = "";
    private static Integer row_count;
    private static Integer col_count;
    private static Integer starting_node;
    private static Integer finishing_node;

    //All nodes
    private static Node grid[][];


    public static void main(String[] args) {
        // Scan the maze file
        String input = "";
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("ERROR: ARGUMENT MUST BE A MAZE FILE");
            System.exit(1);
        }

        String bfs_path ="";
        String dfs_path = "";
        try {
            Scanner scanner = new Scanner(new File(args[1]));
            bfs_path = scanner.nextLine();
            dfs_path = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("ERROR: ARGUMENT MUST BE A SOLUTION FILE");
            System.exit(1);
        }
        
        //We need to create the grid and connect the nodes based off of the input maze file
        //We need this to be able to display the solutions
        createGrid(input);
        connectNodes();

        mazeVerifier(input);
        solutionVerifier(bfs_path);
        solutionVerifier(dfs_path);

        //display mazes and solutions here
        
    }
    
public static void mazeVerifier(String input){
    String mazeInput = input;
    //Split out the maze values into strings and convert them so we can use them, i am aware this is ugly/inefficient way of doing it
    String split[] = mazeInput.split(":");
    String row_count_str = split[0];
    String col_count_str = split[1];
    String starting_node_str = split[2];
    String finishing_node_str = split[3];
    String cell_connectivity_list = split[4];
    Integer row_count = Integer.valueOf(row_count_str);
    Integer col_count = Integer.valueOf(col_count_str);
    //Integer starting_node = Integer.valueOf(starting_node_str);
    //Integer finishing_node = Integer.valueOf(finishing_node_str);

    //case 1: loop over cells to check - should be 0
   //case 2: loop over cells to check - should be 0
   //case 3: can use DFS to visit all nodes in the maze - should be no
   //case 4: can use DFS to visit all nodes in the maze - should be yes

}

public static void solutionVerifier(String solution){
    boolean valid = false;

    //verify whether path from starting cell is through valid pathways, not breaking any maze walls and not getting stuck at deadend and reached finishing cell
    //for an invalid solution, display the partial path up to the node travelled to

    if(valid==true){
        System.out.println("valid");
    }
    else{
        System.out.println("invalid");
    }
    if (row_count * col_count <= 25) {
            displaySolutionOnMaze();
        }
}


    public static void displayBFSPathSolution(String bfs_path) {
        System.out.println(bfs_path);
    }

 public static void displayDFSPathSolution(String dfs_path) {
        System.out.println(dfs_path);
    }

    //This method displays the solution on the maze
    public static void displaySolutionOnMaze() {

        // display top border
        String horizontalBorder = "-";
        for (int i = 0; i < col_count; i++) {
            horizontalBorder += "---";
        }
        System.out.println(horizontalBorder);
        // display body
        for (int i = 0; i < row_count; i++) {
            String line1 = "";
            String line2 = "";
            for (int j = 0; j < col_count; j++) {
                Node cell = grid[i][j];
                // add left horizontal wall
                if (cell.getLeft() == null) {
                    line1 += "|";
                    line2 += "|";
                }
                if (cell == starting_node) {
                    line1 += "S";
                } else if (cell == finishing_node) {
                    line1 += "F";
                } else if (cell.getCorrectStep() == true) {
                    line1 += "*";
                } else {
                    line1 += " ";
                }
                line1 += " ";
                if (cell.getCellOpenness() == both_closed) {
                    line1 += "|";
                    line2 += "--|";
                } else if (cell.getCellOpenness() == bottom_open) {
                    line1 += "|";
                    line2 += "  |";
                } else if (cell.getCellOpenness() == right_open) {
                    line1 += " ";
                    line2 += "--|";
                } else if (cell.getCellOpenness() == both_open) {
                    line1 += " ";
                    line2 += "  |";
                }

            }
            System.out.println(line1);
            if (i == row_count - 1) {
                System.out.println(horizontalBorder);
            } else {
                System.out.println(line2);
            }
        }
    }

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
}
