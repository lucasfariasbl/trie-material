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

> **Exemplo:**
> Digamos que adicionamos a palavra "Carrossel". 

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




