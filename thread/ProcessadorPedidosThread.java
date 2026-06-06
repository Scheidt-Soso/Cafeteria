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
}
