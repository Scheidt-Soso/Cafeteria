package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Pedido;
import model.StatusEnum;

import java.sql.Statement;

public class PedidoDAO {

    public int inserir(Pedido pedido) {

        String sql =
            "INSERT INTO pedidos(cliente_id, status) VALUES (?, ?)";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setInt(1, pedido.getClienteId());
            stmt.setString(2, pedido.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pedido.setId(id);
                    System.out.println("Pedido criado! ID: " + id);
                    return id;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
                pedido.setStatus(
                    StatusEnum.valueOf(rs.getString("status"))
                );

                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                if (dataCriacao != null) {
                    pedido.setDataCriacao(dataCriacao);
                }

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public void atualizarStatus(int pedidoId, StatusEnum status) {

        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, status.name());
            stmt.setInt(2, pedidoId);

            stmt.executeUpdate();

            System.out.println("Status atualizado!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarPorStatus(StatusEnum status) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE status = ?";
        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setClienteId(rs.getInt("cliente_id"));
                    pedido.setStatus(StatusEnum.valueOf(rs.getString("status")));
                    Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                    if (dataCriacao != null) {
                        pedido.setDataCriacao(dataCriacao);
                    }
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
}