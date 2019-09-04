/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Jeu de Tic Tac Toe qu'un utilisateur joue contre une IA (l'ordinateur).
 * L'utilisateur joue en utilisant le symbole «X» et l'IA utilise le symbole
 * «O».
 *
 * @Since 27 february 2018
 * @author Charles Nguyen
 */
public class TP1 {

    /**
     * @param args the command line arguments
     */
    private static final String OUI = "oui";
    private static final String NON = "non";
    private static final String PLUS = "+";
    private static final String MOINS = "-";
    private static final String LIGNE = "|";

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        String reponse;

        System.out.println("Bonjour " + (reponse = introductionNom())
                + ", êtes-vous prêt à jouer?");

        jouer();
    }

    /**
     * Méthode principale pour jouer au Tic Tac Toe
     */
    private static void jouer() {

        int taille;
        int nbTour = 0;

        //déclaration du tableau de jeu pour les X et O
        String[][] tabJeu;

        taille = tailleTableau();

        //tirage pour décider qui commence le jeu
        boolean tirage = premierJoueur();

        boolean verif[][] = new boolean[taille][taille];

        tabJeu = creerTableau(taille);
        afficherTableau(tabJeu, taille);

        //Le jeu continue dans que les cases ne sont pas tous remplies
        while (nbTour < (taille * taille)) {
            //le joueur joue en premier
            if (tirage) {
                obtenirCoordUtilisateur(tabJeu, taille, verif);
                afficherTableau(tabJeu, taille);
                nbTour++;
                verifierGagnant(tabJeu, taille, nbTour);
                deplacer(taille, tabJeu, verif);
                nbTour++;
                //l'AI joue en premier
            } else {
                deplacer(taille, tabJeu, verif);
                nbTour++;
                verifierGagnant(tabJeu, taille, nbTour);
                obtenirCoordUtilisateur(tabJeu, taille, verif);
                afficherTableau(tabJeu, taille);
                nbTour++;
            }
            verifierGagnant(tabJeu, taille, nbTour);
        }
    }

    /**
     *
     * @return retourne la valeur aléatoire du tirage
     */
    private static boolean premierJoueur() {
        Random rand = new Random();
        return rand.nextInt(100) < 50;
    }

    /**
     * Méthode qui demande au joueur s'il désire rejouer une nouvelle partie
     */
    private static void rejouer() {
        Scanner read = new Scanner(System.in);
        String reponse;

        do {
            System.out.print("Voulez vous rejouer une nouvelle partie? "
                    + "Répondez par oui ou non: ");

            reponse = read.nextLine();

            if (reponse.toLowerCase().equals(OUI)) {
                jouer();
            }
            if (reponse.toLowerCase().equals(NON)) {
                System.out.println("Vouez avez quitté la partie.");
                System.exit(0);
            }
            //boucle tant que la réponse n'est pas oui ou non
        } while (!reponse.equals(OUI) || !reponse.equals(NON));

    }

    /**
     * Méthode qui demande au joueur d'inscrire son nom
     *
     * @return le nom entré par le joueur
     */
    private static String introductionNom() {
        String nom = "";
        Scanner read = new Scanner(System.in);

        do {
            System.out.print("Veuillez écrire votre nom: ");
            nom = read.nextLine();

            //boucle tant que la chaine est nulle
        } while (nom.equals(""));

        return nom;
    }

    /**
     * Méthode qui crée le tableau avec la taille entré par le joueur
     *
     * @param taille la taille du jeu
     * @return retourne le tableau du jeu avec une taille définie
     */
    private static String[][] creerTableau(int taille) {
        String[][] dimension = new String[taille][taille];
        for (int i = 0; i < dimension.length; i++) {
            for (int j = 0; j < dimension[i].length; j++) {
                dimension[i][j] = " ";
            }
        }
        return dimension;
    }

    /**
     * Méthode qui affiche le tableau du jeu
     *
     * @param tabJeu le tableau du jeu
     * @param taille la taille du jeu
     */
    private static void afficherTableau(String[][] tabJeu, int taille) {
        int compteur = 0;
        String separateur = "";

        while (compteur < taille * 2) {
            if (compteur % 2 == 0) {
                separateur += PLUS;
            } else {
                separateur += MOINS;
            }
            ++compteur;
        }

        separateur += "+";

        for (int i = 0; i < tabJeu.length; i++) {
            System.out.println(separateur);
            for (int j = 0; j < tabJeu[i].length; j++) {
                System.out.print(LIGNE + tabJeu[i][j]);
            }
            System.out.println(LIGNE);
        }
        System.out.println(separateur);
    }

    /**
     * Méthode qui demande la taille du jeu au joueur
     *
     * @return la taille choisie par le joueur
     */
    private static int tailleTableau() {
        Scanner read = new Scanner(System.in);
        int taille = 0;
        boolean correct = false;
        System.out.print("Veuillez entrer la taille du tableau désiré: ");

        //boucle tant qu'il y a un erreur ou la valeur entré est invalide
        do {
            try {
                taille = read.nextInt();
                correct = true;

                //boucle tant que le joueur entre des lettres
            } catch (InputMismatchException e) {
                System.out.print("Veuillez entrer un nombre: ");
                read.next();
            } catch (NullPointerException e) {
                System.out.print("Vous avez rien entré, veuillez entrer un nombre");
                read.next();
            }

        } while (!correct);
        return taille;
    }

    /**
     * La méthode demande une coordonnée au joueur
     *
     * @param tabJeu tableau du jeu
     * @param taille la taille du jeu
     * @param verif tableau de booléen qui vérouille la case du tableau choisie
     */
    private static void obtenirCoordUtilisateur(String[][] tabJeu,
            int taille, boolean[][] verif) {
        Scanner read = new Scanner(System.in);
        String coord;
        String[] temp;
        boolean correct = false;

        System.out.println("Veuillez entrer vos coordonnées "
                + "(ajoutez un espace entre les coordonnées): ");

        //boucle tant qu'il y a un erreur ou la valeur entré est invalide
        do {
            try {
                coord = read.nextLine();
                temp = coord.split(" ");
                int coordX = Integer.parseInt(temp[0]);
                int coordY = Integer.parseInt(temp[1]);
                correct = remplirTableau(coordX, coordY, tabJeu,
                        taille, verif);

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.print("Vous etes hors des limites. Entrez une "
                        + "coordonnée avec un espace entre les deux: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrez une coordonnée avec un espace entre "
                        + "les deux: ");
            } catch (NullPointerException e) {
                System.out.print("Vous n'avez rien écrit. Veuillez entrer vos "
                        + "coordonnés: ");
            }
        } while (!correct);

    }

    /**
     * La méthode inscrit "X" dans la coordonnée entrée par le joueur
     *
     * @param coordX coordonnée X
     * @param coordY coordonnée Y
     * @param tabJeu tableau du jeu
     * @param taille la taille du jeu
     * @param verif tableau de booléen qui vérouille la case du tableau choisie
     * @return valeur "true" booléenne qui valide que la case est vérouillé
     */
    private static boolean remplirTableau(int coordX, int coordY,
            String[][] tabJeu, int taille, boolean[][] verif) {

        while (verif[coordX][coordY] == true) {
            System.out.println("La case est déjà choisie. Veuillez entrer une "
                    + "case valide: ");
            return false;
        }
        tabJeu[coordX][coordY] = "X";
        verif[coordX][coordY] = true;
        return true;
    }

    /**
     * Méthode ou l'AI va contrer les mouvements du joueur en suivant un
     * althorithme
     *
     * @param taille la taille du jeu
     * @param tabJeu tableau du jeu
     * @param verif tableau de booléen qui vérouille la case du tableau choisie
     */
    private static void verifierMouvement(int taille, String[][] tabJeu,
            boolean[][] verif) {
        int compteurO = 0;
        int compteurX = 0;

        //AI priorise sa victoire horizontalement
        for (int i = 0; i < tabJeu.length; i++) {
            for (int j = 0; j < tabJeu[i].length; j++) {
                if (tabJeu[i][j].equals("O")) {
                    compteurO++;
                }
                if (compteurO == taille - 1) {
                    for (int k = 0; k < tabJeu[i].length; k++) {
                        if (tabJeu[i][k].equals(" ")) {
                            tabJeu[i][k] = "O";
                        }
                    }
                }
            }
            compteurO = 0;
        }

        //AI bloque victoire du joueur horizontalement
        compteurX = 0;
        for (int i = 0; i < tabJeu.length; ++i) {
            for (int j = 0; j < tabJeu[i].length; ++j) {
                if (tabJeu[i][j].equals("X")) {
                    ++compteurX;
                }
                if (compteurX == taille - 1) {
                    for (int k = 0; k < tabJeu[i].length; k++) {
                        if (tabJeu[i][k].equals(" ")) {
                            tabJeu[i][k] = "O";
                        }
                    }
                }
                compteurX = 0;
            }
        }

        //AI priorise sa victoire verticalement
        compteurO = 0;
        for (int i = 0; i < tabJeu.length; i++) {
            for (int j = 0; j < tabJeu[i].length; j++) {
                if (tabJeu[j][i].equals("O")) {
                    compteurO++;

                }
                if (compteurO == taille - 1) {
                    for (int k = 0; k < tabJeu[i].length; k++) {
                        if (tabJeu[j][k].equals(" ")) {
                            tabJeu[j][k] = "O";
                        }
                    }
                }
            }
            compteurO = 0;
        }

        //AI bloque victoire du joueur verticalement
        compteurX = 0;
        for (int i = 0; i < tabJeu.length; i++) {
            for (int j = 0; j < tabJeu[i].length; j++) {
                if (tabJeu[j][i].equals("X")) {
                    compteurX++;

                }
                if (compteurX == taille - 1) {
                    for (int k = 0; k < tabJeu[i].length; k++) {
                        if (tabJeu[j][k].equals(" ")) {
                            tabJeu[j][k] = "O";
                        }
                    }
                }
            }
            compteurX = 0;
        }

        //AI priorise sa victoire diagonalement
        compteurO = 0;
        for (int j = 0; j < taille; j++) {
            if (tabJeu[j][j].equals("X")) {
                compteurO++;
            } else if (tabJeu[j][j].equals("O")) {
                compteurO++;
            }
        }

        //AI bloque victoire du joueur diagonalement
        compteurX = 0;
        for (int j = 0; j < taille; j++) {
            if (tabJeu[j][j].equals("X")) {
                compteurX++;
            }
            if (compteurX == (taille - 1)) {
                if (tabJeu[j].equals(" "));
                tabJeu[j].equals("O");
            }
        }

        //AI priorise victoire diagonale inverse
        compteurO = 0;
        int indice = taille - 1;
        for (int j = 0; j < taille; j++, indice--) {
            if (tabJeu[j][indice].equals("X")) {
                compteurO++;
            }
        }

        //AI bloque victoire du joueur diagonale inverse
        compteurX = 0;
        for (int j = 0; j < taille; j++, indice--) {
            if (tabJeu[j][indice].equals("X")) {
                compteurX++;
            }
            if (compteurX == (taille - 1)) {
                if (tabJeu[j][indice].equals(" "));
                tabJeu[j].equals("O");
            }
        }
        
                 boolean joueIntel = false;
                 
//==============================================================================

//        int compteurO = 0;
//        int compteurX = 0;
//
//        //AI priorise sa victoire horizontalement
//        for (int i = 0; i < tabJeu.length; i++) {
//            for (int j = 0; j < tabJeu[i].length; j++) {
//                if (tabJeu[i][j].equals("O")) {
//                    compteurO++;
//                }
//                if (compteurO == taille - 1) {
//                    for (int k = 0; k < tabJeu[i].length; k++) {
//                        if (tabJeu[i][k].equals(" ")) {
//                            tabJeu[i][k] = "O";
//                            return true;
//                        }
//                    }
//                }
//            }
//            compteurO = 0;
//        }
//
//        //AI bloque victoire du joueur horizontalement
//        compteurX = 0;
//        for (int i = 0; i < tabJeu.length; ++i) {
//            for (int j = 0; j < tabJeu[i].length; ++j) {
//                if (tabJeu[i][j].equals("X")) {
//                    ++compteurX;
//                }
//                if (compteurX == (taille - 1)) {
//                    if (tabJeu[i][j].equals(" "));
//                    tabJeu[i][j] = "O";
//                    return true;
//                }
//            }
//            compteurX = 0;
//        }
//
//        //AI priorise sa victoire verticalement
//        compteurO = 0;
//        for (int i = 0; i < tabJeu.length; i++) {
//            for (int j = 0; j < tabJeu[i].length; j++) {
//                if (tabJeu[j][i].equals("O")) {
//                    compteurO++;
//
//                }
//                if (compteurO == (taille - 1)) {
//                    if (tabJeu[j][i].equals(" "));
//                    tabJeu[j][i] = "O";
//                    return true;
//                }
//            }
//            compteurO = 0;
//        }
//
//        //AI bloque victoire du joueur verticalement
//        compteurX = 0;
//        for (int i = 0; i < tabJeu.length; i++) {
//            for (int j = 0; j < tabJeu[i].length; j++) {
//                if (tabJeu[j][i].equals("X")) {
//                    compteurX++;
//
//                }
//                if (compteurX == (taille - 1)) {
//                    if (tabJeu[j][i].equals(" "));
//                    tabJeu[j][i] = "O";
//                    return true;
//                }
//            }
//            compteurX = 0;
//        }
//
//        AI priorise sa victoire diagonalement
//        compteurO = 0;
//        for (int j = 0; j < taille; j++) {
//            if (tabJeu[j][j].equals("O")) {
//                compteurO++;
//            } 
//            if (tabJeu[j][j].equals(" ")) {
//                tabJeu[j][j] = "O";
//                    return true;
//            }
//        }
//
//        //AI bloque victoire du joueur diagonalement
//        compteurX = 0;
//        for (int j = 0; j < taille; j++) {
//            if (tabJeu[j][j].equals("X")) {
//                compteurX++;
//            }
//            if (compteurX == (taille - 1)) {
//                if (tabJeu[j].equals(" "));
//                tabJeu[j].equals("O");
//                    return true;
//            }
//        }
//
//        //AI priorise victoire diagonale inverse
//        compteurO = 0;
//        int indice = taille - 1;
//        for (int j = 0; j < taille; j++, indice--) {
//            if (tabJeu[j][indice].equals("O")) {
//                compteurO++;
//            }
//            if (compteurO == (taille - 1)) {
//                if (tabJeu[j][indice].equals(" "));
//                tabJeu[j][indice].equals("O");
//                    return true;
//            }
//        }
//
////        AI bloque victoire du joueur diagonale inverse
//        compteurX = 0;
//        indice = taille - 1;
//        for (int j = 0; j < taille; j++, indice--) {
//            if (tabJeu[j][indice].equals("X")) {
//                compteurX++;
//            }
//            if (compteurX == (taille - 1)) {
//                if (tabJeu[j][indice].equals(" "));
//                tabJeu[j][indice].equals("O");
//                    return true;
//            }
//        }
    }

    /**
     * Méthode qui représente l'AI et l'entrée de ses coordonnées
     *
     * @param taille la taille du jeu
     * @param tabJeu tableau du jeu
     * @param verif tableau de booléen qui vérouille la case du tableau choisie
     */
    private static void deplacer(int taille, String[][] tabJeu,
            boolean[][] verif) {
        Random rand = new Random();

        int choixY = rand.nextInt(taille);
        int choixX = rand.nextInt(taille);

        //limite aléatoire
        int limite = 250;

        while (verif[choixY][choixX] && limite > 0) {
            --limite;
            choixY = rand.nextInt(taille);
            choixX = rand.nextInt(taille);
        }

        /**
         * une fonction extra en cas que la fonction aléatoire ne trouve pas de
         * case libre alors il parcourt le tableau un à la fois
         */
        if (limite <= 0) {
            boolean go = true;
            for (int i = 0; i < verif.length && go; i++) {
                for (int j = 0; j < verif[i].length && go; j++) {
                    if (verif[i][j] == false) {
                        choixY = i;
                        choixX = j;
                        go = false;
                    }
                }
            }
        }

        tabJeu[choixY][choixX] = "O";
        verif[choixY][choixX] = true;

        System.out.println("Tour de l'IA.");
        afficherTableau(tabJeu, taille);
    }

    /**
     * Méthode qui vérifie si le joueur ou l'AI est gagnant à chaque tour
     *
     * @param tabJeu tableau du jeu
     * @param taille la taille du jeu
     * @param nbTour nombres de tours totaux
     */
    private static void verifierGagnant(String[][] tabJeu, int taille,
            int nbTour) {
        int compteurX = 0;
        int compteurO = 0;

        int nul = 0;

        //Vérification diagonale
        for (int j = 0; j < taille; j++) {
            if (tabJeu[j][j].equals("X")) {
                compteurX++;
            } else if (tabJeu[j][j].equals("O")) {
                compteurO++;
            }
        }
        if (compteurX == taille) {
            System.out.println("Vous avez gagné.");
            rejouer();
        }
        if (compteurO == taille) {
            System.out.println("L'AI a gagné.");
            rejouer();
        }
        compteurX = 0;
        compteurO = 0;

        //Vérification diagonale inverse
        compteurX = 0;
        compteurO = 0;
        int indice = taille - 1;
        for (int j = 0; j < taille; j++, indice--) {
            if (tabJeu[j][indice].equals("X")) {
                compteurX++;
            } else if (tabJeu[j][indice].equals("O")) {
                compteurO++;
            }
        }
        if (compteurX == taille) {
            System.out.println("Vous avez gagné.");
            rejouer();
        }
        if (compteurO == taille) {
            System.out.println("L'AI a gagné.");
            rejouer();
        }
        compteurX = 0;
        compteurO = 0;

        //Vérification verticale
        for (int i = 0; i < tabJeu.length; i++) {
            for (int j = 0; j < tabJeu[i].length; j++) {
                if (tabJeu[j][i].equals("X")) {
                    compteurX++;

                } else if (tabJeu[j][i].equals("O")) {
                    compteurO++;
                }
            }
            if (compteurX == taille) {
                System.out.println("Vous avez gagné.");
                rejouer();
            }
            if (compteurO == taille) {
                System.out.println("L'AI a gagné.");
                rejouer();
            }
            compteurX = 0;
            compteurO = 0;
        }

        //Vérification horizontale
        for (int i = 0; i < tabJeu.length; i++) {
            for (int j = 0; j < tabJeu[i].length; j++) {
                if (tabJeu[i][j].equals("X")) {
                    compteurX++;

                } else if (tabJeu[i][j].equals("O")) {
                    compteurO++;
                }
            }
            if (compteurX == taille) {
                System.out.println("Vous avez gagné.");
                rejouer();
            }
            if (compteurO == taille) {
                System.out.println("L'AI a gagné.");
                rejouer();
            }
            compteurX = 0;
            compteurO = 0;
        }

        //Si toute les cases sont remplies et il y a aucun gagnant
        if (nul++ == taille * taille) {
            System.out.println("Match nul.");
            rejouer();
        }

    }

}
