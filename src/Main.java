import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static void main(String[] args){
        //MODELADO DEL PROBLEMA
        int s = 3; //Nº DE SIMBOLOS DEL ALFABETO
        int r = 5; //LONG DE LA CADENA

        //PARAMETROS DEL ALGORITMO
        int n = 4; //TAMAÑO DE LA POBLACION
        double pc = 0.8; //PROBABILIDAD DE CRUCE
        double pm = 1 / (r*n); //PROBABILIDAD DE MUTACION
        int t_max = 1;
        int num = 1000; //Nº ORIGINAL DE CASILLAS DE LA RULETA

        //INICIO DEL ALGORITMO
        int t=0;
        int[][] w = new int[n][r];
        for(int i=0; i<n; i++){
            for(int j=0; j<r; j++){
                w[i][j] = (int) Math.floor(Math.random()*s);
            }
        }
        int[] apt = new int[n];
        for(int i=0; i<n; i++){apt[i] = funcionAptitud(w,i,r);}

        int[] apt_gen = new int[t_max+1];
        apt_gen[0]=0;
        for(int i=0; i<n; i++){apt_gen[0]+=apt[i];}

        double[] apt_m_gen = new double[t_max+1];
        apt_m_gen[0] = apt_gen[0]/n;

        //CUERPO DEL ALGORITMO
        while(t<t_max){
            //SELECCION
            int[] p = new int[n];
            p[0] = apt[0] / apt_gen[t];
            int[] c = new int[n];
            c[0] = (int) Math.floor(p[0]*num) + 1;
            int[] alfa = new int[n];
            alfa[0] = 0;
            int[] beta = new int[n];
            beta[0] = alfa[0]+c[0]-1;

            for(int i=1; i<n; i++){
                p[i] = apt[i] / apt_gen[t];
                c[i] = (int) Math.floor(p[i]*num) + 1;
                alfa[i] = alfa[i-1] + c[i-1];
                beta[i] = alfa[i] + c[i] -1;
            }

            int num_real_casillas = beta[n-1] + 1;

            //SELECCION DE INDIVIDUOS
            int[][] w_new = new int[n][r];
            for(int j=0; j<n; j++){
                int cas = (int) Math.floor(Math.random()*num_real_casillas);
                int i=-1;
                for(int x=0; x<n; x++){
                    if(cas>=alfa[x] && cas<=beta[x]){i=x;}
                }
                for(int x=0; x<r; x++){
                    w_new[j][x] = w[i][x];
                }
            }
            for(int i=0; i<n; i++){
                for(int x=0; x<r; x++){
                    w[i][x] = w_new[i][x];
                }
            }
            //FIN SELECCION
            //CROSSOVER
            //MUTACION
            t++;

            for(int i=0; i<n; i++){apt[i] = funcionAptitud(w,i,r);}
            apt_gen[t]=0;
            for(int i=0; i<n; i++){apt_gen[t]+=apt[i];}
            apt_m_gen[t] = apt_gen[t]/n;
        }

        //SOLUCION (MEJOR INDIVIDUO)
        int mejorPos = max(apt);
        int mejor_a = apt[mejorPos];
        String mejor_w = "";
        for(int i=0; i<r; i++){
            mejor_w+=w[mejorPos][i];
        }
        System.out.println("Mejor aptitud: "+mejor_a + " - Mejor cadena: " + mejor_w);



    }

    private static int funcionAptitud(int[][] w, int x, int r){
        //FUNCION DE APTITUD: LONGUITUD DE 2'S EN LA CADENA
        int out=0;
        for(int i=0; i<r; i++){
            if(w[x][i]==2) out++;
        }
        return out;
    }

    private static int max(int[] x){
        int max = -1; int pos = -1;
        for(int i=0; i<x.length; i++){
            if(max<x[i]){
                max = x[i];
                pos=i;
            }
        }
        return pos;
    }
}