package Wheels;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<Notification> notifications = new ArrayList<>();

    public void verificarEEnviarNotificacoes(List<Hire> hires) {
        for (Hire hire : hires) {
            if (hire.isOverdue()) {
                String msg = "Aluguel ID " + hire.getHireId() + " está em atraso para o cliente "
                        + hire.getCustomer().getName() + " com a bike nº " + hire.getBike().getBikeNumber();

                Notification notification = new Notification(
                        hire.getHireId(),
                        hire.getCustomer().getName(),
                        hire.getBike().getBikeNumber(),
                        msg
                );

                notifications.add(notification);

                // Simula o envio do email
                System.out.println("Enviando notificação para " + hire.getCustomer().getName() + ": " + msg);
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