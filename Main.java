import dao.*;
import model.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== CAFETERIA - SISTEMA DE GESTÃO =====");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Produtos");
            System.out.println("3. Gerenciar Pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuProdutos();
                case 3 -> menuPedidos();
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // =========================== CLIENTES ===========================

    private static void menuClientes() {
        while (true) {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> listarClientes();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();

        Cliente cliente = new Cliente(nome, email, telefone);
        clienteDAO.inserir(cliente);
    }

    private static void listarClientes() {
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\nID | Nome | Email | Telefone");
        System.out.println("-".repeat(60));
        for (Cliente c : clientes) {
            System.out.printf("%d | %s | %s | %s%n",
                c.getId(), c.getNome(), c.getEmail(), c.getTelefone());
        }
    }

    // =========================== PRODUTOS ===========================

    private static void menuProdutos() {
        while (true) {
            System.out.println("\n--- PRODUTOS ---");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Listar produtos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Preço: ");
        double preco = lerDouble();
        System.out.print("Quantidade em estoque: ");
        int estoque = lerInteiro();

        System.out.println("Categorias disponíveis:");
        for (CategoriaEnum cat : CategoriaEnum.values()) {
            System.out.println("  " + cat.ordinal() + ". " + cat.name());
        }
        System.out.print("Escolha o número da categoria: ");
        int catIdx = lerInteiro();

        if (preco <= 0 || estoque < 0 || catIdx < 0 || catIdx >= CategoriaEnum.values().length) {
            System.out.println("Dados inválidos!");
            return;
        }

        CategoriaEnum categoria = CategoriaEnum.values()[catIdx];
        Produto produto = new Produto(nome, preco, estoque, categoria);
        produtoDAO.inserir(produto);
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoDAO.listar();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("\nID | Nome | Preço | Estoque | Categoria");
        System.out.println("-".repeat(70));
        for (Produto p : produtos) {
            System.out.printf("%d | %s | %.2f | %d | %s%n",
                p.getId(), p.getNome(), p.getPreco(),
                p.getQuantidadeEstoque(), p.getCategoria().name());
        }
    }

    // =========================== PEDIDOS ===========================

    private static void menuPedidos() {
        while (true) {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Criar pedido");
            System.out.println("2. Listar pedidos");
            System.out.println("3. Adicionar item ao pedido");
            System.out.println("4. Listar itens de um pedido");
            System.out.println("5. Atualizar status do pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> criarPedido();
                case 2 -> listarPedidos();
                case 3 -> adicionarItem();
                case 4 -> listarItens();
                case 5 -> atualizarStatus();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarPedido() {
        listarClientes();
        System.out.print("ID do cliente: ");
        int clienteId = lerInteiro();

        Pedido pedido = new Pedido(clienteId, StatusEnum.ABERTO);
        pedidoDAO.inserir(pedido);
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoDAO.listar();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        System.out.println("\nID | Cliente ID | Status | Data de Criação");
        System.out.println("-".repeat(60));
        for (Pedido p : pedidos) {
            System.out.printf("%d | %d | %s | %s%n",
                p.getId(), p.getClienteId(), p.getStatus().name(),
                p.getDataCriacao() != null ? p.getDataCriacao().toString() : "-");
        }
    }

    private static void adicionarItem() {
        listarPedidos();
        System.out.print("ID do pedido: ");
        int pedidoId = lerInteiro();

        listarProdutos();
        System.out.print("ID do produto: ");
        int produtoId = lerInteiro();

        System.out.print("Quantidade: ");
        int quantidade = lerInteiro();

        System.out.print("Preço unitário: ");
        double preco = lerDouble();

        if (quantidade <= 0 || preco <= 0) {
            System.out.println("Quantidade e preço devem ser positivos!");
            return;
        }

        ItemPedido item = new ItemPedido(pedidoId, produtoId, quantidade, preco);
        itemPedidoDAO.inserir(item);
    }

    private static void listarItens() {
        System.out.print("ID do pedido: ");
        int pedidoId = lerInteiro();

        List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado para este pedido.");
            return;
        }
        System.out.println("\nProduto ID | Quantidade | Preço Unitário | Subtotal");
        System.out.println("-".repeat(60));
        double total = 0;
        for (ItemPedido item : itens) {
            double subtotal = item.getQuantidade() * item.getPreco();
            System.out.printf("%d | %d | %.2f | %.2f%n",
                item.getProdutoId(), item.getQuantidade(), item.getPreco(), subtotal);
            total += subtotal;
        }
        System.out.println("-".repeat(60));
        System.out.printf("TOTAL: %.2f%n", total);
    }

    private static void atualizarStatus() {
        listarPedidos();
        System.out.print("ID do pedido: ");
        int pedidoId = lerInteiro();

        System.out.println("Status disponíveis:");
        for (StatusEnum s : StatusEnum.values()) {
            System.out.println("  " + s.ordinal() + ". " + s.name());
        }
        System.out.print("Escolha o número do status: ");
        int statusIdx = lerInteiro();

        if (statusIdx < 0 || statusIdx >= StatusEnum.values().length) {
            System.out.println("Status inválido!");
            return;
        }

        StatusEnum novoStatus = StatusEnum.values()[statusIdx];
        pedidoDAO.atualizarStatus(pedidoId, novoStatus);
    }
}
