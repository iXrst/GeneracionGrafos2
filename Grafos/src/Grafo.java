import java.util.*;
import java.awt.geom.Point2D;
import java.nio.file.*;


public class Grafo{
    private HashMap<Integer, Set<Integer>> map = new HashMap<>();

    public void addNode(int nodo) {
        map.put(nodo, new HashSet<Integer>());
    }

    public void addNodeConnections(int nodo, Set<Integer> nodos_adyacentes) {
		map.put(nodo,nodos_adyacentes);
	}

    public void delNode(int nodo){
        map.remove(nodo);
    }

    public void addEdge(int nodo_fuente, int nodo_destino, boolean bidireccional) {
        if (!map.containsKey(nodo_fuente)) {
            addNode(nodo_fuente);
        }
        if (!map.containsKey(nodo_destino)) {
            addNode(nodo_destino);
        }
        map.get(nodo_fuente).add(nodo_destino);
        if (bidireccional == true){
            map.get(nodo_destino).add(nodo_fuente);
        }
    }

    public boolean containsNode(int nodo){
        return map.containsKey(nodo);
    }

    public Set<Integer> getNodes() {
        return map.keySet();
    }

    public Set<Integer> getLinkedNodes(int nodo) {
		return map.get(nodo);
	} 

    public void getNodesCount(){ 
        System.out.println("El grafo tiene "+ map.keySet().size() + " nodos"); 
    }

    public void getEdgesCount(boolean bidirection){ 
        int count = 0; 
        for (Integer v : map.keySet()) { 
            count += map.get(v).size(); 
        } 
        if (bidirection == true){count = count / 2;}
        System.out.println("El grafo tiene " + count + " aristas"); 
    } 

    public void hasNode(int nodo){ 
        if (map.containsKey(nodo)) { 
            System.out.println("El grafo contiene "+ nodo + " como nodo"); 
        } 
        else { 
            System.out.println("El grafo no contiene "+ nodo + " como nodo"); 
        } 
    } 
  
    public void hasEdge(int nodo_fuente, int nodo_destino){ 
        if (map.get(nodo_fuente).contains(nodo_destino)) { 
            System.out.println("El grafo tiene una arista entre los nodos " + nodo_fuente + " y " + nodo_destino); 
        } 
        else { 
            System.out.println("El grafo no tiene una arista entre los nodos " + nodo_fuente + " y " + nodo_destino); 
        } 
    }

    public int valueFrequency(int nodo){
        int frequency = 0;
        for (Set<Integer> conexiones : map.values()){
            if(conexiones.contains(nodo)){frequency++;}
        }
        return frequency;
    }

    public String toString(){
        StringBuilder res = new StringBuilder("graph abstract {\n");

        for(Integer node : map.keySet()){
            res.append("\t" + node.toString() + " -- {");
            for(Integer edge : map.get(node)){
                res.append(edge.toString() + " ");
            }
            res.append("}\n");
        }
        res.append("}");
        return (res.toString());
    }

    public void toGVFile(String name, String graph){
        Path path = Paths.get(name + ".gv");
        try {
            Files.writeString(path, graph);
        } catch (Exception e) {
        }
    }

    public static Grafo genErdosRenyi(int n, int m) {
        Random r = new Random();
        Grafo g = new Grafo();
        for (int i = 0; i < n; i++) {
            g.addNode(i);
        }
        int i = 0;
        int n1 = 0;
        int n2 = 0;
        while(i<m) {
            n1 = r.nextInt(n);
            n2 = r.nextInt(n);
            while(n1 == n2) {
                n2 = r.nextInt(n);
            }
            g.addEdge(n1,n2,true);
            i++;
        }
        return g;
    }

    public static Grafo genGeografico(int n, double d) {
        Random r = new Random();
        Grafo g = new Grafo();
        Point2D[] xy = new Point2D[n];
        for (int i = 0; i < n; i++) {
            g.addNode(i);
            xy[i] = new Point2D.Double(r.nextDouble(), r.nextDouble());
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    if (xy[i].distance(xy[j]) < d) {
                        g.addEdge(i,j,true);
                    }
                }
            }
        }
        return g;
    }

    public static Grafo genGilbert(int n, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probabilidad entre 0 y 1");
        Random r = new Random();
        Grafo g = new Grafo();
        for (int i = 0; i < n; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < n - 1 ; i++) {
            for (int j = i + 1; j < n; j++) {
                if (r.nextDouble() <= p) {
                    g.addEdge(i,j,true);
                }
            }
        }
        return g;
    }

    public static Grafo genBarabasiAlbert(int n, double d) {
        Grafo g = new Grafo();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            g.addNode(i);
        }
        int k;
        for (int i = 1; i < d; i++){
            k = i - 1;
            g.addEdge(i,k,true);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j){
                    if((g.map.get(i).size() + g.valueFrequency(i) ) < d && (g.map.get(j).size() + g.valueFrequency(j)) < d){
                        if(r.nextDouble() <= (1-(g.map.get(i).size()/d))){
                            if (g.map.get(i).contains(j) || g.map.get(j).contains(i)){
                            }
                            else{
                                if (Collections.frequency(g.map.values(),i) < d && Collections.frequency(g.map.values(),j) < d)
                                g.addEdge(j,i,true);
                            }
                        }
                    }
                }
            }
        }
        return g;
    }

    public static Grafo BFS(Grafo grafo, int nodoInicio){
        Grafo arbol = new Grafo();
        List<Integer> visitado = new ArrayList<>();

        visitado.add(nodoInicio);

        for(int i = 0; i < visitado.size(); i++){       
            Set<Integer> conexiones = new HashSet<>();
            Set<Integer> adyacentes = grafo.getLinkedNodes(visitado.get(i));
            
            for(Integer a : adyacentes){                
                if(!visitado.contains(a)){
                    visitado.add(a);
                    conexiones.add(a);
                }
            }
            
            if(conexiones.size() > 0)
                arbol.addNodeConnections(visitado.get(i),conexiones); 
        }
        return arbol;
    }

    public static void DFS_recursivo(Grafo arbol, Grafo grafo, Integer nodoActual ,List<Integer> visitado){
        visitado.add(nodoActual);        
        Set<Integer> adyacentes = grafo.getLinkedNodes(nodoActual);
      
        for(Integer a : adyacentes){                
            if( !visitado.contains(a) ){
                if(arbol.containsNode(nodoActual)){
                    Set<Integer> v = arbol.getLinkedNodes(nodoActual);
                    v.add(a);
                }
                else{
                    Set<Integer> vertices = new HashSet<>();
                    vertices.add(a);
                    arbol.addNodeConnections(nodoActual,vertices);
                }                
                DFS_recursivo(arbol, grafo, a, visitado);
            }
        }
    }

    private static Grafo DFS_iterativo(Grafo grafo, int nodoInicio){
        Grafo arbol = new Grafo();
        List<Integer> visitado = new ArrayList<>();
        Stack<Integer> pila = new Stack<>(); 

        int nodoActual;
        pila.push(nodoInicio);
        
        while(pila.size() > 0){
            nodoActual = pila.peek();
            pila.pop();
            if (visitado.contains(nodoActual)) {
                continue;
            }

            visitado.add(nodoActual); 
            Set<Integer> adyacentes = grafo.getLinkedNodes(nodoActual);      
            List<Integer> adyacentes_lista = new ArrayList<>(adyacentes);
            
            // Itera sobre las conexiones del nodo
            for (int x =  adyacentes_lista.size()-1; x >= 0; x--){
                int a = adyacentes_lista.get(x);
                if( !visitado.contains(a) ){
                    pila.push(a);
                }
            }
             
            if (pila.size() > 0 && !visitado.contains(pila.peek())){
                boolean conexion = false;
                int nodo ;
                for (int x =  visitado.size()-1; x >= 0 && !conexion; x-- )
                {
                    nodo = visitado.get(x) ;
                    Set<Integer> conexiones = grafo.getLinkedNodes(nodo);
                    if (conexiones.contains(pila.peek())) {
                        conexion = true;
                        if(arbol.containsNode(nodo)){
                            Set<Integer> v = arbol.getLinkedNodes(nodo);
                            v.add(pila.peek());
                        }
                        else{
                            Set<Integer> vertices = new HashSet<>();
                            vertices.add(pila.peek());
                            arbol.addNodeConnections(nodo,vertices);
                        }              
                    }
                } 
            }            
        }
        return arbol;
    }

   
    public Grafo() {
    }


    

} 
    

