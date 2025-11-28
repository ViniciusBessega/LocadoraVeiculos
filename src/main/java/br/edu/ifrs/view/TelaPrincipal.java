package br.edu.ifrs.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema Locadora - POO2");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizar
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Bem-vindo à Locadora", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenu menuProcessos = new JMenu("Processos");

        JMenuItem itemVeiculos = new JMenuItem("Gerenciar Veículos");
        itemVeiculos.addActionListener(e -> new TelaListaVeiculos(this).setVisible(true));

        JMenuItem itemUsuarios = new JMenuItem("Gerenciar Usuários");
        itemUsuarios.addActionListener(e -> new TelaListaUsuarios(this).setVisible(true));

        JMenuItem itemGerenciarAlugueis = new JMenuItem("Gerenciar Aluguéis (Histórico)");
        itemGerenciarAlugueis.addActionListener(e -> new TelaListaAlugueis(this).setVisible(true));

        JMenuItem itemNovoAluguel = new JMenuItem("Novo Aluguel (Rápido)");
        itemNovoAluguel.addActionListener(e -> new TelaCadastroAluguel(this).setVisible(true));

        menuCadastros.add(itemVeiculos);
        menuCadastros.add(itemUsuarios);

        menuProcessos.add(itemGerenciarAlugueis);
        menuProcessos.addSeparator();
        menuProcessos.add(itemNovoAluguel);

        menuBar.add(menuCadastros);
        menuBar.add(menuProcessos);
        setJMenuBar(menuBar);
    }
}