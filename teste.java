package Wheels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class teste {
    public static void main(String[] args) {
        // Caminhos dos arquivos
        String caminhoClientes = "data/clientes.csv";
        String caminhoBikes = "data/bikes.csv";
        String caminhoHires = "data/hires.csv";
        String caminhoNotifications = "data/notifications.csv";

        List<Customer> clientes = new ArrayList<>();
        List<Hire> hires = new ArrayList<>();

        // -------------------
        // FASE 1 - Criar dados e salvar
        // -------------------
        System.out.println("FASE 1 - Criando dados e salvando em CSVs...");

        // Criar clientes
        clientes.add(new Customer("João Silva", "12345-000", "111122223", "joao.silva@email.com"));
        clientes.add(new Customer("Maria Souza", "54321-000", "999988887", "brenndallandim@gmail.com"));

        // Assumindo que as bikes já foram carregadas em Bike.getBikeList()
        // Para criar hires, garantimos que existam bikes na lista
        if (Bike.getBikeList().size() >= 2) {
            hires.add(new Hire(new Date(), 3, Bike.getBikeList().get(0), clientes.get(0)));
            hires.add(new Hire(new Date(), 5, Bike.getBikeList().get(1), clientes.get(1)));
        } else {
            System.out.println("Lista de bikes insuficiente para criar hires.");
        }

        // Salvar dados
        DataStore.salvarClientes(clientes, caminhoClientes);
        DataStore.salvarBikes(caminhoBikes);
        DataStore.salvarHires(hires, caminhoHires);

        System.out.println("Dados salvos com sucesso.\n");

        // -------------------
        // FASE 2 - Limpar e recarregar
        // -------------------
        System.out.println("FASE 2 - Limpando e recarregando dados dos arquivos...");

        clientes.clear();
        Bike.clearBikeList();
        hires.clear();

        DataStore.carregarClientes(clientes, caminhoClientes);
        DataStore.carregarBikes(caminhoBikes);
        DataStore.carregarHires(hires, caminhoHires, clientes, Bike.getBikeList());

        // Exibir clientes carregados
        System.out.println("Clientes carregados:");
        for (Customer c : clientes) {
            System.out.println("Nome: " + c.getName() + ", CEP: " + c.getPostcode() + ", Telefone: " + c.getTelephone() + ", Email: " + c.getEmail());
        }

        // Exibir bikes carregadas
        System.out.println("\nBikes carregadas:");
        for (Bike b : Bike.getBikeList()) {
            b.showDetails();
        }

        // Exibir locações carregadas
        System.out.println("\nLocações carregadas:");
        for (Hire h : hires) {
            System.out.println("Hire ID: " + h.getHireId()
                    + ", Cliente: " + h.getCustomer().getName()
                    + ", Bike: " + h.getBike().getBikeNumber()
                    + ", Dias: " + h.getNumberOfDays()
                    + ", Início: " + h.getStartDate());
        }

        // -------------------
        // FASE 3 - Notificações de atraso
        // -------------------
        System.out.println("\nFASE 3 - Verificando e enviando notificações de atraso...");

        NotificationService notificationService = new NotificationService();

        // Carrega notificações anteriores (se houver)
        notificationService.carregarNotifications(caminhoNotifications);

        // Verifica hires e envia notificações para os atrasados
        notificationService.verificarEEnviarNotificacoes(hires);

        // Salva notificações atualizadas
        notificationService.salvarNotifications(caminhoNotifications);

        // Exibe notificações geradas
        System.out.println("\nNotificações geradas:");
        for (Notification n : notificationService.getNotifications()) {
            System.out.println("Hire ID: " + n.getHireId()
                    + ", Cliente: " + n.getCustomerName()
                    + ", Bike nº: " + n.getBikeNumber()
                    + ", Mensagem: " + n.getMessage()
                    + ", Data envio: " + n.getDateSent());
        }
    }
}