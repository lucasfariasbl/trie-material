package com.project.trie;

import java.util.Scanner;

public class Main {
  public static void main(String args[]) {
    Scanner sc = new Scanner(System.in);

    Trie trie = new Trie();

    while (true) {

      System.out.println("A: Adicione uma palavra\nB: Remova uma palavra\nC: Pesquise por um prefixo\nZ: Sair\n");
      
      System.out.print("Digite sua escolha: ");
      
      String input = sc.nextLine().toUpperCase();

      if (input.equals("Z")) break;

      switch (input) {
        case "A":
          System.out.print("Digite a palavra a ser adicionada: ");
          trie.add(sc.nextLine());
          System.out.println("");
          break;

        case "B":
          System.out.print("Digite a palavra a ser removida: ");
          trie.remove(sc.nextLine());
          System.out.println("");
          break;
      
        case "C":
          System.out.print("Digite o prefixo: ");
          System.out.println(trie.findWordWithPrefix(sc.nextLine()));
          System.out.println("");
          break;
      }
    }
  }
}
