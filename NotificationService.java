package Wheels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<Notification> notifications = new ArrayList<>();
    private EmailService emailService = new EmailService();

    public void verificarEEnviarNotificacoes(List<Hire> hires) {
        for (Hire hire : hires) {
            if (hire.isOverdue()) {
                String msg = String.format(
                        "Prezado(a) %s,\n\n" +
                                "Identificamos que o aluguel ID %d da bicicleta nº %d está em atraso.\n" +
                                "A devolução era esperada após %d dia(s), iniciando em %s.\n\n" +
                                "Por favor, entre em contato para regularizar a situação.\n\n" +
                                "Atenciosamente,\nWheels Team",
                        hire.getCustomer().getName(),
                        hire.getHireId(),
                        hire.getBike().getBikeNumber(),
                        hire.getNumberOfDays(),
                        new SimpleDateFormat("dd/MM/yyyy").format(hire.getStartDate())
                );

                Notification notification = new Notification(
                        hire.getHireId(),
                        hire.getCustomer().getName(),
                        hire.getBike().getBikeNumber(),
                        msg
                );

                notifications.add(notification);

                // Envia e-mail
                String destinatario = hire.getCustomer().getEmail();
                if (destinatario != null && !destinatario.isEmpty()) {
                    emailService.enviarEmail(destinatario, "Atraso na devolução da bicicleta", msg);
                }
            }
        }
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    // --- Persistência ---

    public void carregarNotifications(String caminho) {
        notifications.clear();
        DataStore.carregarNotifications(notifications, caminho);
    }

    public void salvarNotifications(String caminho) {
        DataStore.salvarNotifications(notifications, caminho);
    }
}