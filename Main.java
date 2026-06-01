import dao.ConexaoFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Tentando conectar ao banco Desafio_Integrador...");
        
        try (Connection conexao = ConexaoFactory.getConexao()) {
            if (conexao != null) {
                System.out.println("PARABÉNS! Conexão estabelecida com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("ERRO AO CONECTAR:");
            System.err.println("Mensagem do erro: " + e.getMessage());
        }
    }
}