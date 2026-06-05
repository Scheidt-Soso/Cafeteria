package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Pedido;

public class PedidoDAO {

    public void inserir(Pedido pedido) {

        String sql =
            "INSERT INTO pedidos(cliente_id, status) VALUES (?, ?)";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, pedido.getClienteId());
            stmt.setString(2, pedido.getStatus().name());

            stmt.executeUpdate();

            System.out.println("Pedido criado!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listar() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM pedidos";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Pedido pedido = new Pedido();

                pedido.setId(rs.getInt("id"));
                pedido.setClienteId(rs.getInt("cliente_id"));

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }
}