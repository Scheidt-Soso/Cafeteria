package thread;

import dao.ConexaoFactory;
import model.StatusEnum;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessadorPedidosThread extends Thread {

    public ProcessadorPedidosThread() {
        super("ProcessadorPedidos");
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                processarPedidosFila();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void processarPedidosFila() {
        List<Integer> pedidosFila = new ArrayList<>();

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM pedidos WHERE status = ?")
        ) {
            stmt.setString(1, StatusEnum.FILA.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidosFila.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for (int pedidoId : pedidosFila) {
            try (Connection conn = ConexaoFactory.getConexao()) {

                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE pedidos SET status = ? WHERE id = ? AND status = ?")) {
                    stmt.setString(1, StatusEnum.PROCESSANDO.name());
                    stmt.setInt(2, pedidoId);
                    stmt.setString(3, StatusEnum.FILA.name());
                    int linhas = stmt.executeUpdate();
                    if (linhas == 0) {
                        continue;
                    }
                }

                System.out.println("[Thread] Pedido " + pedidoId + ": PROCESSANDO...");
                Thread.sleep(8000);

                if (!verificarEstoque(conn, pedidoId)) {
                    try (PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE pedidos SET status = ? WHERE id = ?")) {
                        stmt.setString(1, StatusEnum.CANCELADO.name());
                        stmt.setInt(2, pedidoId);
                        stmt.executeUpdate();
                    }
                    System.out.println("[Thread] Pedido " + pedidoId + ": CANCELADO (estoque insuficiente)!");
                    continue;
                }

                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE pedidos SET status = ? WHERE id = ?")) {
                    stmt.setString(1, StatusEnum.FINALIZADO.name());
                    stmt.setInt(2, pedidoId);
                    stmt.executeUpdate();
                }

                System.out.println("[Thread] Pedido " + pedidoId + ": FINALIZADO!");

            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean verificarEstoque(Connection conn, int pedidoId) throws SQLException {
        String sql = "SELECT ip.produto_id, ip.quantidade, p.quantidade_estoque "
            + "FROM itens_pedido ip "
            + "JOIN produtos p ON p.id = ip.produto_id "
            + "WHERE ip.pedido_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quantidadePedido = rs.getInt("quantidade");
                    int quantidadeEstoque = rs.getInt("quantidade_estoque");
                    if (quantidadePedido > quantidadeEstoque) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
