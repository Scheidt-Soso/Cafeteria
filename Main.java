import dao.*;
import model.*;
import thread.ProcessadorPedidosThread;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

    public static void main(String[] args) {
        ProcessadorPedidosThread processador = new ProcessadorPedidosThread();
        processador.start();

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
            System.out.println("3. Listar itens de um pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> criarPedido();
                case 2 -> listarPedidos();
                case 3 -> listarItens();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarPedido() {
        listarClientes();
        System.out.print("ID do cliente: ");
        int clienteId = lerInteiro();

        List<ItemPedido> carrinho = new ArrayList<>();

        while (true) {
            listarProdutos();
            System.out.print("ID do produto: ");
            int produtoId = lerInteiro();

            Produto produto = produtoDAO.buscarPorId(produtoId);
            if (produto == null) {
                System.out.println("Produto não encontrado!");
                continue;
            }

            System.out.print("Quantidade: ");
            int quantidade = lerInteiro();

            if (quantidade <= 0) {
                System.out.println("Quantidade inválida!");
                continue;
            }

            if (quantidade > produto.getQuantidadeEstoque()) {
                System.out.println("Estoque insuficiente! Disponível apenas "
                    + produto.getQuantidadeEstoque() + " unidades de " + produto.getNome() + ".");
                continue;
            }

            carrinho.add(new ItemPedido(0, produtoId, quantidade, produto.getPreco()));
            System.out.println(produto.getNome() + " adicionado ao carrinho!");

            System.out.println("\n1. Adicionar mais produtos");
            System.out.println("2. Finalizar compra");
            System.out.println("3. Cancelar pedido");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            if (opcao == 2) {
                break;
            } else if (opcao == 3) {
                System.out.println("Pedido cancelado.");
                return;
            }
        }

        if (carrinho.isEmpty()) {
            return;
        }

        Pedido pedido = new Pedido(clienteId, StatusEnum.FILA);
        int pedidoId = pedidoDAO.inserir(pedido);

        if (pedidoId == -1) {
            System.out.println("Erro ao criar pedido!");
            return;
        }

        for (ItemPedido item : carrinho) {
            item.setPedidoId(pedidoId);
            itemPedidoDAO.inserir(item);

            Produto p = produtoDAO.buscarPorId(item.getProdutoId());
            int novoEstoque = p.getQuantidadeEstoque() - item.getQuantidade();
            produtoDAO.atualizarEstoque(item.getProdutoId(), novoEstoque);
        }

        System.out.println("Pedido #" + pedidoId + " enviado para a fila de processamento!");
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

}
