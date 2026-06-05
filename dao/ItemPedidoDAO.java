package dao;

import model.ItemPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    public void inserir(ItemPedido item) {

        String sql =
            "INSERT INTO itens_pedido(pedido_id, produto_id, quantidade, preco) VALUES (?, ?, ?, ?)";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, item.getPedidoId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPreco());

            stmt.executeUpdate();

            System.out.println("Item adicionado ao pedido!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemPedido> listarPorPedido(int pedidoId) {

        List<ItemPedido> itens = new ArrayList<>();

        String sql = "SELECT * FROM itens_pedido WHERE pedido_id = ?";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, pedidoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();

                    item.setPedidoId(rs.getInt("pedido_id"));
                    item.setProdutoId(rs.getInt("produto_id"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPreco(rs.getDouble("preco"));

                    itens.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
