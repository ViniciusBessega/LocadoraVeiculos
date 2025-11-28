package br.edu.ifrs;

import br.edu.ifrs.model.Usuario;
import br.edu.ifrs.util.HibernateUtil;
import br.edu.ifrs.view.TelaPrincipal;
import com.formdev.flatlaf.FlatDarkLaf;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Falha ao carregar tema.");
        }

        // Inicializa o Hibernate e insere dados de teste (se necessário)
        inicializarDados();

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }

    private static void inicializarDados() {
        // Cria uma thread para iniciar o Hibernate antes da interface abrir
        new Thread(() -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // Verifica se já existe usuário, se não, cria um padrão
                Long count = session.createQuery("select count(u) from Usuario u", Long.class).uniqueResult();

                if (count == 0) {
                    Transaction t = session.beginTransaction();
                    Usuario u = new Usuario("Cliente Teste", "cliente@email.com", "99999-9999");
                    session.persist(u);
                    t.commit();
                    System.out.println("Usuário de teste criado com sucesso.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}