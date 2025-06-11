package Wheels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainMenuUI extends JFrame {
    private List<Customer> clientes;
    private List<Bike> bikes;
    private List<Hire> hires;
    private List<Hire> hiresFinalizados;
    private JLabel statusBar;

    public MainMenuUI(List<Customer> clientes, List<Bike> bikes, List<Hire> hires, List<Hire> hiresFinalizados) {
        this.clientes = clientes;
        this.bikes = bikes;
        this.hires = hires;
        this.hiresFinalizados = hiresFinalizados;

        setTitle("Sistema de Aluguel de Bikes");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Estilo tradicional
        Color corPrincipal = new Color(57, 105, 138); // Roxo
        Color corFundo = new Color(214, 217, 223, 255);     // Cinza claro
        Font fontePadrao = new Font("SansSerif", Font.BOLD, 14);

        setLayout(new BorderLayout());

        // Logo
        JPanel topo = new JPanel();
        topo.setBackground(corFundo);
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("img/logo.png")));
            Image imgRedimensionada = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(imgRedimensionada));
            topo.add(logoLabel);
        } catch (IOException e) {
            topo.add(new JLabel("Sistema de Aluguel de Bikes"));
        }
        add(topo, BorderLayout.NORTH);

        // Painel central com margens e espaçamento
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBackground(corFundo);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botões
        JButton btnListarClientes = new JButton("Listar Clientes");
        JButton btnListarBikes = new JButton("Listar Bikes Disponíveis");
        JButton btnNovoAluguel = new JButton("Emitir Novo Aluguel");
        JButton btnDevolverBike = new JButton("Finalizar Devolução");
        JButton btnNotificacoes = new JButton("Verificar Notificações de Atraso");
        JButton btnSair = new JButton("Sair");

        JButton[] botoes = {
                btnListarClientes, btnListarBikes, btnNovoAluguel,
                btnDevolverBike, btnNotificacoes, btnSair
        };

        for (JButton botao : botoes) {
            botao.setFont(fontePadrao);
            botao.setBackground(corPrincipal);
            botao.setForeground(Color.WHITE);
            botao.setFocusPainted(false);
            panel.add(botao);
        }

        add(panel, BorderLayout.CENTER);

        // Ações
        btnListarClientes.addActionListener(e ->listarClientes());
        btnListarBikes.addActionListener(e ->listarBikesDisponiveis());
        btnNovoAluguel.addActionListener(e ->emitirNovoAluguel());
        btnDevolverBike.addActionListener(e ->finalizarDevolucao());
        btnNotificacoes.addActionListener(e ->verificarNotificacoes());
        btnSair.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void listarClientes() {
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente cadastrado.");
            return;
        }

        // Lista de opções de clientes
        String[] opcoesClientes = new String[clientes.size()];
        for (int i = 0; i < clientes.size(); i++) {
            Customer c = clientes.get(i);
            opcoesClientes[i] = "ID " + c.getCustomerNumber() + " - " + c.getName();
        }

        // Seleção
        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione um cliente para ver o histórico:",
                "Histórico de Aluguéis",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesClientes,
                opcoesClientes[0]
        );

        if (escolha == null) return;

        int indexSelecionado = java.util.Arrays.asList(opcoesClientes).indexOf(escolha);
        Customer clienteSelecionado = clientes.get(indexSelecionado);

        // Juntar aluguéis ativos e finalizados
        List<Hire> historico = new java.util.ArrayList<>();
        for (Hire h : hires) {
            if (h.getCustomer().equals(clienteSelecionado)) historico.add(h);
        }
        for (Hire h : hiresFinalizados) {
            if (h.getCustomer().equals(clienteSelecionado)) historico.add(h);
        }

        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente não possui aluguéis registrados.");
            return;
        }

        // Formatador de datas
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Monta visualização
        StringBuilder sb = new StringBuilder();

        // Dados básicos do cliente
        sb.append("Cliente: ").append(clienteSelecionado.getName()).append("\n");
        sb.append("Telefone: ").append(clienteSelecionado.getTelephone()).append("\n");
        sb.append("Email: ").append(clienteSelecionado.getEmail()).append("\n");
        sb.append("CEP: ").append(clienteSelecionado.getPostcode()).append("\n");
        sb.append("\n============================================\n\n");

        // Depois vem o histórico
        sb.append("Histórico de aluguéis:\n\n");
        for (Hire h : historico) {
            Payment p = new Payment(clienteSelecionado);
            p.calculateTotalPayment(h);

            String dataInicio = sdf.format(h.getStartDate());
            String devolucao = (h.getEndDate() != null) ? sdf.format(h.getEndDate()) : "Sem devolução registrada";

            sb.append("📅 Início: ").append(dataInicio)
                    .append("\n🚲 Bike nº: ").append(h.getBike().getBikeNumber())
                    .append("\n📆 Dias contratados: ").append(h.getNumberOfDays());

            double pago = p.getTotalPayment();
            double multa = 0.0;
            double totalComMulta = pago;

            if (h.getEndDate() != null) {
                // Calcular atraso
                long millisAlugado = h.getEndDate().getTime() - h.getStartDate().getTime();
                int diasReais = (int) (millisAlugado / (1000 * 60 * 60 * 24));
                int diasContratados = h.getNumberOfDays();
                int diasExtras = Math.max(0, diasReais - diasContratados);

                if (diasExtras > 0) {
                    multa = diasExtras * h.getBike().getRate();
                    totalComMulta += multa;
                }
            }

            sb.append(String.format("\n💰 Pago: R$ %.2f", pago));

            if (multa > 0) {
                sb.append(String.format("\n⚠️ Multa por atraso: R$ %.2f", multa));
            }

            if (multa > 0) {
                sb.append(String.format("\n📊 Total com multa: R$ %.2f", totalComMulta));
            }

            sb.append("\n📤 Devolução: ").append(devolucao)
                    .append("\n---------------------------------------\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Histórico do Cliente", JOptionPane.INFORMATION_MESSAGE);
    }


    private void listarBikesDisponiveis() {
        List<Bike> todasBikes = Bike.getBikeList();
        List<Bike> alugadas = hires.stream()
                .map(Hire::getBike)
                .toList();

        StringBuilder sb = new StringBuilder();

        for (Bike bike : todasBikes) {
            if (!alugadas.contains(bike)) {
                sb.append("🚲 Bike Nº: ").append(bike.getBikeNumber())
                        .append("\n💵 Depósito: R$").append(bike.getDeposit())
                        .append(" | 📅 Preço/dia: R$").append(bike.getRate())
                        .append("\n----------------------------------------------------------\n");
            }
        }

        if (sb.isEmpty()) {
            sb.append("Nenhuma bike disponível no momento.");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Bikes Disponíveis", JOptionPane.INFORMATION_MESSAGE);
    }

    private void emitirNovoAluguel() {
        // 1. Filtra as bikes disponíveis
        List<Bike> bikesDisponiveis = Bike.getBikeList().stream()
                .filter(b -> hires.stream().noneMatch(h -> h.getBike().equals(b)))
                .toList();

        if (bikesDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma bike disponível para aluguel.");
            return;
        }

        // 2. Escolhe a bike
        Bike[] opcoesBikes = bikesDisponiveis.toArray(new Bike[0]);
        Bike bikeSelecionada = (Bike) JOptionPane.showInputDialog(
                this,
                "Selecione a bike para alugar:",
                "Escolher Bike",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesBikes,
                opcoesBikes[0]
        );

        if (bikeSelecionada == null) return;

        // 3. Escolher cliente existente ou cadastrar novo
        Customer clienteSelecionado = null;

        // Monta lista de clientes + opção de novo
        String[] opcoesClientes = new String[clientes.size() + 1];
        for (int i = 0; i < clientes.size(); i++) {
            Customer c = clientes.get(i);
            opcoesClientes[i] = "ID " + c.getCustomerNumber() + " - " + c.getName();
        }
        opcoesClientes[clientes.size()] = "Cadastrar Novo Cliente"; // Última opção

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o cliente:",
                "Escolha do Cliente",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesClientes,
                opcoesClientes[0]
        );

        if (escolha == null) return;

        if (escolha.equals("Cadastrar Novo Cliente")) {
            // Formulário de novo cliente
            JTextField nomeField = new JTextField();
            JTextField cepField = new JTextField();
            JTextField telField = new JTextField();
            JTextField emailField = new JTextField();

            JPanel painelCliente = new JPanel(new GridLayout(0, 1));
            painelCliente.add(new JLabel("Nome do cliente:"));
            painelCliente.add(nomeField);
            painelCliente.add(new JLabel("CEP:"));
            painelCliente.add(cepField);
            painelCliente.add(new JLabel("Telefone:"));
            painelCliente.add(telField);
            painelCliente.add(new JLabel("Email:"));
            painelCliente.add(emailField);

            int resultado = JOptionPane.showConfirmDialog(this, painelCliente, "Cadastro de Novo Cliente", JOptionPane.OK_CANCEL_OPTION);
            if (resultado != JOptionPane.OK_OPTION) return;

            String nome = nomeField.getText().trim();
            String cep = cepField.getText().trim();
            String telefone = telField.getText().trim();
            String email = emailField.getText().trim();

            clienteSelecionado = new Customer(nome, cep, telefone, email);
            clientes.add(clienteSelecionado);
            DataStore.salvarClientes(clientes, "data/clientes.csv");
        } else {
            // Encontrar cliente correspondente
            int index = java.util.Arrays.asList(opcoesClientes).indexOf(escolha);
            clienteSelecionado = clientes.get(index);
        }

        // 4. Número de dias
        String diasStr = JOptionPane.showInputDialog(this, "Quantos dias de aluguel?");
        if (diasStr == null) return;

        int dias = Integer.parseInt(diasStr);

        // 5. Criar aluguel e pagamento
        Hire novoHire = new Hire(new Date(), dias, bikeSelecionada, clienteSelecionado);
        Payment pagamento = new Payment(clienteSelecionado);
        pagamento.calculateTotalPayment(novoHire);

        hires.add(novoHire);
        DataStore.salvarHires(hires, "data/hires.csv");

        JOptionPane.showMessageDialog(this,
                "Aluguel registrado com sucesso!\n\n" +
                        "Cliente: " + clienteSelecionado.getName() +
                        "\nBike: " + bikeSelecionada.getBikeNumber() +
                        "\nDias: " + dias +
                        "\nTotal: R$ " + String.format("%.2f", pagamento.getTotalPayment()));
    }

    private void finalizarDevolucao() {
        if (hires.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum aluguel ativo para devolução.");
            return;
        }

        // Seleção do aluguel ativo
        Hire[] opcoes = hires.toArray(new Hire[0]);
        Hire hireSelecionado = (Hire) JOptionPane.showInputDialog(
                this,
                "Selecione o aluguel a ser finalizado:",
                "Finalizar Devolução",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (hireSelecionado == null) return;

        // Atualiza a data de devolução como hoje
        hireSelecionado.setEndDate(new Date());

        // Verificar atraso
        long millisAlugado = hireSelecionado.getEndDate().getTime() - hireSelecionado.getStartDate().getTime();
        int diasReais = (int) (millisAlugado / (1000 * 60 * 60 * 24)) + 1; // +1 para contar o dia atual
        int diasContratados = hireSelecionado.getNumberOfDays();
        int diasExtras = Math.max(0, diasReais - diasContratados);

        double valorExtra = diasExtras * hireSelecionado.getBike().getRate();

        // Remove da lista de aluguéis ativos
        hires.remove(hireSelecionado);

        // Adiciona na lista de aluguéis finalizados
        hiresFinalizados.add(hireSelecionado);

        // Salva ambas as listas atualizadas
        DataStore.salvarHires(hires, "data/hires.csv");
        DataStore.salvarHiresFinalizados(hiresFinalizados, "data/hires_finalizados.csv");

        // Mensagem de resumo
        StringBuilder msg = new StringBuilder("Devolução finalizada com sucesso!\n\n");
        msg.append("Bike nº: ").append(hireSelecionado.getBike().getBikeNumber()).append("\n");
        msg.append("Cliente: ").append(hireSelecionado.getCustomer().getName()).append("\n");

        if (diasExtras > 0) {
            msg.append("Atraso: ").append(diasExtras).append(" dia(s)\n");
            msg.append("Valor adicional devido: R$ ").append(String.format("%.2f", valorExtra)).append("\n");
        } else {
            msg.append("Devolução feita no prazo.");
        }

        JOptionPane.showMessageDialog(this, msg.toString(), "Resumo da Devolução", JOptionPane.INFORMATION_MESSAGE);
    }

    private void verificarNotificacoes() {
        NotificationService notificationService = new NotificationService();
        notificationService.verificarEEnviarNotificacoes(hires);

        List<Notification> notificacoes = notificationService.getNotifications();

        if (notificacoes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhuma notificação de atraso encontrada.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Notification n : notificacoes) {
                sb.append("Cliente: ").append(n.getCustomerName()).append("\n")
                        .append("Bike nº: ").append(n.getBikeNumber()).append("\n")
                        .append("Data: ").append(n.getDateSent()).append("\n")
                        .append("Mensagem: ").append(n.getMessage()).append("\n\n")
                        .append("---------------------------------").append("\n\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(null, scrollPane, "Notificações de Atraso Enviadas", JOptionPane.INFORMATION_MESSAGE);
        }

        // Salva no CSV
        notificationService.salvarNotifications("data/notifications.csv");
    }
}