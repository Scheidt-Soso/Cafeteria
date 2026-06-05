package dao;

import model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void inserir(Categoria categoria) {

        String sql = "INSERT INTO categorias(nome) VALUES (?)";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, categoria.getNome());

            stmt.executeUpdate();

            System.out.println("Categoria cadastrada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Categoria categoria) {

        String sql = "UPDATE categorias SET nome = ? WHERE id = ?";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getId());

            stmt.executeUpdate();

            System.out.println("Categoria atualizada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM categorias WHERE id = ?";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Categoria deletada com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Categoria> listar() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categorias";

        try (
            Connection conn = ConexaoFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Categoria categoria = new Categoria();

                categoria.setId(rs.getInt("id"));
                categoria.setNome(rs.getString("nome"));

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }
}
