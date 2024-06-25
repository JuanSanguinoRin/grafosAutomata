/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hxh.grafos2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Automata {
    private Set<String> estadosFinales;
    private Map<String, List<String>> estados;
    private String[] simbolos;
    private String inicial;
    static String[] vecEstados;

    public Automata(String[] simbolos, String[][] transiciones, String inicial) {
        this.simbolos = simbolos;
        this.inicial = inicial;
        this.estados = new HashMap<>();
        this.estadosFinales = new HashSet<>(Arrays.asList(vecEstados));
        for (String[] transicion : transiciones) {
            String estado = transicion[0];
            List<String> transicionesEstado = new ArrayList<>();

            for (int i = 1; i < transicion.length; i++) {
                transicionesEstado.add(transicion[i]);
            }

            estados.put(estado, transicionesEstado);
        }
    }

    public void graficar() {
        // Crea un string que represente el grafo en formato DOT
        StringBuilder dot = new StringBuilder("digraph automata {\n");
        dot.append("    rankdir=LR;\n");
        // Agrega un nodo invisible para la flecha del estado inicial
        dot.append("    \"\" [shape=none];\n");
        dot.append("    \"\" -> ").append(inicial).append(";\n");
        for (Map.Entry<String, List<String>> entry : estados.entrySet()) {
            String estado = entry.getKey();
            List<String> transiciones = entry.getValue();

            // Cambia el estilo del nodo si es un estado final
            if (estadosFinales.contains(estado)) {
                dot.append("    ").append(estado).append(" [label=\"").append(estado).append("\", shape=doublecircle];\n");
            } else {
                dot.append("    ").append(estado).append(" [label=\"").append(estado).append("\"];\n");
            }

            for (int i = 0; i < transiciones.size(); i++) {
                String transicion = transiciones.get(i);
                dot.append("    ").append(estado).append(" -> ").append(transicion).append(" [label=\"").append(simbolos[i]).append("\"];\n");
            }
        }
        dot.append("}");

        // Utiliza Graphviz para generar la imagen
        String dotFile = "automata.dot";
        String pngFile = "automata.png";

        try {
            Files.write(Paths.get(dotFile), dot.toString().getBytes());
            Runtime.getRuntime().exec("dot -Tpng " + dotFile + " -o " + pngFile);
        } catch (IOException e) {
            System.err.println("Error al generar la imagen: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String[] simbolos = {"0", "1", "2"};
        EjercicioAutomata T = new EjercicioAutomata();
        List<String> transitions = new ArrayList<>();
        T.setLetras("012");
        transitions.add("X-Y,Z,W");
        transitions.add("Y-ZK,W,Y");
        transitions.add("Z-WK,Y,K");
        transitions.add("W-YZ,ZY,K");
        transitions.add("K-Z,Y,K");
        String[][] a = T.createTransitionsMatrix(transitions);
        String[][] transiciones = T.Trasformar(a);
        
        String[][] b = filtrarMatriz(transiciones);
        
        vecEstados = T.buscarEstadosEnTerminales(b, "W");
        String[][] c = ordenarElementosMatriz(b);
        for (String[] rows : c) {
            if (rows[0] == null){
                break;
            } else {
                System.out.println(Arrays.toString(rows));
            }
        }
        String inicial = "X";
        Automata automata = new Automata(simbolos, c, inicial);
        automata.graficar();
    }

    public static String[][] filtrarMatriz(String[][] matriz) {
        List<String[]> filasFiltradas = new ArrayList<>();
        for (String[] fila : matriz) {
            if (fila != null && fila.length > 0) {
                List<String> filaFiltrada = new ArrayList<>();
                for (String valor : fila) {
                    if (valor != null && !valor.isEmpty()) {
                        filaFiltrada.add(valor);
                    }
                }
                if (!filaFiltrada.isEmpty()) {
                    filasFiltradas.add(filaFiltrada.toArray(new String[0]));
                }
            }
        }
        return filasFiltradas.toArray(new String[0][]);
    }
    
    public static String[][] ordenarElementosMatriz(String[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != null) {
                    char[] caracteres = matriz[i][j].toCharArray();
                    Arrays.sort(caracteres);
                    matriz[i][j] = new String(caracteres);
                }
            }
        }
        return matriz;
    }
}
