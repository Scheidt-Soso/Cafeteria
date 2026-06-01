import java.sql.Connection;
import dao.ConexaoFactory;

public class TesteConexao {

    public static void main(String[] args) {

        try {
            Connection conexao = ConexaoFactory.getConexao();

            System.out.println("Conexão realizada com sucesso!");

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro na conexão:");
            e.printStackTrace();

        }
    }
}