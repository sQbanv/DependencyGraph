# Temat: Teoria współbierzności - zadanie domowe 1 - Dependency Graph
- - -
# Autor: Dariusz Cebula
- - - 

## 1. Język:

Program został napisany w javie (ver 17), kompilacja w Gradle

## 2. Uruchomienie programu:

W folderze głównym aplikacji wystarczy wywołać

```
./gradlew run
```

może być wymagane prawo do wykonania, wtedy

```
chmod +x gradlew
```

Pliki wejściowe w formacie `.txt` należy dać do folderu `src/main/resources/inputFiles`. Format pliku poniżej

## 3. Działanie programu:

Program na wejściu wczytuje wszystkie pliki `.txt` w folderze `src/main/resources/inputFiles`

### 3.1 Format pliku txt:

Przykład:

```text
(a) x := x + 1
(b) y := y + 2z
(c) x := 3x + z
(d) w := w + v
(e) z := y - z
(f) v := x + v
A = {a, b, c, d, e, f}
w = acdcfbbe
```

gdzie:

- na początku podajemy w nowy liniach transakcje w formacie `(a) x := OPERACJE`,
- pod transakcjami podajemy alfabet w formacie `A = {a, b, ...}`,
- na koniec słowo w formacie `w = abc....`

### 3.2 Poszczególne kroki programu:

1. Oblicza zbiór zależności i niezależności na podstawie podanych transakcji
2. Na podstawie zbioru zależności i podanego słowa `w` wyznacza graf zależności 
3. Na podstawie grafu zależności wyznacza postać normalną Foaty `FNF`

### 3.3 Szczegółowy opis działania programu:

1. Obliczanie zbioru zależnośći i niezależności

```java
public class Dependency {
    private final List<List<Character>> dependencyList = new ArrayList<>();
    private final List<List<Character>> independencyList = new ArrayList<>();

    public Dependency(List<Transaction> transactions) {
        calculateDependencies(transactions);
    }

    private void calculateDependencies(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            transactions.forEach(tr -> {
                List<Character> item = List.of(transaction.getId(), tr.getId());
                if(transaction.getId() == tr.getId()){
                    dependencyList.add(item);
                } else if (tr.getRightActions().contains(transaction.getLeftAction()) || transaction.getRightActions().contains(tr.getLeftAction())) {
                    dependencyList.add(item);
                } else {
                    independencyList.add(item);
                }
            });
        });
    }

    public List<List<Character>> getDependencyList() {
        return dependencyList;
    }

    public List<List<Character>> getIndependencyList() {
        return independencyList;
    }
}
```
Do klasy `Dependency` podajemy listę transakcji i na ich podstawie wyznacza pomiędzy nimi zależności. 
W razie braku dodaje ją do zbioru niezależności

Poniżej klasa `Transaction`
```java
public class Transaction {
    private final char id;
    private final char leftAction;
    private final Set<Character> rightActions;

    public Transaction(char id, char leftAction, Set<Character> rightActions){
        this.id = id;
        this.leftAction = leftAction;
        this.rightActions = rightActions;
    }

    public char getId() {
        return id;
    }

    public char getLeftAction(){
        return leftAction;
    }

    public Set<Character> getRightActions() {
        return rightActions;
    }

    @Override
    public String toString() {
        return "(%c) %c := %s".formatted(id,leftAction,rightActions.toString());
    }
}
```

2. Wyznaczenie grafu zależności

```java
public class DependencyGraph {

    public Graph createDependencyGraph(String word, List<List<Character>> dependencyList) {
        Graph graph = new Graph();

        for (int i=0; i<word.length(); i++) {
            for (int j=i+1; j<word.length(); j++){
                if (dependencyList.contains(List.of(word.charAt(i), word.charAt(j)))) {
                    graph.addEdge(i, j);
                }
            }
        }

        DFS(graph);

        return graph;
    }

    private void DFS(Graph graph){
        Set<Integer> visited = new HashSet<>();
        List<Integer> edgesToRemove = new ArrayList<>();

        for (int u : graph.getAdjacencyList().keySet()) {
            visited.clear();
            edgesToRemove.clear();

            for (int v : graph.getAdjacencyList().get(u)) {
                if (visited.contains(v)) {
                    edgesToRemove.add(v);
                }
                DFSVisit(graph, v, visited);
            }

            for (int v : edgesToRemove) {
                graph.removeEdge(u, v);
            }
        }
    }

    private void DFSVisit(Graph graph, int u, Set<Integer> visited) {
        visited.add(u);

        for (int v : graph.getAdjacencyList().get(u)) {
            if (!visited.contains(v)) {
                DFSVisit(graph, v, visited);
            }
        }
    }
}
```
Do funkcji `Dependency#createDependencyGraph()` przekazujemy podane słowo `w` i listę zależności `D`. 
Funkcja po kolei po każdej literze przechodzi sprawdzając zależność z każdą literą po jej prawej. 
Dzięki temu powstaje graf DAG, z któego następnie za pomocą zmodyfikowanej funkcji DFS z usuwaniem krawędzi zwracamy graf zależności

Poniżej klasa `Graph` w postaci listy sąsiedztwa:
```java
public class Graph {
    private final Map<Integer, List<Integer>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(int source, int target) {
        if (!adjacencyList.containsKey(source)) {
            adjacencyList.put(source, new ArrayList<>());
            adjacencyList.get(source).add(target);
        } else {
            adjacencyList.get(source).add(target);
        }

        if (!adjacencyList.containsKey(target)) {
            adjacencyList.put(target, new ArrayList<>());
        }
    }

    public void removeEdge(int source, int target) {
        if (adjacencyList.containsKey(source) && adjacencyList.get(source).contains(target)) {
            adjacencyList.get(source).remove(Integer.valueOf(target));
        }
    }

    public Graph getInvertedGraph() {
        Graph invertedGraph = new Graph();

        for (int source : adjacencyList.keySet()) {
            for (int target : adjacencyList.get(source)) {
                invertedGraph.addEdge(target, source);
            }
        }

        return invertedGraph;
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public String toDot(String labels) {
        StringBuilder dot = new StringBuilder("digraph g {\n");

        for (int source : adjacencyList.keySet()) {
            for (int target : adjacencyList.get(source)) {
                dot.append("%d -> %d\n".formatted(source, target));
            }
        }

        for (int i=0; i<labels.length(); i++) {
            dot.append("%d[label=%c]\n".formatted(i, labels.charAt(i)));
        }

        dot.append("}");

        return dot.toString();
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }
}

```

3. Wyznaczenie postaci normalnej Foaty `FNF`

```java
public class FNF {

    public static List<List<Character>> getFNF(Graph graph, String labels) {
        Map<Integer, Integer> group = new HashMap<>();
        Graph invertedGraph = graph.getInvertedGraph();

        for (int source : invertedGraph.getAdjacencyList().keySet()) {
            if (invertedGraph.getAdjacencyList().get(source).isEmpty()) {
                group.put(source, 0);
            } else {
                int max = 0;
                for (int target : invertedGraph.getAdjacencyList().get(source)) {
                    max = Math.max(max, group.get(target) + 1);
                }
                group.put(source, max);
            }
        }

        int groups = Collections.max(group.values());

        List<List<Character>> fnf = new ArrayList<>();

        for (int i=0; i<groups+1; i++){
            fnf.add(i, new ArrayList<>());
        }

        for (int v : group.keySet()){
            fnf.get(group.get(v)).add(labels.charAt(v));
        }

        return fnf;
    }
}
```
Do funckji `FNF#getFNF()` przekazujemy wyznaczony wcześniej graf zależności i podane słowo `w`.
Przechodząc po odwróconym (krawędzie na odwrót) grafie zależności sprawdzamy czy dane słowo jest zależne od innego.
Jeśli zbiór krawędzi jest pusty to możemy dodać takie słowo do grupy pierwszej, 
w przeciwnym przypadku sprawdzamy od czego jest zależny i zapisujemy go do najdalszej grupy któa wystąpiła + 1. 
Wszystko to realizuje na HashMap, gdzie klucz to wierzchołek grafu, a wartość to grupa do któej należy
Następnie konwertuję to na `List<List<Character>>`

### 3.4 Output:

Program zwraca wyniki dla każdego pliku `.txt`. W folderze `src/main/resources/outputResults` tworzy dla 
każdego pliku osobny folder wynikowy a w nim:

1. Tworzy plik `results.txt`

Przykład:

```text
D = [[a, a], [a, c], [a, f], [b, b], [b, e], [c, a], [c, c], [c, e], [c, f], [d, d], [d, f], [e, b], [e, c], [e, e], [f, a], [f, c], [f, d], [f, f]]
I = [[a, b], [a, d], [a, e], [b, a], [b, c], [b, d], [b, f], [c, b], [c, d], [d, a], [d, b], [d, c], [d, e], [e, a], [e, d], [e, f], [f, b], [f, e]]
FNF([w]) = [[a, d, b], [c, b], [c], [f, e]]
digraph g {
0 -> 1
1 -> 3
2 -> 4
3 -> 4
3 -> 7
5 -> 6
6 -> 7
0[label=a]
1[label=c]
2[label=d]
3[label=c]
4[label=f]
5[label=b]
6[label=b]
7[label=e]
}
```

gdzie:

- W pierwszej linii `D` oznacza zbiór zależności,
- W drugiej linii `I` oznacza zbiór niezależności,
- W trzeciej linii `FNF([w])` oznacza postać normalną Foaty. Gdzie każda lista oznacza kolejną grupę,
- Następnie graf zależności w formacie `.dot`

2. Tworzy plik `graph.dot` z grafem zależności

Przykład:

```
digraph g {
0 -> 1
1 -> 3
2 -> 4
3 -> 4
3 -> 7
5 -> 6
6 -> 7
0[label=a]
1[label=c]
2[label=d]
3[label=c]
4[label=f]
5[label=b]
6[label=b]
7[label=e]
}
```

3. Tworzy plik `graph.png` z grafem zależności

Przykład:

![](img/graph.png)

4. Wypisuje wyniki w konsoli.

## 4. Wynik wywołania programu dla przykładowych danych

### Dane testowe 1

```
(a) x := x + 1
(b) y := y + 2z
(c) x := 3x + z
(d) w := w + v
(e) z := y - z
(f) v := x + v
A = {a, b, c, d, e, f}
w = acdcfbbe
```

Wyniki:

```
D = [[a, a], [a, c], [a, f], [b, b], [b, e], [c, a], [c, c], [c, e], [c, f], [d, d], [d, f], [e, b], [e, c], [e, e], [f, a], [f, c], [f, d], [f, f]]
I = [[a, b], [a, d], [a, e], [b, a], [b, c], [b, d], [b, f], [c, b], [c, d], [d, a], [d, b], [d, c], [d, e], [e, a], [e, d], [e, f], [f, b], [f, e]]
FNF([w]) = [[a, d, b], [c, b], [c], [f, e]]
digraph g {
0 -> 1
1 -> 3
2 -> 4
3 -> 4
3 -> 7
5 -> 6
6 -> 7
0[label=a]
1[label=c]
2[label=d]
3[label=c]
4[label=f]
5[label=b]
6[label=b]
7[label=e]
}
```

![](img/graph.png)

### Dane testowe 2

```
(a) x := x + y
(b) y := z - v
(c) z := v * x
(d) v := x + 2y
(e) x := 3y + 2x
(f) v := v - 2z
A = {a,b,c,d,e,f}
w = afaeffbcd
```

Wyniki:

```
D = [[a, a], [a, b], [a, c], [a, d], [a, e], [b, a], [b, b], [b, c], [b, d], [b, e], [b, f], [c, a], [c, b], [c, c], [c, d], [c, e], [c, f], [d, a], [d, b], [d, c], [d, d], [d, e], [d, f], [e, a], [e, b], [e, c], [e, d], [e, e], [f, b], [f, c], [f, d], [f, f]]
I = [[a, f], [e, f], [f, a], [f, e]]
FNF([w]) = [[a, f], [a, f], [e, f], [b], [c], [d]]
digraph g {
0 -> 2
1 -> 4
2 -> 3
3 -> 6
4 -> 5
5 -> 6
6 -> 7
7 -> 8
0[label=a]
1[label=f]
2[label=a]
3[label=e]
4[label=f]
5[label=f]
6[label=b]
7[label=c]
8[label=d]
}
```

![](img/graph2.png)