import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class ProdutoApp extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private GenericDao<Produto> dao;
    private List<Produto> produtos;

    public ProdutoApp() {
        dao = new GenericDao<>("produtos.dat");
        produtos = carregarProdutos();

        setTitle("Cadastro de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"Código", "Descrição", "Preço"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        botoes.add(btnAdicionar);
        botoes.add(btnAtualizar);
        botoes.add(btnRemover);

        add(botoes, BorderLayout.SOUTH);

        atualizarTabela();

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnRemover.addActionListener(e -> removerProduto());
    }

    private void adicionarProduto() {
        try {
            String codStr = JOptionPane.showInputDialog("Código:");
            String desc = JOptionPane.showInputDialog("Descrição:");
            String precoStr = JOptionPane.showInputDialog("Preço:");

            int cod = Integer.parseInt(codStr);
            double preco = Double.parseDouble(precoStr);

            Produto novo = new Produto(cod, desc, preco);
            if (produtos.contains(novo)) {
                JOptionPane.showMessageDialog(this, "Código já existe!");
                return;
            }
            produtos.add(novo);
            salvar();
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida!");
        }
    }

    private void atualizarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar.");
            return;
        }

        Produto p = produtos.get(linha);
        String novaDesc = JOptionPane.showInputDialog("Nova descrição:", p.getDescricao());
        String novoPrecoStr = JOptionPane.showInputDialog("Novo preço:", p.getPreco());

        try {
            double novoPreco = Double.parseDouble(novoPrecoStr);
            p.setDescricao(novaDesc);
            p.setPreco(novoPreco);
            salvar();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Preço inválido.");
        }
    }

    private void removerProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }

        produtos.remove(linha);
        salvar();
        atualizarTabela();
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Produto p : produtos) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getDescricao(), p.getPreco()});
        }
    }

    private void salvar() {
        try {
            dao.salvar(produtos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar.");
        }
    }

    private List<Produto> carregarProdutos() {
        try {
            return dao.listar();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProdutoApp().setVisible(true));
    }
}
