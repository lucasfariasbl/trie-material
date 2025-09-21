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

## 6.3  PLN - Processamento de Linguagem Natural
### 6.3.1 Manipulação e Análise de Texto em Grande Escala
No campo de PLN (Processamento de Linguagem Natural), sistemas precisam lidar com grandes volumes de texto para tarefas como:
- Correção ortográfica
- Sugestão de Palavras(autocompletar)
- Busca por prefixos
 
Todas essas tarefas envolvem buscas rápidas por palavras ou prefixos, principalmente em grandes conjuntos de palavras.

**Agora pense comigo:**

Imagine que temos um enorme dicionário de palavras com milhares de entradas.Precisamos armazenar essas palavras de forma eficiente e também permitir buscas rápidas, como:

-	Verificar se uma palavra está correta
- Sugerir palavras a partir de um prefixo digitado
- Encontrar todas as palavras que começam com um determinado trecho

**Onde a Trie poderia nos ajudar?**

Bom,como já sabemos A Trie é uma estrutura de dados ideal para representar grandes conjuntos de strings com prefixos em comum, que é exatamente o que acontece em muitas palavras de um idioma.Cada nó da Trie representa uma letra do alfabeto, e cada caminho da raiz até um nó terminal representa uma palavra completa.
Suponha que queremos armazenar as seguintes palavras:
- casa
- casamento  
- casaco
- caminho 
- carro  
- carta
  
A Trie que será construída terá ramos em comum para os prefixos repetidos (por exemplo, "ca", "cas", "cam"), como acontece frequentemente na linguagem natural.
Assim como em sequências de DNA, as palavras podem compartilhar partes idênticas no início, e a Trie aproveita isso para otimizar o armazenamento.Em vez de repetir cada letra para cada palavra, a Trie armazena os prefixos compartilhados apenas uma vez.Tendo assim um melhor uso do armazenamento e muita Eficiência na busca por palavras.

## 6.4 Algoritmos de Busca e Ordenação 
### 6.4.1 Algortimo de Aho-Corasick
O algoritmo aho-corasick é um algoritmo de busca de strings criado em 1975 por Alfred V. Aho e Margaret J. Corasick.Ele é um algoritmo que nos permite localizar ocorrências de múltiplas strings (0 dicionário") dentro de um texto de entrada.O algoritmo realiza a busca por todas as strings ao mesmo tempo, tornando-se eficiente para essa tarefa. Sua complexidade é linear em relação ao tamanho das strings do dicionário,mais o comprimento do texto pesquisado mais o número de ocorrências encontradas. No entanto, caso as substrings a serem buscadas se sobreponham, como no caso de um dicionário com "a", "aa", "aaa", "aaaa" e um texto "aaaa", o número de correspondências pode crescer de forma quadrática.

**Como ele funciona**
#### Contrução da Trie
O Aho-Corasick começa construindo um Trie a partir do conjunto de padrões (palavras) que se dejeja buscar no texto.Cada caminho da raiz até um nó terminal da Trie representa uma correspondência de um padrão.Se tivermos os padrões "cat", "car", "bat" e "rat", a Trie seria construída da seguinte maneira:
<img src="assets/trie Aho-corasick.jpg" height="600">

#### Links de Falha 
Depois de construir a Trie,o algoritmo adiciona links de falha aos nós da Trie. Esses links são utilizados para otimizar o processo de busca.

Links de falha: Quando o algoritmo encontra uma correspondência parcial, mas não completa, ele segue o link de falha para tentar outra possibilidade, sem precisar retroceder. Isso é crucial para garantir que a busca ocorra de maneira eficiente mesmo quando há falhas em algumas correspondências.

Por exemplo, se estivermos buscando a palavra "rat" no texto e chegarmos ao nó correspondente ao prefixo "ra", mas não houver uma correspondência direta com o próximo caractere "t", o link de falha ajudará o algoritmo a continuar a busca a partir de outro ponto da Trie que ainda seja válido para o caractere "t".

#### Busca no Texto
Uma vez que a Trie e os links de falha estão construídos, o algoritmo começa a percorrer o texto de entrada.

**Primeiro passo:** Processamento do Texto
O algoritmo começa lendo o texto caractere por caractere. Para cada caractere, ele o compara com os nós da Trie.

O que acontece se o caractere do texto corresponder ao nó da Trie?

Simplesmente, o algoritmo segue em frente, avançando para o próximo caractere do texto. Ele continua a busca dessa maneira, procurando o padrão em sequência.

Agora, você pode estar se perguntando:
"E se o caractere no texto não corresponder a nenhum nó na Trie? O que o algoritmo deve fazer?"

**Segundo passo** Falha na Correspondência
Quando o caractere do texto não encontra uma correspondência direta na Trie, o algoritmo não retrocede nem começa a busca de novo. Em vez disso, ele usa um mecanismo inteligente chamado link de falha.
Mas como isso funciona?
> O link de falha é uma espécie de "atalho" que conecta o nó atual da Trie a um nó anterior que pode ter um prefixo comum com o texto atual.
>
> O algoritmo então continua a busca a partir desse novo ponto, tentando corresponder o próximo caractere do texto.
>

Veja o Exemplo:
Imagina que o algoritmo está buscando a palavra "rat" no texto "ratatouille". O algoritmo começa a procurar e encontra o prefixo "ra" no texto.
> Mas o próximo caractere do texto é "t". O algoritmo encontra a letra "t" na Trie e avança para ela. Agora, se o próximo caractere do texto for "l", que não corresponde ao "t" da Trie, o algoritmo não volta ao início.
>
> Ao invés disso, o algoritmo segue o link de falha do nó "ra" até o nó correspondente ao prefixo "r". Ele então continua a busca para o próximo caractere "l", economizando tempo e evitando um retrocesso desnecessário.
> 

**Terceiro passo** Busca Simultânea de Múltiplos Padrões
uma das principais vantagens do algoritmo Aho-Corasick é que ele não está apenas buscando uma palavra, mas sim vários padrões ao mesmo tempo.

Mas como isso é possivel ?
O algoritmo verifica todos os padrões presentes na Trie simultaneamente enquanto percorre o texto.

Em vez de verificar um padrão de cada vez, ele pode verificar múltiplos padrões ao mesmo tempo, utilizando a estrutura da Trie para armazenar e organizar esses padrões de forma eficiente.

Isso torna a busca muito mais rápida, já que ele não precisa repetir a busca para cada palavra. Ele pode verificar todos os padrões ao mesmo tempo, economizando tempo, especialmente quando há um grande número de palavras a serem verificadas em um único texto.

### 6.4.2 Burst Sort
O Burst Sort é um algoritmo de ordenação eficiente, especialmente para grandes conjuntos de strings ou palavras, onde há muitos prefixos em comum. Criado para lidar com grandes volumes de dados, o Burst Sort divide as strings em "pedaços" e organiza essas partes de maneira otimizada, aproveitando a estrutura de Trie para ordenar eficientemente.

**Como ele Funciona**
#### Divisão das Strings em Pedaços
O Burst Sort começa dividindo as strings em "pedaços" menores. Cada "pedaço" é então armazenado em uma estrutura adequada para ordenação eficiente, como uma Trie.

Porque dividir as Strings?
Ao dividir as strings, o algoritmo consegue otimizar o uso de memória e tornar a ordenação mais rápida, especialmente quando muitas strings compartilham prefixos comuns.

Após dividir as strings, o Burst Sort armazena cada "pedaço" em uma Trie. A Trie organiza as strings de forma eficiente, aproveitando os prefixos compartilhados para otimizar tanto a memória quanto o tempo de ordenação.



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




