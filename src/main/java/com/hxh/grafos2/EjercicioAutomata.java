/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hxh.grafos2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

public class EjercicioAutomata {

    ArrayList<String> estados;
    ArrayList<String> terminales;
    String letras;
    String simbolos;
    String inicial;
    private String[][] AFND;
    String[][] AFD;

    public EjercicioAutomata() {
    }

    public EjercicioAutomata(ArrayList<String> estados, ArrayList<String> terminales, String inicial, String[][] AFND) {
        this.estados = estados;
        this.terminales = terminales;
        this.inicial = inicial;
        this.AFND = AFND;
    }

    // metodo donde entra una lista con los datos ingresador por el usuario y las
    // guarda en una matriz con la primera columna el estado
    public String[][] createTransitionsMatrix(List<String> transitions) {

        int numRows = transitions.size();
        String[][] matrix = new String[numRows][letras.length() + 1]; // +1 para incluir columna 0 POSIBLE ERROR

        for (int i = 0; i < numRows; i++) {
            String transition = transitions.get(i);
            String[] parts = transition.split("-");
            matrix[i][0] = parts[0];

            String[] secondParts = parts[1].split(",");
            for (int j = 0; j < letras.length(); j++) {
                if (j < letras.length()) {
                    matrix[i][j + 1] = secondParts[j];
                }
            }
        }
        return matrix;
    }

    public String[][] Trasformar(String[][] AFND) {
        int numFilas = AFND.length;
        int numCols = AFND[0].length;
        // copiar afnd, en afd porque es mas grande
        AFD = new String[300][numCols];

        for (int row = 0; row < numFilas; row++) {
            for (int col = 0; col < numCols; col++) {
                AFD[row][col] = AFND[row][col];
            }
        }

        String[] letrasvec = letras.split("");
        int cont = 0;
        for (int i = 0; i < AFD.length; i++) {
            for (int j = 1; j < AFD[0].length; j++) {
                if (AFD[i][j] != null) {
                    if (AFD[i][0] == null) {
                        break;
                    } else {
                        if (AFD[i][j].length() > 1 && !comprobarExistenciaCadena(AFD, AFD[i][j])) {
                            String[] multiEstados = AFD[i][j].split("");
                            String[] transiciones = new String[multiEstados.length];
                            AFD[AFND.length + cont][0] = AFD[i][j];

                            for (int a = 0; a < letras.length(); a++) {
                                for (int b = 0; b < multiEstados.length; b++) {
                                    transiciones[b] = buscarEnMatriz(AFD, multiEstados[b], letrasvec[a], letras);
                                }
                                AFD[AFND.length + cont][a + 1] = combinarLetrasSinDuplicados(transiciones);
                            }
                            cont++;
                        }
                    }
                }
            }
        }
        return AFD;
    }

    // comprueba si el elemento de la matriz llamado cadena ya esta declarado como
    // estado
    // dados esos dos strings, comprueba si son iguales aunque esten en desorden
    public boolean comprobarExistenciaCadena(String[][] matriz, String cadena) {
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0] != null) {
                if (matriz[i][0].length() == cadena.length()) {
                    char[] arr1 = matriz[i][0].toCharArray();
                    char[] arr2 = cadena.toCharArray();
                    Arrays.sort(arr1);
                    Arrays.sort(arr2);
                    if (Arrays.equals(arr1, arr2)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    // metodo que al darle la letra de la fila, y de la columna devuelve su valor
    public String buscarEnMatriz(String[][] matriz, String estadoInicio, String columnaInicio, String simbolos) {
        int i, j;
        for (i = 0; i < matriz.length; i++) {
            if (matriz[i][0].equals(estadoInicio)) {
                break;
            }
        }
        // hace lo mismo de arriba pero con el string de simbolos, encuentra su indice y
        // se lo pasa a la matriz
        // para encontrar el elemento que esta en interseccion
        j = simbolos.indexOf(columnaInicio) + 1;
        return matriz[i][j];
    }

    // se usa para unir los resultados de buscar las transiciones de un estado no
    // determinado, es decir, se combinan
    // los estados individuales del estado no determinado original, para volverlo
    // una transicion.
    public static String combinarLetrasSinDuplicados(String[] letras) {
        StringBuilder resultado = new StringBuilder();
        Set<Character> letrasUsadas = new HashSet<>();

        for (String cadena : letras) {
            for (char c : cadena.toCharArray()) {
                if (!letrasUsadas.contains(c)) {
                    resultado.append(c);
                    letrasUsadas.add(c);
                }
            }
        }
        String a = resultado.toString();
        char[] chars = a.toCharArray();
        Arrays.sort(chars);
        String b = new String(chars);
        return b.toString();
    }

    public String[] buscarEstadosEnTerminales(String[][] matriz, String terminales) {
        List<String> estadosEnTerminales = new ArrayList<>();
        for (String[] fila : matriz) {
            if (fila[0] != null && fila[0].matches(".*[" + terminales + "].*")) {
                estadosEnTerminales.add(fila[0]);
            }
        }
        return estadosEnTerminales.toArray(new String[0]);
    }

    // Getters
    public ArrayList<String> getEstados() {
        return estados;
    }

    public ArrayList<String> getTerminales() {
        return terminales;
    }

    public String getInicial() {
        return inicial;
    }

    public String[][] getMatrix() {
        return AFND;
    }

    public String getsimbolos() {
        return simbolos;
    }

    public String getLetras() {
        return letras;
    }

    // Setters
    public void setsimbolos(String simbolos) {
        this.simbolos = simbolos;
    }

    public void setEstados(ArrayList<String> estados) {
        this.estados = estados;
    }

    public void setTerminales(ArrayList<String> terminales) {
        this.terminales = terminales;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }

    public void setMatrix(String[][] AFND) {
        this.AFND = AFND;
    }

    public void setLetras(String letras) {
        this.letras = letras;
    }

}
