package br.edu.ifrs.view;

import br.edu.ifrs.model.Veiculo;
import br.edu.ifrs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroVeiculo extends JDialog {
    private JTextField txtPlaca, txtMarca, txtModelo, txtAno, txtCor;

    public TelaCadastroVeiculo(Frame parent) {
        super(parent, "Novo Veículo", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtPlaca = new JTextField();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtAno = new JTextField();
        txtCor = new JTextField();

        panel.add(new JLabel("Placa:"));
        panel.add(txtPlaca);
        panel.add(new JLabel("Marca:"));
        panel.add(txtMarca);
        panel.add(new JLabel("Modelo:"));
        panel.add(txtModelo);
        panel.add(new JLabel("Ano:"));
        panel.add(txtAno);
        panel.add(new JLabel("Cor:"));
        panel.add(txtCor);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        panel.add(new JLabel(""));
        panel.add(btnSalvar);

        add(panel);
    }

    private void salvar() {
        if (!txtPlaca.getText().matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}")) {
            JOptionPane.showMessageDialog(this, "Placa inválida! Use o formato Mercosul (ex: ABC1D23).");
            return;
        }

        try {
            Veiculo v = new Veiculo();
            v.setPlaca(txtPlaca.getText().toUpperCase());
            v.setMarca(txtMarca.getText());
            v.setModelo(txtModelo.getText());
            v.setAno(Integer.parseInt(txtAno.getText()));
            v.setCor(txtCor.getText());

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(v);
            transaction.commit();
            session.close();

            JOptionPane.showMessageDialog(this, "Veículo salvo com sucesso!");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano deve ser um número!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}