package com.project.trie;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Implementação da estrutura de dados Trie (Árvore de prefixos) com a finalidade de obter eficiência 
 * na busca e armazenamento de palavras.
 *
 * Esta implementação considera a adição e pesquisa apenas de palavras com letras minúsculas.
 */
public class Trie {

  private final Node root;

  public Trie() {
    this.root = new Node();
  }

  /**
   * Insere uma palavra na árvore, caso alguma letra não tenha sido inserida ainda, ele cria um nó que a representa
   * e insere na mesma, e ao chegar na ultima letra da palavra, seu nó é marcado como fim de uma palavra.
   *
   * @param word A palavra a ser inserida.
   */
  public void add(String word) {
    word = word.toLowerCase();

    Node nodeAux = this.root;

    for (int i = 0; i < word.length(); i++) {
      Node son = nodeAux.getSons().get(word.charAt(i));

      if (son != null) {
        nodeAux = son;

      } else {
        Node newNode = new Node();
        nodeAux.getSons().put(word.charAt(i), newNode);
        nodeAux = newNode;
      }
    }

    nodeAux.setEndOfWord();
  }

  /**
   * Pesquisa se certa palavra está inserida na árvore, caso todas as suas letras estiverem lá e 
   * sua ultima letra esteja marcada como fim de uma palavra, ela está.
   *
   * @param word A palavra a ser pesquisada.
   * @return {@code true} Se a palavra existe, {@code false} caso contrário.
   */
  public boolean search(String word) {
    word = word.toLowerCase();

    Node nodeAux = this.root;

    for (int i = 0; i < word.length(); i++) {
      Node son = nodeAux.getSons().get(word.charAt(i));

      if (son == null) return false;

      nodeAux = son;
    }

    return nodeAux.isEndOfWord();
  }

  /**
   * Busca se existe alguma palavra que cotenha certo prefixo.
   *
   * @param prefix O prefixo a ser analisado.
   * @return {@code true} Caso exista alguma palavra com aquele prefixo, {@code false} caso contrário.
   */
  public boolean startsWith(String prefix) {
    prefix = prefix.toLowerCase();

    Node nodeAux = this.root;

    for (int i = 0; i < prefix.length(); i++) {
      Node son = nodeAux.getSons().get(prefix.charAt(i));

      if (son == null) return false;

      nodeAux = son;
    }

    return true;
  }

  /**
   * Remove uma palavra da árvore, caso ela esteja lá.
   *
   * Conta com a remoção natural que desmarca o atributo de fim de uma palavra da sua ultima letra
   * e a remoção física que remove o nó que representa as letras da palavra caso não interfira na 
   * estrutura de outra.
   *
   * @param word A palavra a ser removida.
   */
  public void remove(String word) {
    word = word.toLowerCase();

    remove(word, this.root, 0);
  }

  /**
   * Método privado auxiliar da remoção da palavra.
   *
   * @param word A palavra a ser removida.
   * @param node O nó da letra atual.
   * @param index O índice necessário para percorrer a palavra recursivamente.
   *
   * @return {@code true} Caso o nó pai tenha autorização de remover o nó do filho e não interfira em outras
   * palavras, {@code false} caso interfira, assim o nó não é removido.
   */
  private boolean remove(String word, Node node, int index) {
    Node son = node.getSons().get(word.charAt(index));

    if (son == null) return false;

    if (index == word.length() - 1) {
      son.turnOffEndOfWord();
      return son.getSons().isEmpty();

    } else {
      boolean canRemove = remove(word, son, index + 1);
      if (canRemove) node.getSons().remove(word.charAt(index));
      return node.getSons().isEmpty() && !node.isEndOfWord();
    }
  }

  /**
   * Encontra e retorna todas as palavras que contenham um prefixo específico.
   *
   * @param prefix o prefixo a ser analisado.
   *
   * @return Uma lista de strings com todas as palavras que contenham o prefixo.
   */
  public ArrayList<String> findWordWithPrefix(String prefix) {
    prefix = prefix.toLowerCase();

    ArrayList<String> words = new ArrayList<>();
    Node nodeAux = this.root;

    for (int i = 0; i < prefix.length(); i++) {
      Node son = nodeAux.getSons().get(prefix.charAt(i));

      if (son == null) return words;

      nodeAux = son;
    }

    return catchWords(nodeAux, prefix, words);
  }

  /**
   * Método auxiliar privado que utiliza da estratégia DFS (Depth-First Search) para percorrer 
   * todas as palavras a partir da ultima letra do prefixo.
   *
   * @param currentNode o nó da letra atual.
   * @param currentPrefix o prefixo atual.
   * @param currentWords a lista com todas as palavras com o prefixo até o atual momento.
   *
   * @return A lista com todas as palavras encontradas.
   */
  private ArrayList<String> catchWords(Node currentNode, String currentPrefix, ArrayList<String> currentWords) {
    if (currentNode.isEndOfWord()) currentWords.add(currentPrefix);

    for (Map.Entry<Character, Node> entry : currentNode.getSons().entrySet()) {
      String nextPrefix = currentPrefix + entry.getKey();
      Node nextNode = entry.getValue();
      catchWords(nextNode, nextPrefix, currentWords);
    }

    return currentWords;
  }

  /**
   * A classe dos nós a serem utilizados para a representação de cada letra adicionada na árvore.
   */
  public class Node {

    boolean wordEnd;
    HashMap<Character, Node> sons;

    public Node() {
      this.wordEnd = false;
      this.sons = new HashMap<>();
    }

    /**
     * Retorna o mapa com todos os nós filhos do nó.
     *
     * @return Um mapa com todos os nós filhos do nó.
     */
    public HashMap<Character, Node> getSons() {
      return this.sons;
    }

    /**
     * Verifica se o nó é o fim de uma palavra.
     * 
     * @return {@code true} Caso o nó seja o fim de uma palavra, {@code false} caso contrário.
     */
    public boolean isEndOfWord() {
      return this.wordEnd;
    }

    /**
     * Seta o nó como fim de uma palavra.
     */
    public void setEndOfWord() {
      this.wordEnd = true;
    }

    /**
     * Desativa o atributo que torna o nó fim de uma palavra.
     */
    public void turnOffEndOfWord() {
      this.wordEnd = false;
    }
  }
}
