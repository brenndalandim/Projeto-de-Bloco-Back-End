package Wheels;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                if (dados.length == 5) {
                    String nome = dados[0];
                    String cep = dados[1];
                    String telefone = dados[2];
                    String email = dados[3];
                    int id = Integer.parseInt(dados[4]);
                    Customer cliente = new Customer(nome, cep, telefone, email, id);
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

    public static void salvarHiresFinalizados(List<Hire> hiresFinalizados, String caminho) {
        criarDiretorioSeNaoExistir(caminho);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Hire h : hiresFinalizados) {
                String linha = sdf.format(h.getStartDate()) + "," +
                        h.getNumberOfDays() + "," +
                        h.getBike().getBikeNumber() + "," +
                        h.getCustomer().getCustomerNumber() + "," +
                        (h.getEndDate() != null ? sdf.format(h.getEndDate()) : "");
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar hires finalizados: " + e.getMessage());
        }
    }

    public static List<Hire> carregarHiresFinalizados(String caminho, List<Customer> clientes, List<Bike> bikes) {
        List<Hire> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(partes[0]);
                int dias = Integer.parseInt(partes[1]);
                int bikeNum = Integer.parseInt(partes[2]);
                int clienteNum = Integer.parseInt(partes[3]);
                Date endDate = partes.length > 4 ? new SimpleDateFormat("dd/MM/yyyy").parse(partes[4]) : null;

                Bike bike = bikes.stream()
                        .filter(b -> b.getBikeNumber() == bikeNum)
                        .findFirst()
                        .orElse(null);

                Customer cliente = clientes.stream()
                        .filter(c -> c.getCustomerNumber() == clienteNum)
                        .findFirst()
                        .orElse(null);

                if (cliente != null && bike != null) {
                    Hire h = new Hire(startDate, dias, bike, cliente);
                    h.setEndDate(endDate);
                    lista.add(h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
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