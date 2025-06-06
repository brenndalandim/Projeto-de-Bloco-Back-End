package Wheels;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    // Troca o array fixo por uma lista dinâmica
    protected static List<Bike> bikeList = new ArrayList<>();

    protected int deposit = 0;
    protected int rate = 0;
    protected int bikeNumber = 0;

    // Bloco estático para popular a lista inicialmente
    static {
        int j = 0;
        for (int i = 10; i < 15; i++) {
            Bike b = new Bike(i, i, (j * 100));
            bikeList.add(b);
            j++;
        }
    }

    public Bike(int dep, int rat, int num) {
        deposit = dep;
        rate = rat;
        bikeNumber = num;
    }

    // Construtor usado ao carregar de arquivo
    public static Bike fromCSV(String csvLine) {
        String[] dados = csvLine.split(",");
        int dep = Integer.parseInt(dados[0]);
        int rat = Integer.parseInt(dados[1]);
        int num = Integer.parseInt(dados[2]);
        return new Bike(dep, rat, num);
    }

    public int getDeposit() {
        return deposit;
    }

    public int getRate() {
        return rate;
    }

    public int getBikeNumber() {
        return bikeNumber;
    }

    public static Bike findBikeByNumber(int bikeNum) {
        for (Bike bike : bikeList) {
            if (bike.getBikeNumber() == bikeNum) {
                System.out.println("Bike with number '" + bikeNum + "' found \n");
                return bike;
            }
        }
        System.out.println("Bike with number '" + bikeNum + "' not found\n");
        return null;
    }

    public void showDetails() {
        System.out.println("Details for bike number '" + bikeNumber + "'");
        System.out.println("DEPOSIT: " + deposit);
        System.out.println("RATE: " + rate + "\n");
    }

    public double calculateCost(int numberOfDays) {
        double cost = deposit + (rate * numberOfDays);
        System.out.println("Cost would be £ " + cost + "\n");
        return cost;
    }

    // Exporta dados em formato CSV
    public String toCSV() {
        return deposit + "," + rate + "," + bikeNumber;
    }

    // Getter para acessar bikeList fora da classe, se necessário
    public static List<Bike> getBikeList() {
        return bikeList;
    }

    // Limpa a lista de bikes (útil antes de carregar dados do arquivo)
    public static void clearBikeList() {
        bikeList.clear();
    }

    @Override
    public String toString() {
        return "Bike #" + bikeNumber;
    }
}