import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class SomeOtherSort implements Comparator<Pair<Email, Double>> {

    public int compare(Pair<Email, Double> a, Pair<Email, Double> b) {
        if (a.getValue() > b.getValue()) {
            return -1;
        }
        return 1;
    }

}

public class CosineSimilarity {


    ArrayList<Word> words;
    Email email1;
    Email email2;
    ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    int k = 0;
    double minSimilarityScore = Double.MAX_VALUE;
    double[] calArray;


    public CosineSimilarity(int numberOfNeighbors) {
        k = numberOfNeighbors;
        calArray = new double[k];
    }


    public double cosineSimilarity(Email e1, Email e2) {

        double numerator = 0;
        double e1Denominator = 0;
        double e2Denominator = 0;

        for (Word word : words) {
            double tf1 = e1.getTFIDFScore(word);
            double tf2 = e2.getTFIDFScore(word);
            numerator += (tf1 * tf2);
            e1Denominator += (tf1 * tf1);
            e2Denominator += (tf2 * tf2);
        }

        return numerator / (Math.sqrt(e1Denominator) * Math.sqrt(e2Denominator));

    }

    public void addNeighbour(Email email, double similarityScore) {
        if (similarityScore < minSimilarityScore) {

            if (neighbours.size() < k) {
                neighbours.add(new Pair<Email, Double>(email, similarityScore));
                minSimilarityScore = similarityScore;
                return;
            }

            Collections.sort(neighbours, new SomeOtherSort());
            neighbours.remove(0);
            neighbours.add(new Pair<Email, Double>(email, similarityScore));

        }

    }


    public ArrayList<Email> getNeighbours(ArrayList<Email> emails, Email unclassified) {


        for (Email email : emails) {
            double similarityScore = cosineSimilarity(email, unclassified);
            addNeighbour(email, similarityScore);
        }

        return emails;
    }


    public int voting(Email email, ArrayList<Pair<Email, Double>> neighbours) {
        int spamCount = 0;
        int hamCount = 0;

        for (Pair<Email, Double> e : neighbours) {
            if (e.getKey().category == 0) {
                hamCount++;
            } else {
                spamCount++;
            }
        }


        if (spamCount > hamCount) {
            return 1;
        }
        return 0;

    }


    public static void main(String[] args) {

    }

}