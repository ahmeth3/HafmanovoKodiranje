/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mojPackage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author Ahmet
 */
class Cvor {

    /* klasa Cvora u kojoj se cuva sam simbol kao char i broj pojavljivanja
       kao i listovi koji su objekti klase Cvor 
     */
    char znak;
    int brPojavljivanja;
    Cvor levo = null, desno = null;

    public Cvor(char znak, int brPojavljivanja) {
        this.znak = znak;
        this.brPojavljivanja = brPojavljivanja;
    }

    public Cvor(char znak, int brPojavljivanja, Cvor levo, Cvor desno) {
        this.znak = znak;
        this.brPojavljivanja = brPojavljivanja;
        this.levo = levo;
        this.desno = desno;
    }
}

public class HafmanovoKodiranje {

    // funkcija koja rekurzivno ide kroz tekst i odredjuje kod za simbol
    public static void kodiraj(Cvor koren, String str, Map<Character, String> huffmanovKod) {
        if (koren == null) {
            return;
        }

        if (koren.levo == null && koren.desno == null) {
            // kada dodje do krajnjeg cvora upisuje njegov kod u Map
            huffmanovKod.put(koren.znak, str);
        }

        // rekurzivno ide kroz leve i desne naslednike cvora
        kodiraj(koren.levo, str + '0', huffmanovKod);
        kodiraj(koren.desno, str + '1', huffmanovKod);
    }

    // funkcija koja rekurzivno ide kroz kodirani tekst i dekodira ga
    public static int dekodiraj(Cvor koren, int indeks, StringBuilder kodiraniTekst, StringBuilder dekodiraniTekst) {
        if (koren == null) {
            return indeks;
        }

        // ako nema vise naslednika onda se upisuje smbol tog cvora
        if (koren.levo == null && koren.desno == null) {
            dekodiraniTekst.append(koren.znak);
            return indeks;
        }

        // indeks se povecava
        indeks++;

        // ako je kod 0 idi do levog naslednika u suprotnom idi do desnog naslednika
        if (kodiraniTekst.charAt(indeks) == '0') {
            indeks = dekodiraj(koren.levo, indeks, kodiraniTekst, dekodiraniTekst);
        } else {
            indeks = dekodiraj(koren.desno, indeks, kodiraniTekst, dekodiraniTekst);
        }

        return indeks;
    }

    public static Object[] generisatiHuffmanStablo(String tekst) {
        // racuna se frekvencija pojavljivanja svakog simbola
        Map<Character, Integer> frekvencijeZnaka = new HashMap<>();

        for (char znak : tekst.toCharArray()) {
            frekvencijeZnaka.put(znak, frekvencijeZnaka.getOrDefault(znak, 0) + 1);
        }

        // kreira se red po rastucem redosledu frekvencije pojavljivanja simbola
        PriorityQueue<Cvor> red;
        red = new PriorityQueue<>(Comparator.comparingInt(e -> e.brPojavljivanja));

        for (var unos : frekvencijeZnaka.entrySet()) {
            red.add(new Cvor(unos.getKey(), unos.getValue()));
        }

        // ova while petlja kreira objekte klase Cvor
        while (red.size() != 1) {
            // uzima prvi i drugi simbol i kreira ga kao cvor
            Cvor levoDete = red.poll();
            Cvor desnoDete = red.poll();

            // kreira se zajednicki cvor, sa levim i desnim listom
            int zbirPojavljivanja = levoDete.brPojavljivanja + desnoDete.brPojavljivanja;
            red.add(new Cvor('\0', zbirPojavljivanja, levoDete, desnoDete));
        }

        // kreira se koren stabla
        Cvor koren = red.peek();

        // za svaki poseban simbol se odredjuje hafmanov unikatni kod
        Map<Character, String> huffmanovKod = new HashMap<>();
        kodiraj(koren, "", huffmanovKod);

        /* 
        kreira se string gde se svaki znak stringa menja sa njegovim 
        prethodno odredjenim unikatnim kodom 
         */
        StringBuilder kodiraniTekst = new StringBuilder();
        for (char znak : tekst.toCharArray()) {
            kodiraniTekst.append(huffmanovKod.get(znak));
        }

        // dekodira se tekst
        int indeks = -1;
        StringBuilder dekodiraniTekst = new StringBuilder();

        while (indeks < kodiraniTekst.length() - 1) {
            indeks = dekodiraj(koren, indeks, kodiraniTekst, dekodiraniTekst);
        }

        /* 
        vracaju se tri objekta: string sa svakim unikatnim kodom, 
        ceo kodirani tekst i dekodirani tekst 
         */
        return new Object[]{huffmanovKod, kodiraniTekst, dekodiraniTekst};
    }
}
