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
            System.out.println("3. Editar produto");
            System.out.println("4. Deletar produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> editarProduto();
                case 4 -> deletarProduto();
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
            System.out.println("3. Cancelar pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> criarPedido();
                case 2 -> listarPedidos();
                case 3 -> cancelarPedido();
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

        boolean estoqueOk = true;
        for (ItemPedido item : carrinho) {
            Produto p = produtoDAO.buscarPorId(item.getProdutoId());
            if (p == null || item.getQuantidade() > p.getQuantidadeEstoque()) {
                System.out.println("Estoque insuficiente para " + (p != null ? p.getNome() : "produto " + item.getProdutoId())
                    + ". Disponível: " + (p != null ? p.getQuantidadeEstoque() : 0));
                estoqueOk = false;
            }
        }

        if (!estoqueOk) {
            Pedido pedido = new Pedido(clienteId, StatusEnum.CANCELADO);
            int pedidoId = pedidoDAO.inserir(pedido);
            if (pedidoId != -1) {
                for (ItemPedido item : carrinho) {
                    item.setPedidoId(pedidoId);
                    itemPedidoDAO.inserir(item);
                }
                System.out.println("Pedido #" + pedidoId + " cancelado por falta de estoque.");
            }
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
        for (Pedido p : pedidos) {
            System.out.println("Pedido #" + p.getId() + " | Cliente: " + p.getClienteId()
                + " | Status: " + p.getStatus().name()
                + " | Data: " + (p.getDataCriacao() != null ? p.getDataCriacao().toString() : "-"));

            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(p.getId());
            if (itens.isEmpty()) {
                System.out.println("  (nenhum item)");
            } else {
                double total = 0;
                for (ItemPedido item : itens) {
                    Produto prod = produtoDAO.buscarPorId(item.getProdutoId());
                    String nome = prod != null ? prod.getNome() : "Produto #" + item.getProdutoId();
                    double subtotal = item.getQuantidade() * item.getPreco();
                    System.out.printf("  %s | Qtd: %d | Preço: %.2f | Subtotal: %.2f%n",
                        nome, item.getQuantidade(), item.getPreco(), subtotal);
                    total += subtotal;
                }
                System.out.printf("  TOTAL: %.2f%n", total);
            }
            System.out.println("-".repeat(70));
        }
    }

    private static void cancelarPedido() {
        listarPedidos();
        System.out.print("ID do pedido a cancelar: ");
        int pedidoId = lerInteiro();

        Pedido pedido = null;
        for (Pedido p : pedidoDAO.listar()) {
            if (p.getId() == pedidoId) {
                pedido = p;
                break;
            }
        }

        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        if (pedido.getStatus() == StatusEnum.CANCELADO || pedido.getStatus() == StatusEnum.FINALIZADO) {
            System.out.println("Este pedido já foi " + pedido.getStatus().name().toLowerCase() + ".");
            return;
        }

        pedidoDAO.atualizarStatus(pedidoId, StatusEnum.CANCELADO);

        List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
        for (ItemPedido item : itens) {
            Produto p = produtoDAO.buscarPorId(item.getProdutoId());
            if (p != null) {
                int novoEstoque = p.getQuantidadeEstoque() + item.getQuantidade();
                produtoDAO.atualizarEstoque(item.getProdutoId(), novoEstoque);
            }
        }

        System.out.println("Pedido #" + pedidoId + " cancelado! Itens devolvidos ao estoque.");
    }

    private static void editarProduto() {
        listarProdutos();
        System.out.print("ID do produto para editar: ");
        int id = lerInteiro();

        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Deixe em branco para manter o valor atual.");
        System.out.print("Nome (" + produto.getNome() + "): ");
        String nome = scanner.nextLine().trim();
        if (!nome.isEmpty()) produto.setNome(nome);

        System.out.print("Preço (" + produto.getPreco() + "): ");
        String precoStr = scanner.nextLine().trim();
        if (!precoStr.isEmpty()) {
            try {
                double preco = Double.parseDouble(precoStr);
                if (preco > 0) produto.setPreco(preco);
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido, mantendo valor atual.");
            }
        }

        System.out.print("Quantidade em estoque (" + produto.getQuantidadeEstoque() + "): ");
        String estoqueStr = scanner.nextLine().trim();
        if (!estoqueStr.isEmpty()) {
            try {
                int estoque = Integer.parseInt(estoqueStr);
                if (estoque >= 0) produto.setQuantidadeEstoque(estoque);
            } catch (NumberFormatException e) {
                System.out.println("Estoque inválido, mantendo valor atual.");
            }
        }

        System.out.println("Categoria atual: " + produto.getCategoria().name());
        for (CategoriaEnum cat : CategoriaEnum.values()) {
            System.out.println("  " + cat.ordinal() + ". " + cat.name());
        }
        System.out.print("Nova categoria (ou vazio para manter): ");
        String catStr = scanner.nextLine().trim();
        if (!catStr.isEmpty()) {
            try {
                int catIdx = Integer.parseInt(catStr);
                if (catIdx >= 0 && catIdx < CategoriaEnum.values().length) {
                    produto.setCategoria(CategoriaEnum.values()[catIdx]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Categoria inválida, mantendo atual.");
            }
        }

        produtoDAO.atualizar(produto);
    }

    private static void deletarProduto() {
        listarProdutos();
        System.out.print("ID do produto para deletar: ");
        int id = lerInteiro();

        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja deletar \"" + produto.getNome() + "\"? (s/N): ");
        String confirmacao = scanner.nextLine().trim();
        if (confirmacao.equalsIgnoreCase("s")) {
            produtoDAO.deletar(id);
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}
