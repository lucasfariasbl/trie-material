# 1 Motivação
# 2 Introdução
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
> Nossa estrutura ficaria assim: C -> A -> R -> R -> O -> S -> S -> E -> L

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
### 3.2.1 Inserção

### 3.2.2 Pesquisa
### 3.2.3 Prefixos
## 3.3 Análise de complexidade de tempo e memória
## 3.4 Remoção 
## 3.5 Listagem de palavras por prefixos
# 4 Comparações
# 5 Variações e otimizações
## 5.1 Radix Tree
### 5.1.1 Definição
 
  Uma Radix Tree (também chamada de Compact Trie ou Patricia Tree) é uma estrutura de dados, baseada em nós, que armazena, geralmente, strings ou números de forma eficiente, especialmente quando apresentam prefixos em comum.

  A Radix Tree se trata de uma versão otimizada da Trie, levando-se em consideração que, na Trie, cada nó armazena apenas uma letra de uma palavra. No entanto, a Radix Tree busca armazenar prefixos de palavras, pois, assim, a estrutura se torna mais eficiente para o uso de memória, além de diminuir a quantidade de ramos existentes na árvore.

### 5.1.2 Motivação
  
  A Trie armazena um apenas caractere por nó. Isso pode resultar em árvores muito grandes, principalmente quando existem palavras que utilizam prefixos semelhantes, fazendo com que a memória não seja utilizada de forma eficiente.

Vejamos alguns exemplos:

Armazenar carro e carroça em uma Trie: 

<br>
<br>
<div align="center">
 <img src="assets/exemplo-trie.png" height="500">
</div>
<br>
<br>

Vejamos que, na trie cada letra é armazenada em apenas um nó e que foram utilizados diversos nós para armazenar palavras com um prefixo equivalente (carro), no entanto, nesse cenário a Trie utiliza a memória para guardar dados de maneira ineficiente, quando comparamos a uma Radix Tree, vejamos agora a inserção em uma Radix Tree:

<br>
<br>
<div align="center">
 <img src="assets/exemplo-radix.png" height="500">
</div>
<br>
<br>

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

### 5.1.3.1 Inserção

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

### 5.1.3.2 Busca

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

### 5.1.3.3 Remoção

- Busca a palavra;
- Se encontrar, desmarca ele como o fim de uma palavra;
- Verificamos se os nós podem ser removidos ou unidos:
  - Se o nó **não tem filhos** e **não é o fim de outra palavra**, pode ser **removido**;
  - Se o nó **tem um filho** e **não é o fim de uma palavra**, pode ser **unido (concatenado)**.

### 5.1.4 Complexidade 

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

<br>
<br>
<div align="center">
 <img src="assets/exemplo-trie.png" height="500">
</div>
<br>
<br>

  Vejamos que, a trie tradicional armazena cada letra em apenas um nó fazendo com que sejam utilizados vários nós para representar a palavra “carro” e “carroça”, vejamos agora a representação dessas mesmas palavra em uma Succinct Trie:

| Estrutura     | Conteúdo                              |
| ------------- | ------------------------------------- |
| `labels`      | `['c', 'a', 'r', 'r', 'o', 'ç', 'a']` |
| `bit_vector`  | `[1,0, 1,0, 1,0, 1,0, 1,0, 1,0, 0]`   |
| `is_terminal` | `[0, 0, 0, 0, 1, 0, 1]`               |

  Assim, vemos que a Succinct Trie representa dados de maneira mais eficiente, de modo que todos os nós, seguindo a BFS são armazenados em um vetor (labels), bem como o bit_vector armazena a quantidade de filhos de cada “nó”, em formato de bits e is_terminal representa quais “nós” são finais ou não de palavras.

### 5.2.3 Complexidade

| Tipo      | Custo                          |
|-----------|--------------------------------|
| Espaço    | `2n + n × log(σ) + o(n)` bits |
| Busca     | `O(k)`                         |
| Navegação | `O(1)` ou `O(log n)`           |
| Inserção  | ❌ Muito alto                  |
| Remoção   | ❌ Muito alto                  |

  Dessa forma, a utilização das Succinct Tries se torna bem mais eficiente para armazenar muitos dados em que são imutáveis, sendo utilizados para buscas ou navegação, ex: dicionários.

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
<br>
<br>
<div align="center">
  <img src="assets/trie_visualization_final.gif" height="600">
</div>
<br>
<br>
Essas sequências genéticas podem ser muito mais longas e repetitivas que essas que utilizamos,é nesse momento que o uso da Trie se torna muito mais eficiente,pois ao ultilizar a Trie para armazenar essas sequências,o uso do Armazenamento vai ser otimizado, já que quando várias sequências de DNA compartilham os mesmos prefixos, a Trie armazena esse prefixo uma única vez.
<br>
<br>
Além disso, o uso da Trie vai otimizar o tempo de busca dessas sequências, porque muitas delas compartilham prefixos, ou seja, começam com as mesmas sequências. A Trie aproveita isso,tornando assim a busca mais rápida, já que não precisa repetir o mesmo caminho várias vezes. Isso é ideal quando temos muitas sequências parecidas, como é comum no DNA, tornando assim a Trie muito utilizada para buscar sequências de DNA principalmente em áreas da bioinformática, onde é essencial lidar com grandes volumes de dados genéticos de forma rápida e eficiente.

# 7 Guia para resolução de problemas
## 7.1 Dicas
### 7.1.1 Quando usar uma Trie?
**Passo 1:**  Analisar problema
 A primeira pergunta que você deve se fazer é:
**<p style="text-align:center;">"Que tipos de dados envolve o problema e qual a sua unidade mais básica? "</p>**
Se a resposta para essa pergunta envolver uma sequência construida através de um alfabeto(conjunto **FINITO** de símbolos), preste bem atenção no finito, essa é uma das premissas chaves, veremos no passo 3 que o tamanho desse conjunto impactará diretamente no consumo de memória. Nesse caso, é um bom sinal de que o problema pode ser resolvido com Trie.

Pois em sua essência, Trie é uma estrutura otimizada para armazenar e consultar sequências. As sequências mais comuns são as strings, nesse caso, o alfabeto são os caractêres. No entanto, o conceito é bem mais amplo, pode ser uma sequência de digitos, como números de telefones, ou até uma sequência de bits.

**Passo 2:** Analisar quais as operações chave para resolver o problema.

Próxima pergunta que você deve fazer, é quais as operações chaves que preciso para resolver o problema, se as operações são baseadas em prefixos, Trie é disparada uma das estruturas de dados que você deve levar em consideração. Ela materializa a ideia de prefixo em sua estrutura, a sua eficiência para suas operações são geralmente *O(L)*, onde *L* é o comprimento do prefixo. Por exemplo:
- "Liste todas as palavras que começam com ..."(autocompletar)
- "Verifique se tem alguma palavra com prefixo ..."

**Passo 3:**  Botar em consideração as restrições de tempo e espaço do problema

Tendo Trie como um ótimo candidato, devemos nos perguntar, será ela a estrutura certa? Ela é realmente a melhor opção?

A Trie é muito boa em remover, inserir, e buscar uma sequência de comprimento *L*, independente do número total de palavras no dicionário.

Qual o problema da Trie? Memória!
Para cada nó, pode ter ponteiros para cada elemento no alfabeto, desse modo, se o alfabeto é muito grande, se torna inviavel para implementação de Trie padrão. Então é preciso se perguntar:
**<p style="text-align:center"> "O afalbeto é pequeno ou é muito grande?"</p>**
Bom, e no caso de ser inviável? Considere variações de Trie com otmizações de memória, como uma *TST*, ou algumas otimizações como guardar um mapa de hash em cada nó invés de uma array fixo, economiza mais memória em troca de um pouco de velocidade.
https://vjudge.net/contest/698873#problem/A
### 6.1.1 Problemas
#### 6.1.1.1 Ana, a Joaninha
Ana, a Joaninha, tem muitas coisas para fazer e quase nenhum tempo. Ela quer economizar tempo enquanto procura por algo, então ela decidiu criar um mecanismo de busca. Ela tem muitas palavras em sua lista de AFAZERES (TODO). Custa a ela um tempo precioso descobrir se uma palavra está na lista, então ela procura sua ajuda. Você receberá uma lista e algumas consultas. Será solicitado a você que encontre quantas palavras na lista de AFAZERES têm uma determinada palavra como prefixo.

**Entrada**
A primeira linha contém N, Q: o número de palavras na lista de AFAZERES e o número de consultas.

Seguem-se N linhas, com palavras (da lista de AFAZERES) consistindo de letras minúsculas. A soma de seus comprimentos não será maior que 106.

Seguem-se Q linhas, com palavras (consultas) consistindo de letras minúsculas. A soma de seus comprimentos não será maior que 106.

**Saída**
Para cada consulta, imprima o número de palavras na lista de AFAZERES que têm a palavra atual como prefixo.

**Exemplo**

#### 6.1.1.2 Problema do maior XOR
Lucas e Yan, figuras conhecidas nos corredores da UFCG, são o exemplo perfeito de uma dupla dinâmica na Ciência da Computação. Além de enfrentarem juntos os desafios de Cálculo 2, eles compartilham uma tradição sagrada: comer pastel no Hélio's. Dizem as lendas que a quantidade de ketchup que Lucas coloca no pastel é diretamente proporcional à complexidade do último problema que resolveram.

Uma coisa que sempre intrigou seus colegas é como eles consistentemente tiram notas muito parecidas em todas as disciplinas. O segredo, segundo eles, está em sua "sintonia computacional". Para provar isso, eles criaram um desafio.

Dada uma lista de números inteiros, representando as notas que tiraram em várias avaliações, eles querem encontrar o "potencial máximo de colaboração". Esse potencial é definido como o resultado máximo da operação OU-EXCLUSIVO (XOR) entre as notas de duas avaliações distintas. Será que você consegue calcular esse valor e desvendar o segredo da dupla?

**Entrada**
A primeira linha é um inteiro `n`, a quantidade de notas.
A segunda linha é uma lista com todas as notas.

**Saída**
Um único inteiro, com a mairo soma da operação XOR entre as notas de duas avaliações distintas.

**Exemplo**


https://vjudge.net/problem/SPOJ-QN01


https://vjudge.net/contest/698873#problem/D



