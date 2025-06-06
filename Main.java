package Wheels;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String caminhoClientes = "data/clientes.csv";
            String caminhoBikes = "data/bikes.csv";
            String caminhoHires = "data/hires.csv";
            String caminhoHiresFinalizados = "data/hires_finalizados.csv";

            List<Customer> clientes = new ArrayList<>();
            List<Bike> bikes = Bike.getBikeList();
            List<Hire> hires = new ArrayList<>();
            List<Hire> hiresFinalizados = new ArrayList<>();

            DataStore.carregarClientes(clientes, caminhoClientes);
            DataStore.carregarBikes(caminhoBikes);
            DataStore.carregarHires(hires, caminhoHires, clientes, bikes);

            hiresFinalizados = DataStore.carregarHiresFinalizados(caminhoHiresFinalizados, clientes, bikes);

            new MainMenuUI(clientes, bikes, hires, hiresFinalizados);
        });
    }
}
