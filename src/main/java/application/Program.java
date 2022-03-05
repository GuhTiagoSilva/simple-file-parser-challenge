package application;

import models.FileParser;
import models.TxtParser;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        FileParser txtParser = new TxtParser();
        Scanner sc = new Scanner(System.in);

        System.out.print("Insert a directory to monitor: ");
        String directory = sc.nextLine().trim();

        txtParser = txtParser.analyzeFile(directory);

        System.out.println(txtParser.getStatistics());
    }
}
