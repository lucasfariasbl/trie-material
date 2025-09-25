# 1 Motivação
Imagina o autocompletar do seu celular, ao digitar "comp", magicamente aparecem opções como "computação", "computador", "compilador". Mas como é possível fazer isso de forma tão rápida, mesmo com uma inmensidão de palavras?

Se tentassemos resolver esse problema com estruturas de dados comuns, como:
- **Listas ou array:** somos forçados a fazer uma busca exaustiva, olhar cada palavra, uma por uma, e perguntar "você começa com 'comp'?", o que é terrivelmente lento.
- **Tabela hash:** ela transforma uma palavra inteira em um código numérico para uma busca instantanea. O problema é que esse processo destrói a informação do prefixo. O código de "computador" não tem relação nenhuma com o de "componente". Portanto, somos forçados a pegar cada chave da tabela e verificar cada uma individualmente.

É exatamente para isso que a Trie foi criada, ela é perfeita para problemas em dados sequênciais, para aqueles que envolvem operações com prefixos.

Então, se as estruturas clássicas não nos ajuda nesse caso, qual é a mágica? A magia da trie é invés de encarar as sequências como um bloco único, "computador" por exemplo, ele olha como um caminho `c -> o -> m -> p -> ...`. A busca vira um passeio pela árvore.
# 2 Visão Geral em Vídeo: Trie em 3 Minutos 
<div align="center">
 <video controls src="https://raw.githubusercontent.com/NotAdson/trie-material/main/assets/video%20trie.mp4" style="max-width: 100%;">
</video> 
</div>


# 3 Introdução
## 3.1 O que é uma Trie?
Também conhecida como árvore de prefixos, é uma estrutura de dados usada para armazenar sequências de forma dinâmica, facilitando operações com prefixos. Algo interessante sobre a Trie, é que os nós não armazenam a chave, mas o caminho da raiz até um nó representa a chave(ou prefixo).
## 3.2 Visualizando
Vamos ver como as palavras "galo", "gato", e "gado" seriam armazenadas em uma Trie:

<p align="center"><img src="assets/introduction_visualization_trie.png" height="400"></p>

A estrutura de uma Trie é composta por:
1. **Raiz:** ele não representa nenhum valor do alfabeto e serve como o ancestral comum de todas as palavras.
2. **Arestas e nós:** cada nó pode ter vários filhos, e representa um possível caminho de palavra. Por exemplo, a letra _a_ pode levar para _l_, _t_, ou _d_.
3. **Compartilhamento de prefixo:** observe que as três palavras compartilham do prefixo "ga", invés de armazenar ele 3 vezes, ela armazena apenas um.
4. **Marcador de fim:** como sabemos que a palavra "galo" é uma palavra mas "gal" é um prefixo? Adicionando um marcador booleano no final de cada palavra.
# 3 Implementação
## 3.1 Estruturas fundamentais
### 3.1.1 Node
Os nós na implementação de uma Trie, assim como em Linked Lists ou outra estrutura que os utilize, são as peças do quebra-cabeça. Sem eles, não existe implementação.
#### 3.1.1.1 Atributos
A primeira vista pode ser estranho e incomum, mas o nó não armazena o valor que representa dentro da estrutura da Trie, pois aqui, não temos um nó que se linka com outro através de apontadores como "left" ou "right", os nós armazenam algo mais interessante...

**HashMap:** 

Para fins didáticos, nossa implementação aborda a manipulação de palavras na Trie e as armazena em um HashMap. Caso você pare um pouco para pensar, verá que de acordo com nossa abordagem, o HashMap guardará no máximo 26 pares de <Chave, Valor>, onde a chave é cada letra do alfabeto, e seu valor é o nó que o representa. Desta forma, podemos pesquisar por várias palavras que contenham o mesmo prefixo com a mesma eficiência.

**Indicador de fim de palavra:**

Este atributo é extremamente importante para o funcionamento da Trie por definir se uma palavra está presente na  estrutura ou não. Ele é um atributo booleano que define se certa letra representa o fim de uma palavra dentro da Trie. 

- **Exemplo:**
> Digamos que adicionamos a palavra "Carrossel".
>
>Nossa estrutura ficaria assim: ROOT -> C -> A -> R -> R -> O -> S -> S -> E -> <b>L</b>
>
> Podemos pesquisá-la através do método search que veremos adiante, e o retorno será **true**, pois ela foi inserida. Agora caso pesquisarmos pela palavra "Carro", comumente se pensaria que ela está ao visualizar a estrutura que temos, porém ela nunca foi adicionada. Para isso que temos nosso atributo mágico "wordEnd", que define se certo nó que representa uma letra n também se refere ao fim de uma palavra. Nosso algoritmo olharia para o nó que representa a letra "o" da **nossa estrutura** e se perguntaria "Esta letra é o fim de uma palavra?". E no nosso caso, essa resposta seria **false**.

- Veja a implementação da classe Node (Atributos e construtor):

```java
 public class Node {

    boolean wordEnd;
    HashMap<Character, Node> sons;

    public Node() {
      this.wordEnd = false;
      this.sons = new HashMap<>();
    }
}
```

Para a realização das operações, ela conta com os seguintes métodos:

- getSons(): Retorna o mapa de filhos do nó.
- isEndOfWord(): Retorna o valor do atributo wordEnd.
- setEndOfWord(): Torna true o valor do atributo wordEnd.
- turnOffEndOfWord(): Torna false o valor do atributo wordEnd.

### 3.1.2 Trie
A classe Trie é o maestro da orquestra, ela coordena e controla todos os métodos para formar a estrutura, se formos encaixar na metafora do quebra-cabeça, a Trie é quem o monta. Ela conta apenas com um único atributo estático nomeado "root", que não corresponde à caractere algum. 

Todos os nós partem do root.

- Veja abaixo a parte inicial do código da classe Trie:

```java
public class Trie {

  private final Node root;

  public Trie() {
    this.root = new Node();
  }
}
```

## 3.2 Operações

A maioria dos métodos que veremos são bem intuitivos e nada complexos de serem compreendidos, vamos à leitura.
### 3.2.1 Inserção

A inserção de uma palavra na Trie, ocorre caractere por caractere. onde no fim, o nó que representa a última letra da palavra terá seu atributo "endWord" setado como true.

**Vamos exemplificar com a adição da palavra "Material":**

A ideia é iterar sobre a palavra de tal forma que adicionemos letra por letra.

Primeiro, criamos um nó auxiliar partindo do nó root (Node nodeAux). Como sabemos, cada nó tem um HashMap com as letras que partem dele. Dado isso, verificamos se no HashMap de nodeAux, com sua chave sendo a primeira letra da palavra, (m), o valor resultante é um nó ou não. Caso o valor retornado seja null, isso significa que, já que estamos partindo do root, não há nenhuma palavra que inicie com a letra m na estrutura, pois não há um nó no valor afiliado à ela. Sendo assim, temos que criar um novo nó e associá-lo. Desta forma, agora temos um nó representando aquele caractere no root.

Para exemplificar melhor, vamos à um universo paralelo onde a palavra "Marca" já foi adicionada e queremos adicionar "Material". Ao iniciar do root, veremos que em seu HashMap, a chave "m" resulta em um nó, que por sua vez, em seu HashMap, a chave "a" retorna um nó, mas que agora, por sua vez, a chave "t" retorna um valor null, diferente se se verificarmos a chave "r" da palavra "Marca". Portanto, seria necessário instanciar um novo nó e adicioná-lo ao valor da chave "t".

Voltando ao nosso universo canônico, temos a atualização de nodeAux. Como verificamos se a primeira letra "existia" na estrutura, e caso ainda não existisse, adicionamos-a, nodeAux agora será o nó que representa a primeira letra, e agora vamos verificar a existência de um nó que represente a segunda, (a). Veja que é como se tivéssemos dado 1 passo de nó para nó. Estávamos em root, e agora pulamos para o nó da primeira letra. A partir de agora, o processo se repete, verificando se a próxima letra como chave tem algum nó filho instanciado no HashMap do nó atual, se sim, apenas é dado um nodeAux = son, caso contrário, como visto antes, o nó é criado e associado à letra.

Nossa última etapa ocorre após a verificação e adição da última letra, pois estando com ela nas mãos, temos que setar como true o atributo de seu nó que indica que ela é o fim de uma palavra. Após isso, a adição foi concluída com sucesso. 

 - Para melhor compreensão, segue a implementação do método:

```java
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
```

### 3.2.2 Pesquisa
O método de pesquisa tem como parâmetro a palavra a ser pesquisada na árvore e retorna um valor booleano baseado na pergunta "A palavra está na árvore?"

Assim como em todos os métodos, partimos do nó root, só que diferente da adição, nossas ações baseadas nas verificações são mais simples.

Da mesma forma que o método da inserção de palavras, temos que analisar se no HashMap do nó atual, temos uma referência associada à chave da próxima letra, um nó, ou se temos o valor null. 

Caso o retorno da chave seja null, isto indica que não temos um nó referenciando a letra que estamos verificando na vez, portanto, ela nunca foi adicionada. Neste momento, podemos e devemos retornar o valor **false** para o método, indicando que a palavra não está na estrutura.

```java
public boolean search(String word) {
    word = word.toLowerCase();

    Node nodeAux = this.root;

    for (int i = 0; i < word.length(); i++) {
      Node son = nodeAux.getSons().get(word.charAt(i));

      if (son == null) return false;
```

Caso contrário, ou seja, o valor de retorno seja um nó, passamos a verificar seu HashMap à procura da próxima letra, e assim em diante.

```java
      nodeAux = son;
```

Daí pode-se pensar que é basicamente isso, dado que caso todas as letras estejam presentes na estrutura obviamente a palavra inteira está presente. Porém, vamos a um exemplo prático:

**Supomos que adicionamos a palavra "Carrossel" e pesquisamos a existência de "Carro":**

ROOT -> C -> A -> R -> R -> O -> S -> S -> E -> <b>L</b>

> Podemos visualizar através da explicação do material, que a partir do nó root, será validada a existência da letra "c", logo após, em seu nó, a existência da letra "a", e assim em diante até chegarmos na verificação da existência da letra "o". Acabou, né? Confirmamos que a palavra "Carro" está presente e podemos retornar **true**...

> É isso que a estrutura apresentada indica, mas nós nunca digitamos o comando add("Carro"), correto? Para isso, utilizamos o atributo que já foi mencionado no material anteriormente, que indica se certa letra representa o fim de uma palavra.
> Isso seria, obviamente, testado na última letra da palavra pesquisada, no nosso caso, o "o", retornando o valor booleano **false**.

```java
    return nodeAux.isEndOfWord();
```

- Para melhor compreensão, segue a implementação completa do método:

```java
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
```

### 3.2.3 Prefixos

A jornada que o método que pesquisa por um prefixo percorre é idêntica ao search que vimos anteriormente, alterando apenas 1 detalhe que simplifica o código. Ou seja, se você sabe implementar o search(), sabe implementar o startsWith(). 

O método recebe como parâmetro um prefixo e retornará um valor booleano referente à existência de qualquer palavra que contenha aquele prefixo.

Assim como no search(), o startsWith() percorrerá letra por letra da palavra verificando se ela existe no mapa de seu nó parent, caso a letra não tenha valor referente, o retorno é **false**, caso contrário chegamos no ponto que o diferencia do search().

```java
public boolean startsWith(String prefix) {
    prefix = prefix.toLowerCase();

    Node nodeAux = this.root;

    for (int i = 0; i < prefix.length(); i++) {
      Node son = nodeAux.getSons().get(prefix.charAt(i));

      if (son == null) return false;
```

Ao chegar na última letra, como estamos tratando de prefixos e não de palavras completas, é irrelevante a verificação isEndOfWord() do nó, portanto, se ao longo do loop o caminho não quebrar e chegar ao fim, apenas retornamos **true**.

```java
    return true;
```

- Segue a implementação completa:

```java
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
```
### 3.2.4 Remoção 

O método da remoção de uma palavra da Trie segue uma sequência de fatos e conta com 2 partes em sua remoção: 

-  **Remoção lógica:** Desliga o atributo endWord do nó referente a última letra da palavra
-  **Remoção física:** Através de uma verificação consegue saber quando está autorizado a remover completamente a referência ao caractere.

Veremos com mais detalhes os momentos onde as duas partes ocorrem a seguir, mas antes temos que nos inserir nos passos do método de remoção em ordem cronológica.

**Parte 1: Descida**

Inicialmente, temos que descer até o último caractere da palavra, mas, diferente da forma que iteramos por ela até agora, desta vez faremos esse passo recursivamente.

Chamaremos nosso método privado recursivo com 3 parâmetros:

- A palavra a ser removida
- O nó atual
- Índice atual

Sendo os 2 útimos primeiramente:

- this.root
- 0

```java
public void remove(String word) {
    word = word.toLowerCase();

    remove(word, this.root, 0);
  }
```

Nosso algoritmo, enquanto descemos, se baseará em pegar o nó referente a letra atual que estamos tratando e chamar recursivamente. Apenas isso. Simples, não é? Só precisamos substituir nos parâmetros o nó anterior pelo atual e incrementar no index.

**Parte 2: Remoção lógica**

Ao chamarmos o método recursivamente suficiente até o index na qual estamos ser igual ao tamanho da nossa palavra - 1, o que significa que estamos no nó da última letra, aplicamos a remoção lógica, que pode ser traduzida para a desativação do atributo de finalização de palavra do nó e retornamos a função com um valor booleano, que nos informa se o mapa dos filhos do nó é vazio, o que nos levará à nossa outra remoção.

**Parte 3: Subida e remoção física:**

Para subir de volta na árvore utilizaremos a parte dos retornos da função de remoção. 

Cada retorno da função é armazenado em um atributo booleano, que como dito antes, é baseado no fato do mapa de filhos do nó ser vazio ou não. Isso serve para nos indicar se o nó pai tem autorização de remover fisicamente o nó filho de seu mapa, o que não pode acontecer caso o filho tenha alguém referenciado no seu mapa de filhos para não quebrar a construção da árvore.

Caso o retorno tenha sido **true**, o nó remove de seu mapa o valor da chave atual, e após essa verificação, já que a partir de agora não temos certeza se estamos em um nó que é considerado fim de uma palavra, temos que retornar o valor booleano baseado em 2 coisas:

- O mapa do nó é vazio
- Ele não é fim de uma palavra

A partir desse retorno podemos com segurança avisar ao nó pai se ele pode remover fisicamente seu filho ou não.

Com esses passos subimos a árvore até terminar nossa pilha de execução, finalizando a função do método.

```java
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
```
## 3.3 Listagem de palavras por prefixos

A função deste método é nos retornar uma lista com todas as palavras que iniciam com o prefixo passado como parâmetro.

Assim como o método de remoção, trabalhamos com etapas para a realização do método:

- Descida até o fim do prefixo
- Adição de palavras através de DFS

**Parte 1: Descida**

Trivialmente, desceremos por iteração na árvore até o nó da última letra do prefixo, quebrando a execução e retornando uma lista vazia caso um nó não exista.

Ao chegarmos no fim, chamaremos nossa função auxiliar que será responsável por montar todas as palavras e adicioná-las na lista que será retornada ao fim do processo através da técnica Depth-First Search (DFS), que percorre uma árvore por profundidade.

```java
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
```

**Parte 2: Depth-First Search**

A partir do nó da última letra do prefixo, chamamos a função catchWords() que terá 3 parâmetros:

- O nó atual
- O prefixo atual
- A lista de palavras

A cada chamada do método recursivo, sua primeira verificação é se o nó de seu parâmetro é o fim de uma palavra, pois caso seja, o prefixo se trata de uma palavra completa e é adicionado à lista final.

Após a verificação inicial, utilizamos o DFS para percorrer cada ramo da árvore em profundidade concatenando os caracteres e chamando a função recursivamente. No fim do processo, a lista é retornada e temos, por fim, o resultado esperado

- Para melhor compreensão, segue a implementação do método privado:

```java
private ArrayList<String> catchWords(Node currentNode, String currentPrefix, ArrayList<String> currentWords) {
    if (currentNode.isEndOfWord()) currentWords.add(currentPrefix);

    for (Map.Entry<Character, Node> entry : currentNode.getSons().entrySet()) {
      String nextPrefix = currentPrefix + entry.getKey();
      Node nextNode = entry.getValue();
      catchWords(nextNode, nextPrefix, currentWords);
    }

    return currentWords;
  }
```
## 3.4 Análise de complexidade de tempo e memória

E finalmente chegamos ao motivo do por que a Trie é tão importante e tão famosa nas estruturas de dados de armazenamento. Sua extrema eficiência.

Pode-se pensar que para guardar, utilizando nossa implementação de exemplo, palavras, poderíamos simplesmente colocar todas em uma lista, e caso queiramos alguma, basta fazer uma iteração sobre ela, algo bem básico e cotidiano na programação. Mas em termos de eficiência, em uma escala mais profissional, isso seria muito custoso, dado que teríamos milhões de dados cadastrados, sendo assim em seu pior caso, O(n) | n = tamanho da lista.

A Trie vem pra contornar esse problema de uma forma bem curiosa... Reaproveitando elementos que já estão presentes na estrutura. A essa altura da leitura deste material você já deve ter se dado conta disso (e achado bem interessante). 

- **Exemplo:**

> Ao adicionarmos a palavra "Cama" na estrutura e logo após, "Camaleão" e "Camada", o prefixo "Cama" é reaproveitado, assim, fazendo parte de 3 palavras diferentes!

<div align="center">
 <img src="assets/operacoes_listagem_exemplo_cama.png" height="500">
</div>
Este reaproveitamento é a chave para a economia de memória e a performance da Trie.

Por conseguinte, os métodos de inserção, pesquisa de palavras, pesquisa por prefixos e remoção, são em seu pior caso, O(k) | k = tamanho da palavra passada como parâmetro, sendo extremamente eficiente.

O único método que se diferencia dessa regra é o de listagem das palavras a partir de certo prefixo, sendo em seu pior caso O(p + n) | p = o tamanho do prefixo ∧ n = a soma de todos os nós existentes a partir do prefixo.

# 4 Comparações
## 4.1 Trie x Hashtable

| Critério                         | **Trie**                                                                                                          | **Hash Table**                                                                                  |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| **Conceito**                     | Árvore de prefixos em que cada nó representa um caractere da chave, caminhos formam as palavras.                  | Estrutura baseada em *funções de hash* que mapeia chave -> valor em um array.                      |
| **Complexidade de Busca**        | **O(L)**, onde *L* é o tamanho da chave. Independente do número de chaves (n).                                    | **O(1)** em média, **O(n)** no pior caso (muitas colisões).                                     |
| **Complexidade de Inserção**     | **O(L)**                                                                                                          | **O(1)** em média, **O(n)** no pior caso.                                                       |
| **Complexidade de Remoção**      | **O(L)** (precisa checar se nós podem ser apagados).                                                              | **O(1)** em média; **O(n)** no pior caso.                                                       |
| **Uso de Memória**               | Geralmente **maior**, pois cada nó guarda ponteiros para o alfabeto. | Geralmente **menor**, já que cada chave é armazenada integralmente sem prefixos compartilhados. |
| **Compartilhamento de Prefixos** | **Sim**, prefixos comuns são armazenados uma única vez.                                                           | **Não**, cada chave é guardada inteira.                                                         |
| **Busca por Prefixo**            | **Excelente**, encontra rapidamente todas as chaves com determinado prefixo.                                      | **Ruim**, é necessário percorrer e filtrar todas as chaves.                                    |
| **Ordenação de Chaves**          | **Natural**, basta percorrer a árvore em ordem alfabética.                                                     | **Não existe**, é preciso copiar e ordenar à parte.                                            |
| **Flexibilidade de Chaves**      | Ideal para **strings** ou alfabetos finitos.                                         | Aceita **qualquer tipo de chave** com função hash bem definida.                                 |
| **Dependência de Função Hash**   | **Nenhuma**, não usa hashing.                                                                                     | **Total**, a qualidade da função hash é crítica para evitar colisões.                           |
| **Escalabilidade**               | Pode crescer muito em largura se o alfabeto for grande.                       | Escala bem, mas exige *rehash* quando a tabela é redimensionada.                                |
| **Implementação**                | Mais **complexa**, com múltiplos nós e ponteiros.                                                                 | Mais **simples**, e amplamente disponível em bibliotecas padrão.                                |
| **Melhor Cenário de Uso**        | Autocompletar, dicionários, busca ordenada, roteamento por prefixo.                                               | Consultas exatas e rápidas de chave→valor, cache, contadores, tabelas de símbolos.              |
| **Desvantagens**         | Alto consumo de memória, custo proporcional ao tamanho da chave.                                                  | Dependência da função hash e não suporta buscas por prefixo nem ordenação natural.              |

**Em resumo:**
Trie: percorre cada caractere da chave, com custo O(L) (L = tamanho da chave). Compartilha prefixos, facilita busca por prefixo, autocompletar e ordenação natural das chaves. Em contrapartida, consome mais memória e é mais complexa de implementar.

Hash Table: oferece buscas e inserções em O(1) em média, ideal para consultas exatas de chave -> valor. Tem implementação simples e uso de memória geralmente menor, mas depende fortemente de uma boa função hash e não lida bem com prefixos ou ordenação.

## 4.2 Comparação: Trie vs Árvore Balanceada

|  **Critério**                   |  **Trie**                                                                 |  **Árvore Balanceada (AVL, Red-Black, B-Tree etc.)**                         |
|----------------------------------|------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| **Estrutura**                    | Árvore em que cada **nó representa um caractere**                           | Árvore binária (ou n-ária) com balanceamento baseado em **comparação de chaves** |
| **Chave**                        | Armazenada **como caminho** entre raiz e folha                              | Armazenada **inteira** em cada nó                                              |
| **Ordem das chaves**             | ordem lexicográfica natural                                         | ordem definida por comparações                                         |
| **Busca por prefixo**            | Muito eficiente (O(P))                                                    |  Ineficiente – requer percorrer subárvore                                    |
| **Busca por chave completa**     | O(L) – L = comprimento da chave                                             | O(log N) – N = número de chaves                                                |
| **Inserção**                     | O(L)                                                                         | O(log N)                                                                        |
| **Remoção**                      | O(L)                                                                         | O(log N)                                                                        |
| **Eficiência com prefixos**      | Altíssima                                                                  |  Fraca                                                                        |
| **Uso de memória**               |  Maior – muitos nós (especialmente com alfabetos grandes)                 |  Mais eficiente – armazena chaves completas                                   |
| **Comparações entre chaves**     |  Não há comparações – só navegação por caracteres                          |  Baseada em comparação entre chaves inteiras                                  |
| **Desempenho em grandes alfabetos**|  Pior – pode ter muitos filhos por nó                                     |  Melhor – não depende do tamanho do alfabeto                                  |
| **Autocomplete e correção**      |  Suporte direto                                                             |  Precisa de lógica adicional                                                  |
| **Complexidade de implementação**|  Alta – manipulação de muitos ponteiros                                    |  Moderada – depende do tipo de árvore (AVL, Red-Black, etc.)                 |
| **Aplicações comuns**            | Dicionários, autocomplete, buscas por prefixo                               | Índices de bancos de dados, conjuntos ordenados, mapas ordenados               |

---

**Em resumo:** 
A Trie busca cada caractere da chave em O(L)  (no qual L é o tamanho da chave), compartilhando prefixos e sendo ótima para autocompletar e buscas por prefixo.
A BST armazena a chave inteira e busca em O(log n) (se balanceada), ideal para consultas ordenadas e intervalos, com menor consumo de memória quando não há muitos prefixos repetidos.
# 5 Variações e otimizações
## 5.1 Radix Tree
### 5.1.1 Definição
 
Uma Radix Tree (também chamada de Compact Trie ou Patrícia Tree) é uma estrutura de dados, baseada em nós, que armazena, geralmente, strings ou números de forma eficiente, especialmente quando apresentam prefixos em comum.

A Radix Tree se trata de uma versão otimizada da Trie, levando-se em consideração que, na Trie, cada nó armazena apenas uma letra de uma palavra. No entanto, a Radix Tree busca armazenar prefixos de palavras, pois, assim, a estrutura se torna mais eficiente para o uso de memória, além de diminuir a quantidade de ramos existentes na árvore.
### 5.1.2 Motivação
  
A Trie armazena um apenas caractere por nó. Isso pode resultar em árvores muito grandes, principalmente quando existem palavras que utilizam prefixos semelhantes, fazendo com que a memória não seja utilizada de forma eficiente.

Vejamos alguns exemplos:

Armazenar carro e carroça em uma Trie:
<div align="center">
 <img src="assets/saccicinct_trie_motivation_example.png" height="400">
</div>

Vejamos que, na trie cada letra é armazenada em apenas um nó e que foram utilizados diversos nós para armazenar palavras com um prefixo equivalente (carro), no entanto, nesse cenário a Trie utiliza a memória para guardar dados de maneira ineficiente, quando comparamos a uma Radix Tree, vejamos agora a inserção em uma Radix Tree:
<div align="center">
 <img src="assets/radix_tree_exemplo.png" height="400">
</div>
Em vez de criar um nó para armazenar cada caractere, a estrutura armazena blocos de caracteres (prefixos) em nós: carro > ça.

Assim, é perceptível que:
- Prefixos comuns são compartilhados;
- Nós com um único filho são combinados em substrings (prefixos);
- Fins de palavras são marcados mesmo que o caminho ainda continue (ex: “carro” termina, mas ainda existe “carroça”);

Isso reduz:
- O número de nós;
- A profundidade da árvore;
- O número de comparações feitas durante busca e inserção.

### 5.1.3 Operações

### 5.1.4 Inserção

- Começa da raiz;
- A cada passo procura um filho que compartilha um prefixo equivalente à string a ser inserida, ou parte dela;
- Existem 3 casos de inserção:
  - **Sem prefixo em comum**: nesse caso a string é adicionada em um novo nó;
  - **Prefixo completo do filho**: esse é o caso de “carro” e “carroça”, ao adicionar “carroça”, é adicionado um novo nó, filho de “carro” que contém o prefixo “ça”;
  - **Prefixo parcial**: precisa dividir (split) o nó existente.

**Vejamos um exemplo do caso iii. para adicionar “rápido” em uma árvore que contém “raiz”:**

- Prefixo em comum: “ra”;
- Resto de “raiz”: “iz”;
- Resto de “rápido”: “pido”;
- Criação de um novo nó “ra” que terá dois filhos:
  - “iz” (marcado como fim de “raiz”);
  - “pido” (marcado como fim de “rápido”);

---

### 5.1.5 Busca

- Começa da raiz;
- Busca um filho com prefixo equivalente;
- Caso encontre:
  - Desmarca aquele prefixo como fim de uma palavra;
  - Continua a busca no filho;
- Caso contrário:
  - Retorna false, pois não está na árvore;
- Verificamos se o nó atual é o fim de uma palavra:
  - Se for, palavra encontrada → retorna `true`;

---

### 5.1.6 Remoção

- Busca a palavra;
- Se encontrar, desmarca ele como o fim de uma palavra;
- Verificamos se os nós podem ser removidos ou unidos:
  - Se o nó **não tem filhos** e **não é o fim de outra palavra**, pode ser **removido**;
  - Se o nó **tem um filho** e **não é o fim de uma palavra**, pode ser **unido (concatenado)**.

### 5.1.7 Complexidade 

A Radix Tree possui complexidade O(k), de modo que k significa o tamanho da palavra, para inserção, remoção e busca. É mais eficiente que a Trie por reduzir o número de nós.

## 5.2 Saccicinct Trie

### 5.2.1 Definição

  Uma Succinct Trie é uma estrutura de dados que representa uma Trie tradicional de maneira compacta, buscando ocupar o menor espaço possível de memória, mantendo também a capacidade de realizar as demais operações, busca e navegação com eficiência. Assim, essa estrutura é ideal para armazenar grandes volumes de dados imutáveis com prefixos equivalentes, já que a inserção e remoção é custosa, como em dicionários, sistemas embarcados e etc.
  
  A grande diferença entre a Succinct Trie e a Trie tradicional, se diz respeito à forma como os dados são armazenados na memória, pois ao invés de usar ponteiros para referenciar caracteres, são utilizadas estruturas bit-level compactadas. Bit-level compactadas são estruturas que utilizam bits individuais, para representar objetos na memória de forma mais eficiente em espaço e garantir a utilização da memória de maneira mais eficiente possível.

Essa estrutura é formada por:  LOUDS — Level-Order Unary Degree Sequence, Label Array e terminal bitmap. 

- **Louds** nada mais é do que a codificação, em bits, da quantidade de filho de cada nó;
- **Label array** é um vetor paralelo que armazena os caracteres associados a cada, respectivo nó, mantendo a ordem BFS dos nós;
- **Terminal bitmaps** são vetores que indicam se os nós de um determinado nível são fins de palavra ou não, também representados em bits (`1` para fim e `0` caso não seja fim).

### 5.2.2 Motivação
  Como já foi discutido, as Tries tradicionais utilizam a memória de maneira ineficiente quando a comparamos com suas otimizações. Quando vamos utilizar uma trie para armazenar um grande número de dados, em que a memória é crítica e os dados são majoritariamente utilizados para leitura, podemos encontrar um problema em relação ao espaço de memória que está sendo utilizado, por isso, nesse cenário, as Succinct Tries utilizam a memória de maneira mais eficiente, pois consegue armazenar elementos (prefixos) utilizando a estratégia de bit-levels compactadas, preservando e otimizando a capacidade de busca e navegação, reduzindo drasticamente o uso de memória. 
- Vejamos um exemplo:
  Armazenar as palavras “carro” e “carroça” em uma Trie tradicional:
<div align="center">
 <img src="assets/saccicinct_trie_motivation_example.png" height="400">
</div>

  Vejamos que, a trie tradicional armazena cada letra em apenas um nó fazendo com que sejam utilizados vários nós para representar a palavra “carro” e “carroça”, vejamos agora a representação dessas mesmas palavra em uma Succinct Trie:

| Estrutura     | Conteúdo                              |
| ------------- | ------------------------------------- |
| `labels`      | `['c', 'a', 'r', 'r', 'o', 'ç', 'a']` |
| `bit_vector`  | `[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 0]`   |
| `is_terminal` | `[0, 0, 0, 0, 1, 0, 1]`               |

  Assim, vemos que a Succinct Trie representa dados de maneira mais eficiente, de modo que todos os nós, seguindo a BFS são armazenados em um vetor (labels), bem como o bit_vector armazena a quantidade de filhos de cada “nó”, em formato de bits e is_terminal representa quais “nós” são finais ou não de palavras.
### 5.2.3 Complexidade

| Tipo      | Custo                         |
| --------- | ----------------------------- |
| Espaço    | `2n + n × log(σ) + o(n)` bits |
| Busca     | `O(k)`                        |
| Navegação | `O(1)` ou `O(log n)`          |
| Inserção  | Muito alto                    |
| Remoção   | Muito alto                    |

  Dessa forma, a utilização das Succinct Tries se torna bem mais eficiente para armazenar muitos dados em que são imutáveis, sendo utilizados para buscas ou navegação, ex: dicionários.
## 5.3 Concurrent Tries
### 5.3.1 Definição
  Concurrent Tries é uma estrutura de dados que, como as outras, também é baseada em árvores e tende a ser uma versão otimizada de uma trie convencional, por utilizar técnicas de lock, além de utilizarem hash’s como estrutura auxiliar. No entanto, ela suporta acesso simultâneo seguro por múltiplas threads sem corromper a estrutura e sem retornar resultados inconsistentes, ou seja, permitem leitura e escrita concorrente, bem como evitam locks globais e minimizam contenção entre threads.

Uma Ctrie é estruturada como uma árvore de prefixos, de modo que: 
- Cada nó representa um **prefixo**  
- Cada nível da árvore corresponde a uma **parte do prefixo (caractere)**  
- Os nós podem conter:
  - um **mapa de filhos**, que associa cada prefixo aos próximos nós  
  - e um **valor** (caso represente uma chave completa)  
- Permite **snapshotting eficiente**, ou seja, tirar uma cópia consistente do trie **sem travar a estrutura**
### 5.3.2 Motivação

  As Ctries possuem grande usabilidade na computação, principalmente em áreas de roteamento de IP, interpretação de linguagens, caches em tempo real, Servidores HTTP ou REST com alta concorrência e etc. Isso acontece, pois nas Ctries vários usuários podem fazer a mesma operação ao mesmo tempo que não vai haver a perca ou sobrescreção de dados, isso ocorre pois essa estrutura utiliza de técnicas avançadas para inserção, remoção e busca, vejamos:
### 5.3.3 Lock-Based Tries
- Usa **locks finos** em cada nó para permitir múltiplas operações paralelas, ou seja, em vez de travar a estrutura toda para fazer uma operação, você trava apenas a menor parte necessária  
- Conforme aumenta o número de threads, melhor a estrutura funciona  
- Reduz a chance de threads ficarem esperando  

---

### 5.3.4 Lock-Free Tries
- Usa **instruções atômicas**, como o **CAS**, pois evita deadlock, contenção e se torna mais seguro por ser feito em hardware  
- Evita completamente locks, mas é mais difícil de implementar  
- Melhor performance sob alta concorrência  

---

### 5.3.5 Immutable Tries
- Cada modificação cria uma **nova versão** da estrutura  
- Threads podem acessar **versões antigas com segurança**, pois os nós são imutáveis  
- Não destroem o estado anterior de um nó  

### 5.3.6 Operações 

### 5.3.7 Inserção
- Percorre os nós até onde a chave diverge ou termina  
- Cria novos nós se necessário  
- Em Ctries (hash tries), percorre a árvore inspecionando blocos de bits do hash  
---
### 5.3.8 Busca
- Caminha até o final da chave  
- Se a estrutura for bem balanceada e não houver colisões (no hash), a profundidade é limitada  
---
### 5.3.9 Remoção
- Encontra o nó da chave  
- Marca como removido  
---
### 5.3.10 Snapshot
- Apenas aponta para o nó raiz atual  
- Como os nós são imutáveis, não há risco de inconsistência  
- Leitores podem continuar acessando a versão antiga mesmo após novas inserções  
### 5.3.11 Complexidade

| Operação     | Complexidade Média   | Pior caso | Observações                                     |
| ------------ | -------------------- | --------- | ----------------------------------------------- |
| **Inserção** | `O(k)` ou `O(log n)` | `O(k)`    | Um nó por caractere/nível; pode haver colisões  |
| **Busca**    | `O(k)` ou `O(log n)` | `O(k)`    | Caminha até a folha correspondente              |
| **Remoção**  | `O(k)`               | `O(k)`    | Pode envolver limpeza de nós intermediários     |
| **Snapshot** | `O(1)`               | `O(1)`    | Apenas copia a referência do nó raiz (imutável) |

## 5.4 BURST TRIES

### 5.4.1 Definição
 Burst Tries nada mais é do que uma estrutura de dados híbrida, pois utiliza uma organização hierárquica de uma Trie convencional (árvore), mas conta também com a utilização de buffers (arrays) nas folhas para o armazenamento de um conjunto de chaves com prefixos em comum. Se trata, assim como as anteriores, de uma versão otimizada de uma Trie, que garante melhor performance e otimização do uso da memória, pois armazena múltiplas chaves por folhas (buffers). Um problema da estrutura surge quando o buffer de uma ou mais folhas, ficam totalmente preenchidos, assim é feito o chamado “burst”, que consiste em criar novos nós internos e redistribuir as chaves armazenadas com base nos próximos caracteres. Desse modo a árvore cresce sob demanda.
  Essa estrutura garante um grande ganho de desempenho no que se diz respeito a busca, inserção e armazenamento de grandes conjuntos de strings, pois reduz o overhead de ponteiros em tries tradicionais e melhora a localidade de cache. Vejamos cenários que apresentam boa usabilidade:
  
- Compiladores;
- Sistemas de indexação;
- Dicionários dinâmicos.

### 5.4.2 Motivação
  Como já foi discutido anteriormente, as Tries tradicionais são excelentes estruturas para armazenarem dados com base em prefixos, no entanto essa estrutura possui algumas limitações ao armazenar um grande conjunto de dados, como: uso excessivo de memória, muitos ponteiros e crescimento excessivo da árvore.
  Desse modo, surge as Burst Tries que consegue equilibrar eficiência e praticidade, porquanto armazenam várias chaves com um mesmo prefixo em um único buffer	 e quando esse buffer atinge sua capacidade máxima, novos nós intermediários são criados e as chaves são divididas com base no próximo caractere, assim evitando a criação prematura de nós e reduzindo o consumo de memória.

### 5.4.3 Operações 
#### 5.4.3.1 Inserção
- Busca o buffer correspondente ao prefixo;
- Insere a chave no buffer;
- Se ultrapassar o limite do buffer, ocorre o burst;
- Cria um novo buffer;
- Redistribui as chaves em novos nós, com base nos novos prefixos;
#### 5.4.3.2 Busca
- Caminha pela trie até o buffer correspondente;
- Procura a chave dentro do buffer (usando busca binária);
#### 5.4.3.3 Remoção
- Encontra o buffer da chave;
- Remove a chave do buffer;
- Opcionalmente remove subtries vazias;
### 5.4.4 Complexidade

| Operação     | Complexidade Média | Pior Caso  | Observações                                        |
|--------------|--------------------|------------|----------------------------------------------------|
| **Busca**    | `O(k + log m)`     | `O(k + m)` | `k` = tamanho da chave, `m` = tamanho do buffer    |
| **Inserção** | `O(k + log m)`     | `O(k + m)` | Pode causar *burst* → redistribuição de `m` chaves |
| **Remoção**  | `O(k + log m)`     | `O(k + m)` | Sem burst, só remove do buffer                     |
| **Burst**    | —                  | `O(m)`     | Ocorre apenas quando buffer atinge o limite        |

## 5.5 Ternary Search Tries

### 5.5.1 Definição
  Uma Ternary Search Tree é uma estrutura de dados que combina as propriedades de uma Trie convencional e árvores de busca binária (BST), adaptada para armazenar strings de forma eficiente, tanto em relação ao tempo, quanto em relação ao consumo de memória. Sua estrutura é composta por um nó, que armazena um único caractere e três filhos, um à esquerda (para armazenar caracteres menores que o pai), um ao centro (para o próximo caractere da string, se o caractere atual for igual) e um à direita (para caracteres maiores que o pai). As TST’s tem grande usabilidade no dia à dia, como: sistemas de busca de autocomplete, dicionários e corretores ortográficos, compiladores e interpretadore e entre outros.
  Ademais, sua ideia principal é percorrer cada caractere de forma ordenada, de forma análoga à uma busca binária sobre a palavra, mas mantendo a estrutura sequencial das strings. A TST se torna mais eficiente, pois:

- Nas tries, cada nó pode ter 256 filhos (seguindo a tabela ASCII), o que exige grandes hashes;
- Em TST’s , cada nó possui apenas 3 ponteiros;
- Em grandes conjuntos de palavras se torna muito eficiente.
### 5.5.2 Operações
#### 5.5.2.1 Inserção
- Inicia pela raiz e compara cada caractere;
- Se menor, vai pra subárvore à esquerda;
- Se maior, vai pra subárvore à direita;
- Se igual, avança para o filho do meio.
#### 5.5.2.2 Busca
- Percorre cada caractere e faz as mesmas comparações da inserção;
- A palavra existe se o algoritmo chegar a um nó marcado como "fim de palavra".
#### 5.5.2.3 Remoção
- Faz uma busca da palavra a ser removida;
- Caso a encontre, a marca como “removida”;
- Caso contrário, não faz nada, pois a palavra a ser removida não está lá;
- Opcionalmente, ramos vazios podem ser removidos.
#### 5.5.2.4 Busca por prefixo
- Percorre todo o prefixo;
- Depois, faz travessia da subárvore do meio coletando palavras.
### 5.5.3 Complexidade

| Operação              | Complexidade (Tempo) | Observações                                            |
| --------------------- | -------------------- | ------------------------------------------------------ |
| **Busca**             | O(k + h)             | `k` = tamanho da string, `h` = altura da árvore        |
| **Inserção**          | O(k + h)             | Pode precisar criar até `k` novos nós                  |
| **Remoção**           | O(k + h)             | Remove marca de fim de palavra; poda é opcional        |
| **Busca por prefixo** | O(k + n)             | `n` = nº de palavras com o prefixo; percorre subárvore |
| **Espaço (Memória)**  | O(n·k)               | Menor que trie, pois só 3 ponteiros por nó             |

  Em diversos cenários, a TST garante a praticidade e excelente performance, devido a sua compacidade dos dados e do acesso sequencial dos caracteres, bem como a comparação de prefixos ocorre ordenadamente, garantindo o melhor aproveitamento da memória. Além disso, se a árvore estiver balanceada, o custo de h = log n, assim como nas BST’s convencionais.
# 6 Aplicações no mundo real
## 6.1 Rede de Computadores
### 6.1.1 Roteamento de Pacotes IP
Em redes,cada pacote IP precisa ser roteado para seu destino correto,e isso é feito com base no seu Endereço IP de destino.Para isso,os roteadores mantém uma tabela de rotas onde cada uma das entradas vai indicar qual vai ser o próximo salto para um determinado prefixo. ou seja,quando o roteador receber um determinado pacote,ele vai precisar decidir para onde enviar esse pacote na Rede para que esse pacote chegue ao seu destino correto.cada uma das entradas da tabela possui duas coisas importantes, um prefixo e um próximo salto.

**Prefixo:**
É a parte inicial de um endereço IP que vai representar um grupo de endereços.vamos supor que temos um prefixo de um endereço ip com 113.237.00/16, o prefixo 113.237. vai cobrir todos os Endereços que começam com 113.237., ou seja de 113.237.0.0  até 113.237.255.255

**Próximo salto:**	
É a porta ou o caminho para onde deve ser enviado o pacote para que chegue ao seu destino correto. Geralmente é uma interface física, como uma conexão Ethernet, ou o endereço IP do próximo roteador na rota.

**Resolvendo Problemas:**
Agora que sabemos como funciona o Roteamento de pacotes IP vamos supor o determinado problema.Dado um endereço IP 985.623.1.88,encontre a entrada de rota mais específica possível.

- Rotas:
> Rota 1: 985.643.2.88.
>
> Rota 2: 885.623.1.88.
>
> Rota 3. 985.623.00/16.

quando o pacote com o destino 985.623.1.88 chegar, o roteador vai converter esse IP em binário e vai percorrer a trie bit a bit.durante essa busca,ele vai verificar se há alguma entrada de rota correspondente no caminho,sempre que ele encontra uma entrada válida ele armazena essa entrada como a “melhor rota até o momento”.ele faz isso até que não seja mais possível descer na árvore pela falta de nós filhos ou porque o endereço chegou ao fim. no final a entrada armazenada vai ser a entrada que tem o prefixo mais longo,ou seja a melhor rota para aquele destino.

## 6.2 Análise de Sequências de DNA
### 6.2.1 Armazenamento e Busca em Grandes Volumes de Dados Genéticos
Na bioinformática, pesquisadores lidam diariamente sequências de DNA, essas sequências são compostas por quatro letras (A, C, G, T), que representam os nucleotídeos.

Essas sequências são frequentemente armazenadas, comparadas e analisadas para:

- Identificar padrões genéticos
- Encontrar regiões comuns entre espécies
- Detectar mutações
- Realizar buscas rápidas por subsequências
  
**Agora vamos pensar:**

Como podemos usar a Trie para resolver problemas que envolvem sequências de DNA?

Pense comigo, dada um grande conjunto de sequência de DNA, como podemos armazená-las de forma eficiente? Como podemos buscar rapidamente se uma determinada sequência está nesse conjunto, ou até mesmo encontrar todas as ocorrências de uma subsequência?

Bom, como já vimos anteriormente, a Trie é ideal para representar grandes conjuntos de strings com prefixos comuns, como acontece nas sequências de DNA, onde muitas compartilham trechos semelhantes. Cada nó da Trie representa um nucleotídeo, (A, C, G ou T). Cada caminho da raiz até um nó terminal representa uma sequência ou subsequência de DNA.

- Exemplo:
 
Suponha que queremos armazenar as seguintes sequências na nossa Trie.

> ATCGCCGT
> 
> ATCAGT
> 
> ATTGCG
> 
> GCTACA
> 
> GCTATT

A Trie que será construída terá ramos em comum para os ramos que tem prefixos repetidos, como podemos observar na imagem.
<div align="center">
  <img src="assets/trie_dna.png" height="400">
</div>
Essas sequências genéticas podem ser muito mais longas e repetitivas que essas que utilizamos,é nesse momento que o uso da Trie se torna muito mais eficiente,pois ao ultilizar a Trie para armazenar essas sequências,o uso do Armazenamento vai ser otimizado, já que quando várias sequências de DNA compartilham os mesmos prefixos, a Trie armazena esse prefixo uma única vez.

Além disso, o uso da Trie vai otimizar o tempo de busca dessas sequências, porque muitas delas compartilham prefixos, ou seja, começam com as mesmas sequências. A Trie aproveita isso,tornando assim a busca mais rápida, já que não precisa repetir o mesmo caminho várias vezes. Isso é ideal quando temos muitas sequências parecidas, como é comum no DNA, tornando assim a Trie muito utilizada para buscar sequências de DNA principalmente em áreas da bioinformática, onde é essencial lidar com grandes volumes de dados genéticos de forma rápida e eficiente.
# 7 Guia para resolução de problemas
## 7.1 Dicas
### 7.1.1 Quando usar uma Trie?
**Passo 1:**  Analisar problema
 A primeira pergunta que você deve se fazer é:
 
> **Que tipos de dados envolve o problema e qual a sua unidade mais básica? "**

Se a resposta para essa pergunta envolver uma sequência construida através de um alfabeto(conjunto **FINITO** de símbolos), preste bem atenção no finito, essa é uma das premissas chaves, veremos no passo 3 que o tamanho desse conjunto impactará diretamente no consumo de memória. Nesse caso, é um bom sinal de que o problema pode ser resolvido com Trie.

Pois em sua essência, Trie é uma estrutura otimizada para armazenar e consultar sequências. As sequências mais comuns são as strings, nesse caso, o alfabeto são os caractêres. No entanto, o conceito é bem mais amplo, pode ser uma sequência de digitos, como números de telefones, ou até uma sequência de bits.

**Passo 2:** Analisar quais as operações chave para resolver o problema.

Próxima pergunta que você deve fazer, é quais as operações chaves que preciso para resolver o problema, se as operações são baseadas em prefixos, Trie é disparada uma das estruturas de dados que você deve levar em consideração. Ela materializa a ideia de prefixo em sua estrutura, a sua eficiência para suas operações são geralmente $O(L)$, onde $L$ é o comprimento do prefixo. Por exemplo:
- "Liste todas as palavras que começam com ..."(autocompletar)
- "Verifique se tem alguma palavra com prefixo ..."

**Passo 3:**  Botar em consideração as restrições de tempo e espaço do problema

Tendo Trie como um ótimo candidato, devemos nos perguntar, será ela a estrutura certa? Ela é realmente a melhor opção?

A Trie é muito boa em remover, inserir, e buscar uma sequência de comprimento *L*, independente do número total de palavras no dicionário.

Qual o problema da Trie? Memória!
Para cada nó, pode ter ponteiros para cada elemento no alfabeto, desse modo, se o alfabeto é muito grande, se torna inviavel para implementação de Trie padrão. Então é preciso se perguntar:

>**"O afalbeto é pequeno ou é muito grande?"**

Bom, e no caso de ser inviável? Considere variações de Trie com otmizações de memória, como uma *TST*, ou algumas otimizações como guardar um mapa de hash em cada nó invés de uma array fixo, economiza mais memória em troca de um pouco de velocidade.
## 7.2 Problemas Práticos
### 7.2.1 [Monitoria de LP2](https://www.spoj.com/problems/ADAINDEX/en/)
Ana, para otimizar seu tempo ao corrigir os inúmeros projetos da disciplina de Laboratório de Programação 2, está desenvolvendo um script de análise de código.

O primeiro passo de seu script é extrair todos os identificadores (nomes de variáveis, funções, classes, etc.) dos códigos dos alunos e compilá-los em uma grande lista. Agora, para analisar rapidamente os padrões de nomenclatura e a aderência dos alunos às boas práticas, ela precisa de uma forma eficiente de fazer consultas nessa lista.

O desafio é o seguinte: você receberá a lista completa de identificadores e algumas consultas em forma de prefixos. Para cada prefixo, sua tarefa é ajudar Ana a descobrir quantos identificadores na lista começam com ele.

**Entrada**
A primeira linha contém N, Q: o número de palavras na lista e o número de consultas.

Seguem-se N linhas, com palavras consistindo de letras minúsculas. A soma de seus comprimentos não será maior que $10^6$.

Seguem-se Q linhas, com palavras (consultas) consistindo de letras minúsculas. A soma de seus comprimentos não será maior que $10^6$.

**Saída**
Para cada consulta, imprima o número de palavras na lista que começam com a palavra atual.

**Exemplo**
```
12 6
calculartotalpedido
calcularimposto
cliente
clientedadospessoais
clienteservice
listadeclientes
getusuarioporid
usuariorepositorio
usuario
x
a
temp
cliente
calcular
usuario
get
lista
z
```

```
3
2
2
1
1
0
```
### 7.2.2 [Nicks semelhantes](https://codeforces.com/problemset/problem/514/C)
Gabriel, um ávido jogador de Valorant, está desenvolvendo uma ferramenta para analisar nicks de jogadores. A ideia é encontrar jogadores com nicks parecidos, que possam ser amigos ou contas alternativas (*smurfs*).

Inicialmente, a memória de sua ferramenta é preenchida com uma lista de **n** nicks. Em seguida, a ferramenta deve ser capaz de processar consultas do seguinte tipo: "Dado um nick **s**, determine se a memória contém um nick **t** que tenha o mesmo número de caracteres que **s** e que difira de **s** em exatamente uma posição".

Gabriel já montou a estrutura da ferramenta, só falta escrever o programa para ela e testá-lo com os dados, que consistem em **n** nicks iniciais e **m** consultas. Ele decidiu confiar este trabalho a você.

**Entrada**
A primeira linha contém dois números não negativos **n** e **m** ($0 \le n \le 3 \cdot 10^5, 0 \le m \le 3 \cdot 10^5$) — o número de nicks iniciais e o número de consultas, respectivamente.

A seguir, vêm **n** strings não vazias que são carregadas na memória da ferramenta.

A seguir, vêm **m** strings não vazias que são as consultas para a ferramenta.

O comprimento total das linhas na entrada não excede $6 \cdot 10^5$. Cada linha consiste apenas das letras 'a', 'b', 'c'.

**Saída**
Para cada consulta, imprima em uma única linha "**YES**" (sem as aspas), se a memória contiver o nick procurado, caso contrário, imprima "**NO**" (sem as aspas).

**Exemplo**
```
5 6
limaz
bielzzr
yanlira
maddy
addsound
limax
maddy
bielzzy
yanlaro
addsound
limas
```

```
YES
NO
YES
NO
NO
YES
```
### 7.2.3 [Sintonia computacional](https://www.spoj.com/problems/QN01/en/)
Lucas e Yan, figuras conhecidas nos corredores da UFCG, compartilham uma tradição sagrada: comer pastel em seu Hélio. Dizem as lendas que a quantidade de ketchup que Lucas coloca no pastel é diretamente proporcional à complexidade do último problema que resolveram.

Uma coisa que sempre intrigou seus colegas é como eles consistentemente tiram notas muito parecidas em todas as disciplinas. O segredo, segundo eles, está em sua "sintonia computacional". Para provar isso, eles criaram um desafio.

Dada uma lista de números inteiros, representando as notas que tiraram em várias avaliações, eles querem encontrar o "potencial máximo de colaboração". Esse potencial é definido como o resultado máximo da operação OU-EXCLUSIVO (XOR) entre as notas de duas avaliações(não necessarimanete distinta)s. Será que você consegue calcular esse valor e desvendar o segredo da dupla?

**Entrada**
A primeira linha é um inteiro `n`, a quantidade de notas.
A segunda linha é uma lista com todas as notas.

**Saída**
Na primeira linha, um único inteiro, com a mairo soma da operação XOR entre as notas de duas avaliações distintas. Na segunda linha a posição *i* e *j* das notas utilizacas.

**Exemplo**
```
3
1 2 3
```

```
3
1 2
```

## 7.3 Trie ou não Trie? Eis a questão
### 7.3.1 Problema 1: Lista Telefônica
**Descrição:** Você recebe uma lista de números de telefone. Sua tarefa é determinar se a lista é consistente, ou seja, se nenhum número é prefixo de outro. Por exemplo, se a lista contém "911" e "911254", ela é inconsistente. 

**Pergunta:** A Trie é uma boa estrutura para resolver este problema? Por quê?

### 7.3.2 Problema 2: Verificador de Anagramas
**Descrição:** Dadas duas palavras, determine se uma é um anagrama da outra (contém exatamente as mesmas letras, na mesma quantidade, mas em ordem diferente). Por exemplo, "amor" e "roma" são anagramas. 

**Pergunta:** A Trie ajudaria a resolver este problema de forma eficiente? Qual seria a abordagem principal?

### 7.3.3 Problema 3: Autocompletar para Contatos
**Descrição:** Você está implementando a busca em uma lista de contatos. Ao digitar as primeiras letras de um nome, o sistema deve sugerir todos os contatos que começam com essas letras. 

**Pergunta:** Qual estrutura de dados você usaria para implementar essa funcionalidade de forma que as sugestões apareçam quase instantaneamente?

### 7.3.4 Problema 4: Substring Comum Mais Longa
**Descrição:** Dadas duas strings, encontre a substring mais longa que aparece em ambas. Por exemplo, para "banana" e "cabana", a resposta é "bana". 

**Pergunta:** Seria possível usar uma Trie padrão para encontrar a substring comum mais longa? Qual é a dificuldade?

### 7.3.5 Problema 5: Banco de Senhas
**Descrição:** Um sistema precisa verificar se uma nova senha escolhida por um usuário já existe em um banco de dados com milhões de senhas conhecidas para evitar senhas duplicadas. A verificação precisa ser extremamente rápida. 

**Pergunta:** Entre uma Trie e um Hash Set (Conjunto de Hash), qual seria mais apropriado para esta tarefa e por quê?
## 7.4 Gabarito dos Problemas Práticos
### 7.4.1 [Monitoria de LP2](https://www.spoj.com/problems/ADAINDEX/en/ "null")
#### 7.4.1.1 Análise do Problema
À primeira vista, o problema parece ser só uma contagem simples. Mas qual é a pegadinha? A escala. Com milhões de palavras e consultas, uma abordagem de força bruta, checando palavra por palavra, seria um desastre. Simplesmente não passaria no tempo limite. A questão principal, então, não é _o quê_ fazer, mas _como_ fazer isso de forma inteligente e rápida. O enunciado praticamente grita a resposta ao pedir para contar palavras que "começam com" um prefixo. Esse é o território da Trie. Ela nasceu para isso. Então, como a gente usa essa ferramenta da melhor forma possível?
#### 7.4.1.2 Estratégia de Solução
O truque aqui é dar uma turbinada na Trie padrão. Não basta ela só guardar as palavras; ela precisa nos ajudar a contar. A gente quer que a própria estrutura já tenha a resposta pronta. Mas como? O pensamento é o seguinte: se, ao inserir uma palavra, a gente deixasse um "rastro" nos nós por onde ela passou?
1. **Estrutura do Nó Aprimorada:** A chave para a velocidade é modificar o tijolo básico da Trie: o nó. Além dos ponteiros para os filhos, cada nó vai ter um contador, que podemos chamar de `prefix_count`. É uma ideia simples, mas poderosa. Pense nesse contador como um pedágio numa estrada: toda vez que uma palavra "passa" por um nó ao ser inserida, o contador aumenta. Curto e grosso.
2. **Construção e Agregação:** Na hora de construir a Trie, a mágica acontece. A gente insere cada uma das `N` palavras, caractere por caractere. O pulo do gato é que cada nó que a gente visita nesse caminho tem seu `prefix_count` incrementado. O que isso significa na prática? Que um nó passa a saber não só que ele faz parte de um caminho, mas _quantas_ palavras compartilham aquele exato prefixo. No final, a Trie deixa de ser só um dicionário e vira um mapa que mostra o quão popular é cada começo de palavra.
3. **Consulta Super Rápida:** Com a Trie montada desse jeito, a consulta vira uma piada de tão fácil. Para um prefixo qualquer, a gente só precisa passear pela Trie. Se o caminho existir até o final do prefixo, o número guardado no `prefix_count` daquele último nó já é a nossa resposta. Sem precisar olhar o resto do banco de dados. E se o caminho quebrar no meio? Mais fácil ainda. Quer dizer que nenhuma palavra começa com aquele prefixo, e a resposta é zero.
<div align="center">
  <img src="assets/exemplo_problema_monitoria.png" height="1200">
</div>

#### 7.4.1.3 Complexidade
O mais legal dessa abordagem é o resultado final. A complexidade, $O(S_N​+S_Q​)$, ou seja, a soma de todas as palavras inseridas com a soma de todos os prefixos, é linear. Isso quer dizer que o tempo de execução cresce junto com o tamanho da entrada, e não de forma explosiva. É uma otimização gigantesca, tornando a solução não só viável, mas extremamente eficiênte😛.

### 7.4.2 [Nicks semelhantes](https://codeforces.com/problemset/problem/514/C "null")
#### 7.4.2.1 Análise do Problema
Este problema já é um pouco mais sutil. A busca não é por uma correspondência exata, nem por um prefixo. O desafio é encontrar uma "semelhança" com uma regra bem específica: mesmo tamanho e diferença em apenas uma posição. Força bruta? Sem chance, seria lento demais. A pista crucial aqui é o alfabeto minúsculo ('a', 'b', 'c'). Um alfabeto tão pequeno limita drasticamente o espaço de busca para variações. Isso é um convite para usar uma estrutura que se organiza por caracteres, como a Trie. A grande questão é: como podemos vasculhar a Trie em busca de uma correspondência que permite exatamente uma falha?🧐
#### 7.4.2.2 Estratégia de Solução
A estratégia se baseia numa ideia bem legal: uma busca em profundidade (DFS) que funciona como uma "busca com orçamento para erros". A Trie nos dá a estrutura para não repetir trabalho, e a recursão vai controlar esse nosso orçamento.
1. **Construção da Trie:** Primeiro, o básico. Inserimos todos os `N` nicks na Trie, marcando os nós terminais com uma flag `isEndOfWord` para sabermos onde uma palavra de fato termina.
2. **Consulta Recursiva com Orçamento de Erros:** Para cada consulta `s`, a gente dispara uma função recursiva que navega pela Trie. O brilhantismo está nos parâmetros que ela carrega: o nó atual, a posição `index` na string `s`, e um contador `mismatches`. Esse contador é o nosso orçamento.
    - **Como a busca funciona?** Em cada nó, a gente olha para os possíveis caminhos ('a', 'b', 'c'). Se o caractere do caminho bate com o da nossa string `s`, ótimo, seguimos em frente sem gastar o orçamento. Mas e se for diferente? Aí a gente gasta uma unidade do orçamento (`mismatches` aumenta) e continua mesmo assim. É a nossa chance de encontrar a palavra "semelhante".
    - **Quando a gente para?** A busca por um caminho é abortada na hora se o orçamento estourar (`mismatches > 1`). Isso poda a árvore de busca e economiza um tempo absurdo. Por outro lado, se chegarmos ao final da string, uma solução válida só é encontrada se duas condições forem perfeitas: o orçamento foi gasto em exatamente uma unidade (`mismatches == 1`) e o nó atual representa o fim de uma palavra de verdade (`node.isEndOfWord == true`).
Essa abordagem sistemática explora todas as variações de uma letra de forma inerentemente eficiente, pois os prefixos comuns a múltiplos nicks são percorridos apenas uma vez.
#### 7.4.2.3 Complexidade
A construção é linear em relação à soma dos comprimentos dos nicks, $O(\sum{|nick_i|})$. A consulta para uma string de comprimento $L$ tem uma complexidade de aproximadamente $O(L\cdot ∣\text{alfabeto}∣)$, pois em cada nível, no pior caso, exploramos um pequeno número de ramos. O tempo total é, portanto, ordens de magnitude mais rápido que a abordagem ingênua, sendo independente do número total de nicks na base de dados durante a fase de consulta. 
### 7.4.3 [Sintonia computacional](https://www.spoj.com/problems/QN01/en/)
#### 7.4.3.1 Análise do Problema
Aqui a Trie vai além do mundo das palavras e mergulha nas operações de bits. O desafio de achar o maior XOR possível entre dois números de uma lista enorme mostra na hora que a força bruta ($O(N^2)$) não é uma opção. A solução exige uma virada de chave no nosso pensamento. E se a gente parasse de ver os números como$\dots$ bem, números, e passasse a vê-los como sequências de bits? Essa abstração é a porta de entrada para a **Trie Binária**. A gente vai organizar os números pelos seus prefixos binários, e essa é a chave para uma busca muito mais rápida.

#### 7.4.3.2 Estratégia de Solução
A solução se apoia numa estratégia gulosa (ou "esganada"), que a Trie Binária torna possível. Pense bem, qual é o segredo para maximizar um número em binário? É fazer seus bits mais à esquerda, os que valem mais, serem iguais a `1`.
1. **Estrutura e Construção:** A gente monta uma Trie onde cada nó tem no máximo dois filhos: `0` e `1`. Simples assim. Cada número da lista é inserido como um caminho de `K` bits (por exemplo, cerca de 31 para um `int`), do mais importante (MSB) para o menos importante (LSB).
2. **Busca Gulosa:** Para cada número `x` da lista, a gente vai caçar na Trie o seu par perfeito `y`. Como essa caçada funciona? Para cada bit de `x`, do mais valioso para o menos, a gente aplica uma lógica gulosa.
    - A gente sabe que `a XOR b` dá `1` se `a` e `b` forem diferentes. Certo? Então, para fazer o resultado ser o maior possível, se o bit atual de `x` é `0`, a gente quer muito achar um caminho na Trie que comece com `1`. Por isso, tentamos descer pelo filho `1`. Se o bit de `x` é `1`, a gente faz o contrário, buscando o caminho `0`.
    - É como se estivéssemos construindo o par ideal para o XOR, bit a bit. Em cada nível da Trie, a gente faz a escolha que "liga" o bit mais valioso que der no resultado. E se o caminho ideal (o bit oposto) não existir? Aí não tem jeito, somos forçados a seguir pelo que tem, e aceitamos um `0` naquela posição do resultado.
    - Ao final dessa viagem de `K` passos, teremos encontrado o número que dá o maior XOR possível com `x`.

A gente guarda o maior valor de XOR que encontrarmos no geral e as indexações para dar a resposta final.

<div align="center">
  <img src="assets/exemplo_problema_sintonia.png" height="300">
</div>

#### 7.4.3.3 Complexidade
- **Seja K o número de bits na representação dos números (ex: 31).**
- **Tempo de Construção:** $O(N \cdot K)$.
- **Tempo de Busca:** Para cada um dos $N$ números, fazemos uma busca de profundidade $K$. Logo, $O(N \cdot K)$.
- **Tempo Total:** $O(N \cdot K)$, uma melhoria drástica em relação ao $O(N^2)$ da força bruta.

## 7.5 Gabarito dos Desafios

### 7.5.1 Problema 1: Lista Telefônica
**Veredito:** Pode apostar todas as fichas: a Trie é a resposta.

#### 7.5.1.1 Análise Passo a Passo

- **Passo 1 (Analisar Problema):** Pense bem, o que são números de telefone? Em sua essência, são apenas **-> sequências de dígitos <-**. Nosso universo se resume ao alfabeto de `0` a `9`, um conjunto bem específico e limitado. Esse cenário, por si só, já deveria acender um letreiro em neon na sua cabeça: "Hmm, isso tem cheiro de Trie😏".
- **Passo 2 (Analisar Operações):** A alma deste desafio pulsa em uma única palavra: **"prefixo"**. Precisamos saber se um número é o ponto de partida de outro. E qual estrutura de dados parece ter sido desenhada com a palavra "prefixo" em mente? Exato, a Trie. Ela não é apenas uma candidata; é a principal suspeita desde o início.
- **Passo 3 (Analisar Restrições):** A Trie, neste caso, opera como um detetive particularmente astuto. Ela confere a consistência no exato momento da inserção, sem nenhum trabalho extra. Visualize o "911254" sendo adicionado depois do "911". A Trie, ao percorrer o caminho `9 -> 1 -> 1`, nota na hora: "Ei, este nó por onde estou passando já marca o fim de outra palavra!". Fim de jogo: a lista é inconsistente. O inverso também funciona: se inserirmos "911" e o nó do último `1` já tem uma continuação (o `2`), a Trie deduz: "Entendi, o número que acabei de registrar é o começo de outro já existente!". É simples, é elegante e de uma eficiência brutal🙂‍↔️.

### 7.5.2 Problema 2: Verificador de Anagramas
**Veredito:** De jeito nenhum. Tentar usar uma Trie aqui é uma cilada.😬

#### 7.5.2.1 Análise Passo a Passo
- **Passo 1 (Analisar Problema):** A primeira impressão até que engana. Estamos lidando com strings, que são sequências dentro de um alfabeto finito. Parece o território perfeito, certo? A história é outra.🤐
- **Passo 2 (Analisar Operações):** É neste ponto que o castelo de cartas da Trie desmorona. A tarefa é verificar anagramas. E qual é a regra de ouro, o DNA de um anagrama? A **ordem dos caracteres não significa nada**; o que importa é a contagem de cada um. Agora, qual é o pilar que sustenta uma Trie? A **-> ordem é sagrada <-**. Seus caminhos são construídos a partir da sequência exata dos caracteres. Querer resolver anagramas com uma Trie é como tentar montar um quebra-cabeça usando um mapa rodoviário. A ferramenta simplesmente não conversa com a lógica do problema.
- **Passo 3 (Analisar Restrições):** Forçar o uso de uma Trie aqui seria um monumental desperdício de tempo e de linhas de código. A solução de verdade é ridiculamente mais simples. Que tal apenas contar a frequência de cada letra nas duas palavras? Se os totais baterem, são anagramas. Ou, mais direto ainda: coloque as letras de cada palavra em ordem alfabética. Se os resultados forem idênticos, temos um vencedor. Ambas as abordagens são muito mais rápidas e infinitamente mais lógicas.

### 7.5.3 Problema 3: Autocompletar para Contatos
**Veredito:** Este é o palco perfeito para a Trie brilhar.
#### 7.5.3.1 Análise Passo a Passo
- **Passo 1 (Analisar Problema):** Temos nomes de contatos. São, por natureza, sequências de caracteres de um alfabeto definido. Nenhum mistério por aqui.
- **Passo 2 (Analisar Operações):** O que o enunciado pede, exatamente? "Sugerir todos os contatos que **começam com**...". Isso não é só uma pista; é a descrição funcional do superpoder de uma Trie. Já parou pra pensar como a busca do seu smartphone cospe sugestões de nomes antes mesmo de você piscar? Não tem feitiçaria, só a estrutura de dados correta fazendo seu trabalho.
- **Passo 3 (Analisar Restrições):** A Trie é a escolha canônica para esta tarefa, ponto final. Buscar pelo prefixo `jo` é uma operação quase instantânea. Ao alcançarmos o nó que representa o final de `jo`, a Trie se comporta como um sistema de arquivos incrivelmente otimizado: a partir dali, basta listar todos os "arquivos" (os nomes completos) contidos naquela "pasta". Uma busca em profundidade (DFS) partindo daquele ponto resolve a questão de maneira limpa, rápida e eficiente. Para um sistema que exige respostas em frações de segundo, não existe competidor à altura.

### 7.5.4 Problema 4: Substring Comum Mais Longa
**Veredito:** Uma Trie comum não dá conta do recado. Precisamos de mais poder de fogo.

#### 7.5.4.1 Análise Passo a Passo
- **Passo 1 (Analisar Problema):** De volta às strings. O ponto de partida parece familiar e seguro.
- **Passo 2 (Analisar Operações):** Agora vem a virada na trama. A palavra que muda completamente o jogo é **"substring"**. Uma Trie lida maravilhosamente bem com prefixos, que são a porta da frente de uma palavra; ela só se importa com o começo. Uma substring, por outro lado, é um pedaço que pode ser extraído de qualquer lugar, do meio, do fim. A Trie padrão, com seu foco no ponto de partida, é míope para o que acontece no interior da palavra.
- **Passo 3 (Analisar Restrições):** Usar uma Trie padrão aqui nos entregaria, no máximo, o prefixo comum mais longo, o que não resolve o problema. E se a gente turbinasse a Trie? Pense na palavra "banana" e imagine inserir na Trie não só ela, mas todos os seus sufixos: "anana", "nana", "ana", "na" e "a". Se replicarmos esse processo para todas as palavras, a resposta que buscamos emerge. Essa estrutura "anabolizada" tem nome e sobrenome: **Árvore de Sufixos (Suffix Tree)**, a prima mais velha e mais forte da Trie, projetada sob medida para este tipo de desafio.

### 7.5.5 Problema 5: Banco de Senhas
**Veredito:** Embora funcione, um Hash Set é a ferramenta certa para o trabalho.

#### 7.5.5.1 Análise Passo a Passo
- **Passo 1 (Analisar Problema):** Senhas são strings. Sequências de caracteres de um alfabeto. Na teoria, o terreno é fértil para uma Trie.
- **Passo 2 (Analisar Operações):** Qual é a missão aqui? Uma busca por **existência exata**. A pergunta é direta: "esta senha está no banco de dados?". Queremos um "sim" ou um "não", nada mais. Não há necessidade de prefixos, de sugestões, de nada além de uma consulta de pertencimento.
- **Passo 3 (Analisar Restrições):** É aqui que a gente precisa pensar como um bom artesão: usar a ferramenta ideal para cada tarefa. Uma Trie é um canivete suíço, cheia de funções úteis. Um **Hash Set**, por outro lado, é uma chave de fenda: faz uma única coisa, mas com uma velocidade e precisão absurdas. Para a simples tarefa de checar se a senha existe, não precisamos do arsenal do canivete; a chave de fenda resolve. Um Hash Set entrega a resposta em tempo médio constante, O(1). É mais simples de implementar para este fim e, muitas vezes, mais eficiente em termos de memória. A Trie até daria conta, mas seria um exagero desnecessário.
