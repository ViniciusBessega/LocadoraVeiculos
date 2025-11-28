package br.edu.ifrs.view;

import br.edu.ifrs.model.Aluguel;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaListaAlugueis extends JDialog {
    private JTable tabela;
    private DefaultTableModel tableModel;

    public TelaListaAlugueis(Frame parent) {
        super(parent, "Gerenciamento de Aluguéis", true);
        setSize(850, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Cliente", "Veículo", "Retirada", "Devolução", "Valor (R$)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override // Impede edição direta na célula
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        tabela.setAutoCreateRowSorter(true); // Permite ordenar clicando no cabeçalho
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Selecionar apenas um por vez

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        JButton btnNovo = new JButton("Novo Aluguel");
        JButton btnExcluir = new JButton("Excluir Selecionado"); // NOVO BOTÃO
        JButton btnRecarregar = new JButton("Atualizar Lista");

        btnNovo.addActionListener(e -> {
            new TelaCadastroAluguel(parent).setVisible(true);
            carregarDados();
        });

        btnExcluir.addActionListener(e -> excluirAluguel());

        btnRecarregar.addActionListener(e -> carregarDados());

        panelBotoes.add(btnNovo);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnRecarregar);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Aluguel> alugueis = session.createQuery("from Aluguel", Aluguel.class).list();

            for (Aluguel a : alugueis) {
                tableModel.addRow(new Object[]{
                        a.getId(),
                        a.getUsuario().getNome(),
                        a.getVeiculo().getPlaca() + " - " + a.getVeiculo().getModelo(),
                        a.getDataRetirada().format(fmt),
                        a.getDataDevolucao().format(fmt),
                        String.format("%.2f", a.getValorTotal())
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar aluguéis: " + e.getMessage());
        }
    }

    private void excluirAluguel() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluguel na tabela para excluir.");
            return;
        }


        Long idAluguel = (Long) tabela.getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 0);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o aluguel ID " + idAluguel + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction t = session.beginTransaction();

                Aluguel aluguel = session.get(Aluguel.class, idAluguel);
                if (aluguel != null) {
                    session.remove(aluguel);
                    t.commit();
                    JOptionPane.showMessageDialog(this, "Aluguel excluído com sucesso!");
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(this, "Aluguel não encontrado no banco de dados.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}