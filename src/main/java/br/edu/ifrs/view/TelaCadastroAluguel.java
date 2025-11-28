package br.edu.ifrs.view;

import br.edu.ifrs.model.Aluguel;
import br.edu.ifrs.model.Usuario;
import br.edu.ifrs.model.Veiculo;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaCadastroAluguel extends JDialog {
    private JComboBox<Usuario> cbUsuario;
    private JComboBox<Veiculo> cbVeiculo;
    private JTextField txtDataRetirada, txtDataDevolucao, txtValor;

    public TelaCadastroAluguel(Frame parent) {
        super(parent, "Novo Aluguel", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbUsuario = new JComboBox<>();
        cbVeiculo = new JComboBox<>();
        txtDataRetirada = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataDevolucao = new JTextField();
        txtValor = new JTextField();

        carregarCombos();

        panel.add(new JLabel("Usuário:"));
        panel.add(cbUsuario);
        panel.add(new JLabel("Veículo:"));
        panel.add(cbVeiculo);
        panel.add(new JLabel("Data Retirada (dd/MM/aaaa):"));
        panel.add(txtDataRetirada);
        panel.add(new JLabel("Data Devolução (dd/MM/aaaa):"));
        panel.add(txtDataDevolucao);
        panel.add(new JLabel("Valor Total (R$):"));
        panel.add(txtValor);

        JButton btnSalvar = new JButton("Confirmar Aluguel");
        btnSalvar.addActionListener(e -> salvar());

        panel.add(new JLabel(""));
        panel.add(btnSalvar);

        add(panel);
    }

    private void carregarCombos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Usuario> usuarios = session.createQuery("from Usuario", Usuario.class).list();
            List<Veiculo> veiculos = session.createQuery("from Veiculo", Veiculo.class).list();

            for (Usuario u : usuarios) cbUsuario.addItem(u);
            for (Veiculo v : veiculos) cbVeiculo.addItem(v);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar listas: " + e.getMessage());
        }
    }

    private void salvar() {
        try {
            Aluguel a = new Aluguel();
            a.setUsuario((Usuario) cbUsuario.getSelectedItem());
            a.setVeiculo((Veiculo) cbVeiculo.getSelectedItem());

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            a.setDataRetirada(LocalDate.parse(txtDataRetirada.getText(), fmt));
            a.setDataDevolucao(LocalDate.parse(txtDataDevolucao.getText(), fmt));
            a.setValorTotal(Double.parseDouble(txtValor.getText().replace(",", ".")));

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction t = session.beginTransaction();
            session.persist(a);
            t.commit();
            session.close();

            JOptionPane.showMessageDialog(this, "Aluguel registrado!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: Verifique as datas e valor.\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}