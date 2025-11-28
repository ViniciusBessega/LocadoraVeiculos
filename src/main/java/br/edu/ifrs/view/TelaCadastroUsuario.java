package br.edu.ifrs.view;

import br.edu.ifrs.model.Usuario;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroUsuario extends JDialog {
    private JTextField txtNome, txtEmail, txtTelefone;

    public TelaCadastroUsuario(Frame parent) {
        super(parent, "Novo Usuário", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();

        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("E-mail:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Telefone:"));
        panel.add(txtTelefone);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        panel.add(new JLabel("")); // Espaçador
        panel.add(btnSalvar);

        add(panel);
    }

    private void salvar() {
        if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha pelo menos Nome e E-mail.");
            return;
        }

        try {
            Usuario u = new Usuario();
            u.setNome(txtNome.getText());
            u.setEmail(txtEmail.getText());
            u.setTelefone(txtTelefone.getText());

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(u);
            transaction.commit();
            session.close();

            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}