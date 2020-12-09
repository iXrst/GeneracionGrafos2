import java.util.*;
import java.awt.geom.Point2D;
import java.nio.file.*;


public class Grafo{
    private HashMap<Integer, Set<Integer>> map = new HashMap<>();
    public static void main(String[] args) {
        Grafo g4_1 = Grafo.genBarabasiAlbert(30, 10);
        g4_1.toGVFile("genBarabasi30", g4_1.toString());
        Grafo g4_2 = Grafo.genBarabasiAlbert(100, 10);
        g4_2.toGVFile("genBarabasi100", g4_2.toString());
        Grafo g4_3 = Grafo.genBarabasiAlbert(500, 10);
        g4_3.toGVFile("genBarabasi500", g4_3.toString());

        //BFS
        Grafo bfs4_1 = Grafo.BFS(g4_1, 8);
        bfs4_1.toGVFile("arbolgenBarabasi30_bfs", bfs4_1.toString());
        //DFS_R
        List<Integer> visitado1 = new ArrayList<>();
        Grafo dfsr4_1 = new Grafo();
        Grafo.DFS_recursivo(dfsr4_1, g4_1, 8, visitado1);
        dfsr4_1.toGVFile("arbolgenBarabasi30_dfs_r", dfsr4_1.toString());
        //DFS_I
        Grafo dfsi4_1 = Grafo.DFS_iterativo(g4_1, 8);
        dfsi4_1.toGVFile("arbolgenBarabasi30_dfs_i", dfsi4_1.toString());
        
        //BFS
        Grafo bfs4_2 = Grafo.BFS(g4_2, 8);
        bfs4_2.toGVFile("arbolgenBarabasi100_bfs", bfs4_2.toString());
        //DFS_R
        List<Integer> visitado2 = new ArrayList<>();
        Grafo dfsr4_2 = new Grafo();
        Grafo.DFS_recursivo(dfsr4_2, g4_2, 8, visitado2);
        dfsr4_2.toGVFile("arbolgenBarabasi100_dfs_r", dfsr4_2.toString());
        //DFS_I
        Grafo dfsi4_2 = Grafo.DFS_iterativo(g4_2, 8);
        dfsi4_2.toGVFile("arbolgenBarabasi100_dfs_i", dfsi4_2.toString());

        //BFS
        Grafo bfs4_3 = Grafo.BFS(g4_3, 8);
        bfs4_3.toGVFile("arbolgenBarabasi500_bfs", bfs4_3.toString());
        //DFS_R
        List<Integer> visitado3 = new ArrayList<>();
        Grafo dfsr4_3 = new Grafo();
        Grafo.DFS_recursivo(dfsr4_3, g4_3, 8, visitado3);
        dfsr4_3.toGVFile("arbolgenBarabasi500_dfs_r", dfsr4_3.toString());
        //DFS_I
        Grafo dfsi4_3 = Grafo.DFS_iterativo(g4_3, 8);
        dfsi4_3.toGVFile("arbolgenBarabasi500_dfs_i", dfsi4_3.toString());
    }

    public void addNode(int vertice) {
        map.put(vertice, new HashSet<Integer>());
    }

    public void addNodeConnections(int vertice, Set<Integer> adyacentes) {
		map.put(vertice,adyacentes);
	}

    public void delNode(int vertice){
        map.remove(vertice);
    }

    public void addEdge(int fuente, int destino, boolean bidireccional) {
        if (!map.containsKey(fuente)) {
            addNode(fuente);
        }
        if (!map.containsKey(destino)) {
            addNode(destino);
        }
        map.get(fuente).add(destino);
        if (bidireccional == true){
            map.get(destino).add(fuente);
        }
    }

    public boolean containsNode(int vertice){
        return map.containsKey(vertice);
    }

    public Set<Integer> getNodes() {
        return map.keySet();
    }

    public Set<Integer> getLinkedNodes(int vertice) {
		return map.get(vertice);
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

    public void hasNode(int s){ 
        if (map.containsKey(s)) { 
            System.out.println("El grafo contiene "+ s + " como nodo"); 
        } 
        else { 
            System.out.println("El grafo no contiene "+ s + " como nodo"); 
        } 
    } 
  
    public void hasEdge(int s, int d){ 
        if (map.get(s).contains(d)) { 
            System.out.println("El grafo tiene una arista entre los nodos " + s + " y " + d); 
        } 
        else { 
            System.out.println("El grafo no tiene una arista entre los nodos " + s + " y " + d); 
        } 
    }

    public void toGVFile(String name, String graph){
        Path path = Paths.get(name + ".gv");
        try {
            Files.writeString(path, graph);
        } catch (Exception e) {
        }
    }

    public int valueFrequency(int vertice){
        int frequency = 0;
        for (Set<Integer> conexiones : map.values()){
            if(conexiones.contains(vertice)){frequency++;}
        }
        return frequency;

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
        // Funciona para no bidireccionales
        // for (int i = 0; i < d; i++) {
        //     for (int j = 0; j < d; j++) {
        //         if (i != j) {
        //             if (g.map.get(i).contains(j) || g.map.get(j).contains(i)){
        //             }
        //             else{
        //                 g.addEdge(i,j,false);
        //             }
        //         }
        //     }
        // }
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
                }else{
                    Set<Integer> newV = new HashSet<>();
                    newV.add(a);
                    arbol.addNodeConnections(nodoActual,newV);
                }                
                DFS_recursivo(arbol, grafo, a, visitado);
            }
        }
    }

    public static void util(Grafo arbol, Grafo grafo, Integer destino ,List<Integer> visitado ) {
        boolean seCreoConexion = false;
        int nodo ;
        for (int x =  visitado.size()-1; x >= 0 && !seCreoConexion; x-- )
        {
            nodo = visitado.get(x) ;
            Set<Integer> conexiones = grafo.getLinkedNodes(nodo);
            if (conexiones.contains(destino)) {
                seCreoConexion = true;
                if(arbol.containsNode(nodo)){
                    Set<Integer> v = arbol.getLinkedNodes(nodo);
                    v.add(destino);
                }else{
                    Set<Integer> newV = new HashSet<>();
                    newV.add(destino);
                    arbol.addNodeConnections(nodo,newV);
                }              
            }
        }  
    }

    private static Grafo DFS_iterativo (Grafo grafo, int nodoInicio){
        Grafo arbol = new Grafo();
        List<Integer> visitado = new ArrayList<>();
        Stack<Integer> porVisitar = new Stack<>(); 
        int nodoActual ;
        porVisitar.push(nodoInicio);
        
        while(  0 < porVisitar.size() ){
            //Obtiene el nodo que tiene que visitar
            nodoActual = porVisitar.peek();
            porVisitar.pop();
            // Verifica que el nodo no sea visitado
            if ( visitado.contains(nodoActual)) {
                continue;
            }
            //Agrega el nodo a visitados 
            visitado.add(nodoActual); 
            
            //Obtiene las conexiones del nodo
            Set<Integer> ady = grafo.getLinkedNodes(nodoActual);      
            List<Integer> adyacentes = new ArrayList<>(ady);
            
            // Itera sobre las conexiones del nodo
            for (int x =  adyacentes.size()-1; x >= 0; x-- )
            {
                int a = adyacentes.get(x);
                
                if( !visitado.contains(a) ){
                    //Agrega a nodo por visitar a una cola
                    porVisitar.push(a);
                }
            }
             
            if (porVisitar.size() > 0 &&  !visitado.contains( porVisitar.peek() ) ) {
                util(arbol, grafo, porVisitar.peek(), visitado); 
            }            
        }
        return arbol;
    }

   
    public Grafo() {
    }


    

} 
    

