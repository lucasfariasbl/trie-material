package com.project.trie;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrieTest {

  private Trie trie;

  @BeforeEach
  void setUp() {
    this.trie = new Trie();
  }
  
  @Test
  void testInsercaoDePalavras() {
    trie.add("Casa");

    assertTrue(trie.search("Casa"));
    assertTrue(trie.search("CASA"));
    assertTrue(trie.search("casa"));
    assertTrue(trie.search("CaSa"));
  
    assertFalse(trie.search("Cas"));

    assertFalse(trie.search("Teclado"));

    trie.add("teclado");

    assertTrue(trie.search("Teclado"));

    assertFalse(trie.search("monitor"));
    assertFalse(trie.search("palheta"));
    assertFalse(trie.search("ukulele"));
  }

  @Test
  void testInsercaoPalavraSendoPrefixoDeOutra() {
    trie.add("Carrossel");

    assertTrue(trie.search("Carrossel"));
    assertFalse(trie.search("Carro"));

    trie.add("Carro");

    assertTrue(trie.search("Carro"));

    trie.remove("Carrossel");

    assertFalse(trie.search("Carrossel"));
  }

  @Test
  void testPrefixos() {
    trie.add("Telesena");
    trie.add("Teleferico");
    trie.add("Telescopio");
    trie.add("Televisao");

    assertTrue(trie.startsWith("Tele"));
    assertTrue(trie.startsWith("tele"));
    assertTrue(trie.startsWith("TELE"));
    assertTrue(trie.startsWith("t"));
    assertTrue(trie.startsWith("Telev"));
    assertTrue(trie.startsWith("telef"));
    assertTrue(trie.startsWith("tel"));
    assertTrue(trie.startsWith("telesc"));

    assertFalse(trie.startsWith("Comp"));
    assertFalse(trie.startsWith("Arq"));
    assertFalse(trie.startsWith("Proj"));
    assertFalse(trie.startsWith("Arv"));
  }

  @Test
  void testLocalizaPalavrasComPrefixo() {
    trie.add("Carro");
    trie.add("Carteiro");
    trie.add("Carta");
    trie.add("Carrossel");
    trie.add("Carreta");
    trie.add("Caro");
    trie.add("Carrinho");
    trie.add("Carroca");
    trie.add("Camomila");

    List<String> esperado = List.of("carro", "carteiro", "carta", "carrossel", "carreta", "caro", "carrinho", "carroca");
    List<String> resultado = trie.findWordWithPrefix("Car");

    assertEquals(esperado.size(), resultado.size());
    assertTrue(resultado.containsAll(esperado));

  }

  @Test
  void testLocalizaTodasAsPalavras() {
    trie.add("a");
    trie.add("b");
    trie.add("c");
    trie.add("d");
    trie.add("e");
    trie.add("f");
    trie.add("g");
    trie.add("h");
    trie.add("i");
    trie.add("j");
    trie.add("k");
  
    assertEquals(11, trie.findWordWithPrefix("").size());
  }
  

  @Test
  void testRemocaoDePalavras() {
    trie.add("Monitoramento");

    assertTrue(trie.search("Monitoramento"));
    
    assertFalse(trie.search("Monitor"));

    trie.add("Monitor");

    assertTrue(trie.search("Monitor"));

    trie.remove("Monitor");

    assertFalse(trie.search("Monitor"));
    
    assertTrue(trie.search("Monitoramento"));

    trie.remove("Monitoramento");

    assertFalse(trie.search("Monitoramento"));
  }
}
