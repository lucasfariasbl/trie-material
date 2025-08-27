# 1 Motivação
# 2 Introdução
# 3 Implementação

# 4 Comparações
# 5 Variações e otimizações
# 6 Aplicações no mundo real
<img src="assets/trie_redes.gif" height="800">
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



