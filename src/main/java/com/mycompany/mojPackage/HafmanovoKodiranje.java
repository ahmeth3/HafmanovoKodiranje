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

    public static void kodiraj(Cvor koren, String str, Map<Character, String> huffmanovKod) {
        if (koren == null) {
            return;
        }

        if (koren.levo == null && koren.desno == null) {
            huffmanovKod.put(koren.znak, str);
        }

        kodiraj(koren.levo, str + '0', huffmanovKod);
        kodiraj(koren.desno, str + '1', huffmanovKod);
    }

    public static int dekodiraj(Cvor koren, int indeks, StringBuilder kodiraniTekst, StringBuilder dekodiraniTekst) {
        if (koren == null) {
            return indeks;
        }

        if (koren.levo == null && koren.desno == null) {
            dekodiraniTekst.append(koren.znak);
            return indeks;
        }

        indeks++;

        if (kodiraniTekst.charAt(indeks) == '0') {
            indeks = dekodiraj(koren.levo, indeks, kodiraniTekst, dekodiraniTekst);
        } else {
            indeks = dekodiraj(koren.desno, indeks, kodiraniTekst, dekodiraniTekst);
        }

        return indeks;
    }

    public static Object[] generisatiHuffmanStablo(String tekst) {
        Map<Character, Integer> frekvencijeZnaka = new HashMap<>();

        for (char znak : tekst.toCharArray()) {
            frekvencijeZnaka.put(znak, frekvencijeZnaka.getOrDefault(znak, 0) + 1);
        }

        PriorityQueue<Cvor> red;
        red = new PriorityQueue<>(Comparator.comparingInt(e -> e.brPojavljivanja));

        for (var unos : frekvencijeZnaka.entrySet()) {
            red.add(new Cvor(unos.getKey(), unos.getValue()));
        }

        while (red.size() != 1) {
            Cvor levoDete = red.poll();
            Cvor desnoDete = red.poll();

            int zbirPojavljivanja = levoDete.brPojavljivanja + desnoDete.brPojavljivanja;
            red.add(new Cvor('\0', zbirPojavljivanja, levoDete, desnoDete));
        }

        Cvor koren = red.peek();

        Map<Character, String> huffmanovKod = new HashMap<>();
        kodiraj(koren, "", huffmanovKod);

        StringBuilder kodiraniTekst = new StringBuilder();
        for (char znak : tekst.toCharArray()) {
            kodiraniTekst.append(huffmanovKod.get(znak));
        }

        int indeks = -1;
        StringBuilder dekodiraniTekst = new StringBuilder();

        while (indeks < kodiraniTekst.length() - 1) {
            indeks = dekodiraj(koren, indeks, kodiraniTekst, dekodiraniTekst);
        }

        return new Object[]{huffmanovKod, kodiraniTekst, dekodiraniTekst};
    }
}
