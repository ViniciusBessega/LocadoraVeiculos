package br.edu.ifrs.view;

import br.edu.ifrs.model.Veiculo;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaListaVeiculos extends JDialog {
    private JTable tabela;
    private DefaultTableModel tableModel;

    public TelaListaVeiculos(Frame parent) {
        super(parent, "Gestão de Veículos", true); // Modal
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Placa", "Marca", "Modelo", "Ano", "Cor"}, 0);
        tabela = new JTable(tableModel);

        tabela.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Veículo");
        JButton btnRecarregar = new JButton("Atualizar Lista");

        btnAdicionar.addActionListener(e -> {
            new TelaCadastroVeiculo(parent).setVisible(true);
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
            List<Veiculo> veiculos = session.createQuery("from Veiculo", Veiculo.class).list();

            for (Veiculo v : veiculos) {
                tableModel.addRow(new Object[]{
                        v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAno(), v.getCor()
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados: " + e.getMessage());
        }
    }
}