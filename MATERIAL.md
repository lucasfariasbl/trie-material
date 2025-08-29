# 1 Motivação
# 2 Introdução
# 3 Implementação
## 3.1 Estruturas fundamentais
### 3.1.1 Node
Os nós na implementação de uma Trie, assim como em Linked Lists ou outra estrutura que os utilize, são as peças do quebra-cabeça. Sem eles, não existe implementação.
#### 3.1.1.1 Atributos
A primeira vista pode ser estranho e incomum, mas o nó não armazena o valor que representa dentro da estrutura da Trie, pois aqui, não temos um nó que se linka com outro através de apontadores como "left" ou "right", os nós armazenam algo mais interessante...

**HashMap:** 

Isso mesmo, aqui, cada nó armazena um HashMap. Para fins didáticos, nossa implementação aborda a manipulação de palavras na Trie. Caso você pare um pouco para pensar, verá que de acordo com nossa abordagem, o HashMap guardará no máximo 26 pares de <Chave, Valor>, onde a chave é cada letra do alfabeto, e seu valor é o nó que o representa. Desta forma, podemos pesquisar por várias palavras que contenham o mesmo prefixo com a mesma eficiência.

**Indicador de fim de palavra:**

Este atributo é extremamente importante para o funcionamento da Trie por definir se uma palavra está presente na  estrutura ou não. Ele é um atributo booleano que define se certa letra representa o fim de uma palavra dentro da Trie. 

- **Exemplo:**
> Digamos que adicionamos a palavra "Carrossel".
> 
> Nossa estrutura ficaria assim: C -> A -> R -> R -> O -> S -> S -> E -> L

> Podemos pesquisá-la através do método search que veremos adiante, e o retorno será **true**, pois ela foi inserida. Agora caso pesquisarmos pela palavra "Carro", comumente se pensaria que ela está ao visualizar a estrutura que temos, porém ela nunca foi adicionada. Para isso que temos nosso atributo mágico "isEndOfWord", que define se certo nó que representa uma letra n também se refere ao fim de uma palavra. Nosso algoritmo olharia para o nó que representa a letra "o" da **nossa estrutura** e se perguntaria "Esta letra é o fim de uma palavra?". E no nosso caso, essa resposta seria **false**.

### 3.1.2 Trie
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
| Inserção  |  Muito alto                  |
| Remoção   |  Muito alto                  |

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
###  Lock-Based Tries
- Usa **locks finos** em cada nó para permitir múltiplas operações paralelas, ou seja, em vez de travar a estrutura toda para fazer uma operação, você trava apenas a menor parte necessária  
- Conforme aumenta o número de threads, melhor a estrutura funciona  
- Reduz a chance de threads ficarem esperando  

---

### Lock-Free Tries
- Usa **instruções atômicas**, como o **CAS**, pois evita deadlock, contenção e se torna mais seguro por ser feito em hardware  
- Evita completamente locks, mas é mais difícil de implementar  
- Melhor performance sob alta concorrência  

---

### Immutable Tries
- Cada modificação cria uma **nova versão** da estrutura  
- Threads podem acessar **versões antigas com segurança**, pois os nós são imutáveis  
- Não destroem o estado anterior de um nó  

### 5.3.3 Operações 

### Inserção
- Percorre os nós até onde a chave diverge ou termina  
- Cria novos nós se necessário  
- Em Ctries (hash tries), percorre a árvore inspecionando blocos de bits do hash  

---

### Busca
- Caminha até o final da chave  
- Se a estrutura for bem balanceada e não houver colisões (no hash), a profundidade é limitada  

---

### Remoção
- Encontra o nó da chave  
- Marca como removido  

---

### Snapshot
- Apenas aponta para o nó raiz atual  
- Como os nós são imutáveis, não há risco de inconsistência  
- Leitores podem continuar acessando a versão antiga mesmo após novas inserções  

### 5.3.4 Complexidade

| Operação     | Complexidade Média   | Pior caso | Observações                                           |
| ------------ | -------------------- | --------- | ----------------------------------------------------- |
| **Inserção** | `O(k)` ou `O(log n)` | `O(k)`    | Um nó por caractere/nível; pode haver colisões        |
| **Busca**    | `O(k)` ou `O(log n)` | `O(k)`    | Caminha até a folha correspondente                    |
| **Remoção**  | `O(k)`               | `O(k)`    | Pode envolver limpeza de nós intermediários |
| **Snapshot** | `O(1)`               | `O(1)`    | Apenas copia a referência do nó raiz (imutável)      |

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

### Inserção
- Busca o buffer correspondente ao prefixo;
- Insere a chave no buffer;
- Se ultrapassar o limite do buffer, ocorre o burst;
- Cria um novo buffer;
- Redistribui as chaves em novos nós, com base nos novos prefixos;

### Busca
- Caminha pela trie até o buffer correspondente;
- Procura a chave dentro do buffer (usando busca binária);

### Remoção
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

### Inserção
- Inicia pela raiz e compara cada caractere;
- Se menor, vai pra subárvore à esquerda;
- Se maior, vai pra subárvore à direita;
- Se igual, avança para o filho do meio.

### Busca
- Percorre cada caractere e faz as mesmas comparações da inserção;
- A palavra existe se o algoritmo chegar a um nó marcado como "fim de palavra".

### Remoção
- Faz uma busca da palavra a ser removida;
- Caso a encontre, a marca como “removida”;
- Caso contrário, não faz nada, pois a palavra a ser removida não está lá;
- Opcionalmente, ramos vazios podem ser removidos.

### Busca por prefixo
- Percorre todo o prefixo;
- Depois, faz travessia da subárvore do meio coletando palavras.

### 5.5.3 Complexidade

| Operação           | Complexidade (Tempo) | Observações                                           |
|--------------------|----------------------|--------------------------------------------------------|
| **Busca**          | O(k + h)             | `k` = tamanho da string, `h` = altura da árvore        |
| **Inserção**       | O(k + h)             | Pode precisar criar até `k` novos nós                 |
| **Remoção**        | O(k + h)             | Remove marca de fim de palavra; poda é opcional       |
| **Busca por prefixo** | O(k + n)          | `n` = nº de palavras com o prefixo; percorre subárvore|
| **Espaço (Memória)**| O(n·k)              | Menor que trie, pois só 3 ponteiros por nó            |

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




