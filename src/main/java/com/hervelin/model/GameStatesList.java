package com.hervelin.model;

import com.hervelin.NeuralNetwork.NeuralNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//    private static final String FILE_HEADER = "Points de vie du turnPlayer faibles, Points d'liste10 du turnPlayer faibles, Points d'attaque du turnPlayer faibles, Points de mouvement du turnPlayer faibles, Mouvement max possible, Nombre d'armes possédées, Meilleure arme dispo, liste8 à portée, liste9 à portée, liste10 à portée, Joueur adverse à portée, Joueur adverse à portée de liste12, Joueur adverse à portée d'liste13, turnPlayer à portée de liste12, turnPlayer à portée d'liste13, liste17 possible, Action réalisée,POSX,POSY ";
public class GameStatesList {
    List<Double> liste1 = new ArrayList<Double>();
    List<Double> liste2 = new ArrayList<Double>();
    List<Double> liste3 = new ArrayList<Double>();
    List<Double> liste4 = new ArrayList<Double>();
    List<Double> liste5 = new ArrayList<Double>();
    List<Double> liste6 = new ArrayList<Double>();
    List<Double> liste7 = new ArrayList<Double>();
    List<Double> liste8 = new ArrayList<Double>();
    List<Double> liste9 = new ArrayList<Double>();
    List<Double> liste10 = new ArrayList<Double>();
    List<Double> liste11 = new ArrayList<Double>();
    List<Double> liste12 = new ArrayList<Double>();
    List<Double> liste13 = new ArrayList<Double>();
    List<Double> liste14 = new ArrayList<Double>();
    List<Double> liste15 = new ArrayList<Double>();
    List<Double> liste16 = new ArrayList<Double>();
    List<Double> liste17 = new ArrayList<Double>();
    List<Double> action = new ArrayList<Double>();
    /*List<Double> PosX = new ArrayList<Double>();
    List<Double> PosY = new ArrayList<Double>();*/


    public void reset() {
        liste1.clear();
        liste2.clear();
        liste3.clear();
        liste4.clear();
        liste5.clear();
        liste6.clear();
        liste7.clear();
        liste8.clear();
        liste9.clear();
        liste10.clear();
        liste11.clear();
        liste12.clear();
        liste13.clear();
        liste14.clear();
        liste15.clear();
        liste16.clear();
        liste17.clear();
        action.clear();
        /*PosX.clear();
        PosY.clear();*/
    }

    public void add(ArrayList<Double> list) {
        if(list!=null) {
            liste1.add(list.get(0));
            liste2.add(list.get(1));
            liste3.add(list.get(2));
            liste4.add(list.get(3));
            liste5.add(list.get(4));
            liste6.add(list.get(5));
            liste7.add(list.get(6));
            liste8.add(list.get(7));
            liste9.add(list.get(8));
            liste10.add(list.get(9));
            liste11.add(list.get(10));
            liste12.add(list.get(11));
            liste13.add(list.get(12));
            liste14.add(list.get(13));
            liste15.add(list.get(14));
            liste16.add(list.get(15));
            liste17.add(list.get(16));
            action.add(list.get(17));
        }else{
            System.out.println("ok ça bug");
        }
    }

    public void learnAll(NeuralNetwork net) {
        int i;
        int k=0;
        int j;
        do {
            j=0;
            for (i = 0; i < this.liste1.size(); i++) {
                double out = net.outputs(new double[]{this.liste1.get(i), this.liste2.get(i), this.liste3.get(i), this.liste4.get(i), this.liste5.get(i), this.liste6.get(i), this.liste7.get(i), this.liste8.get(i), this.liste9.get(i), this.liste10.get(i), this.liste11.get(i), this.liste12.get(i), this.liste13.get(i), this.liste14.get(i), this.liste15.get(i), this.liste16.get(i), this.liste17.get(i)})[0];
                if (out <= this.action.get(i) - 0.01 || out >= this.action.get(i) + 0.01) {
                    net.learn(new double[]{this.action.get(i)});
                    System.out.println(out);
                    System.out.println(this.action.get(i));
                    j++;
                }
            }
            k++;
        }while(j>11000);
        System.out.println(j +" Erreures dans le training");
        System.out.println("Neural training : "+i+" lignes dans le DataSet donneesDeJeu.csv");
        try{
            net.save(new File("solid_war.network"));
        }catch (Exception e){
            System.out.println("Erreure dans la sauvegarde du réseau de neurones");
        }
    }
}