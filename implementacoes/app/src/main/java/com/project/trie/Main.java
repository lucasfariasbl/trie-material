package com.project.trie;

import java.util.Scanner;

public class Main {
  public static void main(String args[]) {
    Scanner sc = new Scanner(System.in);

    Trie trie = new Trie();

    while (true) {

      System.out.println("\nA: Adicione uma palavra\nB: Remova uma palavra\nC: Pesquise por um prefixo\nZ: Sair\n");
      
      System.out.print("Digite sua escolha: ");
      
      String input = sc.nextLine().toUpperCase();

      if (input.equals("Z")) { 
        System.out.println("");
        break;
      }

      switch (input) {
        case "A":
          System.out.print("\nDigite a palavra a ser adicionada: ");
          trie.add(sc.nextLine());
          break;

        case "B":
          System.out.print("\nDigite a palavra a ser removida: ");
          trie.remove(sc.nextLine());
          break;
      
        case "C":
          System.out.print("\nDigite o prefixo: ");
          String prefix = sc.nextLine();
          System.out.println("");
          System.out.println(trie.findWordWithPrefix(prefix));
          break;
      }
    }
  }
}
