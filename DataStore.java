package Wheels;

import java.io.*;
import java.text.ParseException;
import java.util.List;

public class DataStore {

    // ---- Clientes ----
    public static void salvarClientes(List<Customer> clientes, String caminho) {
        criarDiretorioSeNaoExistir(caminho);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Customer c : clientes) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    public static void carregarClientes(List<Customer> clientes, String caminho) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 4) {
                    String nome = dados[0];
                    String cep = dados[1];
                    int telefone = Integer.parseInt(dados[2]);
                    int id = Integer.parseInt(dados[3]);
                    Customer cliente = new Customer(nome, cep, telefone, id);
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    // ---- Bikes ----
    public static void salvarBikes(String caminho) {
        criarDiretorioSeNaoExistir(caminho);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Bike b : Bike.getBikeList()) {
                writer.write(b.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar bikes: " + e.getMessage());
        }
    }

    public static void carregarBikes(String caminho) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            Bike.clearBikeList();
            while ((linha = reader.readLine()) != null) {
                Bike bike = Bike.fromCSV(linha);
                Bike.getBikeList().add(bike);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar bikes: " + e.getMessage());
        }
    }

    // ---- Hires ----
    public static void salvarHires(List<Hire> hires, String caminho) {
        criarDiretorioSeNaoExistir(caminho);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Hire h : hires) {
                writer.write(h.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar hires: " + e.getMessage());
        }
    }

    public static void carregarHires(List<Hire> hires, String caminho, List<Customer> clientes, List<Bike> bikes) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                try {
                    String[] dados = linha.split(",");
                    int customerId = Integer.parseInt(dados[3]);
                    int bikeNumber = Integer.parseInt(dados[4]);

                    Customer cliente = clientes.stream()
                            .filter(c -> c.getCustomerNumber() == customerId)
                            .findFirst().orElse(null);

                    Bike bike = bikes.stream()
                            .filter(b -> b.getBikeNumber() == bikeNumber)
                            .findFirst().orElse(null);

                    if (cliente != null && bike != null) {
                        Hire hire = Hire.fromCSV(linha, cliente, bike);
                        hires.add(hire);
                    }
                } catch (ParseException e) {
                    System.out.println("Erro ao converter data de hire: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar hires: " + e.getMessage());
        }
    }

    // ---- Notifications ----
    public static void salvarNotifications(List<Notification> notifications, String caminho) {
        criarDiretorioSeNaoExistir(caminho);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (Notification n : notifications) {
                writer.write(n.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar notifications: " + e.getMessage());
        }
    }

    public static void carregarNotifications(List<Notification> notifications, String caminho) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Notification n = Notification.fromCSV(linha);
                notifications.add(n);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar notifications: " + e.getMessage());
        }
    }

    // ---- Auxiliar ----
    private static void criarDiretorioSeNaoExistir(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File dir = arquivo.getParentFile();
        if (dir != null && !dir.exists()) {
            boolean criado = dir.mkdirs();
            if (criado) {
                System.out.println("Diretório criado: " + dir.getAbsolutePath());
            }
        }
    }
}