package com.yegor;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Random r = new Random();

        Board player = new Board();
        Board pc = new Board();

        pc.deployRandom();
        player.deployRandom();

        player.drawToPlayer();
        System.out.println();
        pc.drawToEnemy(player);


        battle(player, pc, s, r);
    }

    private static void battle(Board player, Board pc, Scanner s, Random r) {
        while (pc.getLives() > 0 || player.getLives() > 0) {
            try {
                while (player.getShipsLength() < 5) {
                    System.out.println("Enter in order: 1. Row; 2. Col; 3. Size of the ship; 4. True if vertical, False if horizontal;");
                    player.addPlayerShip(s.nextInt(), s.nextInt(), s.nextInt(), s.nextBoolean());
                    player.drawToPlayer();
                }
                System.out.println("Enter the row you wish to attack: ");
                int row = s.nextInt();
                if (row > 9) {
                    System.out.println("Pls enter valid value!");
                    continue;
                }
                System.out.println("Enter the col you wish to attack: ");
                int col = s.nextInt();
                if (col > 9) {
                    System.out.println("Pls enter valid value!");
                    continue;
                }
                if (player.attack(pc, row, col)) {
                    System.out.println("Nice shot, keep going");
                    pc.drawToEnemy(pc);
                    continue;
                }
                pc.attack(player, r.nextInt(10), r.nextInt(10));
                player.drawToPlayer();
                System.out.println();
                pc.drawToEnemy(pc);
            } catch (InputMismatchException e) {
                System.out.println("Input error, all input values should be integers!");
                //TODO: Loop restart after mismatch exception was caught
            }
        }
    }
}

