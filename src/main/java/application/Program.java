package application;

import models.FileParser;
import models.TxtParser;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        FileParser txtParser = new TxtParser();
        Scanner sc = new Scanner(System.in);

        System.out.print("Type a directory: ");
        String directory = sc.nextLine().trim();

        txtParser = txtParser.getFileParser(directory);

        System.out.println(txtParser);
    }
}
