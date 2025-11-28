package br.edu.ifrs.view;

import br.edu.ifrs.model.Usuario;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaListaUsuarios extends JDialog {
    private JTable tabela;
    private DefaultTableModel tableModel;

    public TelaListaUsuarios(Frame parent) {
        super(parent, "Gestão de Usuários", true);
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "E-mail", "Telefone"}, 0);
        tabela = new JTable(tableModel);
        tabela.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Usuário");
        JButton btnRecarregar = new JButton("Atualizar Lista");

        btnAdicionar.addActionListener(e -> {
            new TelaCadastroUsuario(parent).setVisible(true);
            carregarDados();
        });

        btnRecarregar.addActionListener(e -> carregarDados());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnRecarregar);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Usuario> usuarios = session.createQuery("from Usuario", Usuario.class).list();

            for (Usuario u : usuarios) {
                tableModel.addRow(new Object[]{
                        u.getId(), u.getNome(), u.getEmail(), u.getTelefone()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários: " + e.getMessage());
        }
    }
}