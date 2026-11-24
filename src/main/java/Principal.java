
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Principal {

    private static final Scanner scanner = new Scanner(System.in);

    // Configuracoes de conexao com o MySQL
    private static final String URL_SERVIDOR = "jdbc:mysql://localhost:3306/";
    private static final String URL_BANCO = "jdbc:mysql://localhost:3306/pessoadb";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    public static void main(String[] args) {
        try {
            // Cria o banco de dados e a tabela, se necessario.
            criarBancoETabela();

            // Estabelece conexao com o banco.
            try (Connection conexao = conectar()) {

                //Variável de controle do menu
                int opcao;
                do {
                    // Apresenta o menu.
                    System.out.println("\n=== MENU CADASTRO DE PESSOAS ===");
                    System.out.println("1 - Cadastrar pessoa");
                    System.out.println("2 - Listar pessoas");
                    System.out.println("3 - Consultar pessoa por ID");
                    System.out.println("4 - Atualizar pessoa");
                    System.out.println("5 - Excluir pessoa");
                    System.out.println("9 - Sair");

                    System.out.print("Escolha uma opcao: ");

                    try {
                        opcao = Integer.parseInt(scanner.nextLine());

                        switch (opcao) {
                            case 1:
                                cadastrar(conexao);
                                break;

                            case 2:
                                listar(conexao);
                                break;

                            case 3:
                                consultar(conexao);
                                break;

                            case 4:
                                atualizar(conexao);
                                break;

                            case 5:
                                excluir(conexao);
                                break;

                            case 9:
                                System.out.println("Programa encerrado.");
                                break;

                            default:
                                System.out.println("Opcao invalida.");
                                break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Informe uma opcao numerica valida.");
                        opcao = 0;

                    } catch (SQLException e) {
                        System.out.println("Erro no MySQL: " + e.getMessage());
                        opcao = 0;
                    }

                } while (opcao != 9);
            }

        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao MySQL.");
            System.out.println(e.getMessage());

        } finally {
            scanner.close();
        }
    }

    /**
     * Cria o banco de dados e a tabela pessoa, caso nao existam.
     */
    private static void criarBancoETabela() throws SQLException {

        // Conecta ao servidor MySQL, sem selecionar um banco.
        try (Connection conexao = DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
             Statement comando = conexao.createStatement()) {

            // Cria o banco de dados se não existir
            comando.executeUpdate("CREATE DATABASE IF NOT EXISTS pessoadb");

            System.out.println("Banco de dados verificado/criado!");
        }

        // Conecta ao banco de dados criado.
        try (Connection conexao = conectar();
             Statement comando = conexao.createStatement()) {

            // Cria a tabela pessoa senão existir.
            String sql = "CREATE TABLE IF NOT EXISTS pessoa ("
                    + " id INT AUTO_INCREMENT PRIMARY KEY,"
                    + " nome VARCHAR(150) NOT NULL)";

            comando.executeUpdate(sql);

            System.out.println("Tabela pessoa verificada/criada!");
        }
    }

    /**
     * Estabelece conexao com o banco de dados.
     */
    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL_BANCO, USUARIO, SENHA);
    }

    /**
     * Cadastra uma pessoa no banco de dados.
     */
    private static void cadastrar(Connection conexao) throws SQLException {

        System.out.println("\n--- CADASTRAR PESSOA ---");

        // Entrada: recebe o nome.
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("O nome nao pode ser vazio.");
        } else {
            // Processamento: prepara o comando SQL.
            String sql = "INSERT INTO pessoa (nome) VALUES (?)";

            try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                comando.setString(1, nome);

                // Executa a insercao.
                comando.executeUpdate();

                // Recupera o ID gerado automaticamente.
                try (ResultSet resultado = comando.getGeneratedKeys()) {

                    if (resultado.next()) {
                        int id = resultado.getInt(1);
                        System.out.println("Pessoa cadastrada com sucesso!");
                        System.out.println("ID: " + id);
                    }
                }
            }
        }
    }

    /**
     * Lista todas as pessoas cadastradas.
     */
    private static void listar(Connection conexao) throws SQLException {

        System.out.println("\n--- LISTA DE PESSOAS ---");

        boolean encontrou = false;

        // Consulta as pessoas em ordem alfabetica.
        String sql = "SELECT id, nome FROM pessoa ORDER BY id";

        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                exibirPessoa(resultado);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma pessoa cadastrada.");
        }
    }

    /**
     * Consulta uma pessoa pelo ID.
     */
    private static void consultar(Connection conexao) throws SQLException {

        System.out.println("\n--- CONSULTAR PESSOA ---");
        int id = lerId();
        if (id <= 0) {
            System.out.println("ID invalido.");
        } else {
            consultarPessoa(conexao, id);
        }
    }

    /**
     * Consulta e apresenta uma pessoa pelo ID.
     *
     * @return true se a pessoa foi encontrada.
     */
    private static boolean consultarPessoa(Connection conexao, int id) throws SQLException {

        boolean encontrou = false;

        String sql = "SELECT id, nome FROM pessoa WHERE id = ?";

        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    exibirPessoa(resultado);
                    encontrou = true;
                }
            }
        }
        if (!encontrou) {
            System.out.println("Pessoa nao encontrada.");
        }

        return encontrou;
    }

    /**
     * Atualiza o nome de uma pessoa.
     */
    private static void atualizar(Connection conexao) throws SQLException {
        
        System.out.println("\n--- ATUALIZAR PESSOA ---");
        
// Entrada: recebe o ID.
        int id = lerId();
        if (id <= 0) {
            System.out.println("ID invalido.");
        } else {
            // Verifica se a pessoa existe.
            System.out.println("Dados atuais:");
            if (consultarPessoa(conexao, id)) {

                // Entrada: recebe o novo nome.
                System.out.print("Novo nome: ");
                String nome = scanner.nextLine().trim();

                if (nome.isEmpty()) {
                    System.out.println("O nome nao pode ser vazio.");
                } else {
                    // Processamento: atualiza o registro.
                    String sql = "UPDATE pessoa SET nome = ? WHERE id = ?";

                    try (PreparedStatement comando = conexao.prepareStatement(sql)) {

                        comando.setString(1, nome);
                        comando.setInt(2, id);

                        int linhasAlteradas = comando.executeUpdate();

                        if (linhasAlteradas > 0) {
                            System.out.println("Pessoa atualizada com sucesso!");
                        } else {
                            System.out.println("Nenhuma alteracao foi realizada.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Exclui uma pessoa do banco de dados.
     */
    private static void excluir(Connection conexao) throws SQLException {

        System.out.println("\n--- EXCLUIR PESSOA ---");

        // Entrada: recebe o ID.
        int id = lerId();
        if (id <= 0) {
            System.out.println("ID invalido.");
        } else {
            // Verifica se a pessoa existe.
            System.out.println("Dados da pessoa:");

            if (consultarPessoa(conexao, id)) {
                // Entrada: solicita confirmacao.
                System.out.print("Confirma a exclusao? (S/N): ");
                String confirmacao = scanner.nextLine().trim();

                if (confirmacao.equalsIgnoreCase("S")) {
                    
                    // Processamento: exclui o registro.
                    String sql = "DELETE FROM pessoa WHERE id = ?";

                    try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                        comando.setInt(1, id);
                        int linhasExcluidas = comando.executeUpdate();
                        if (linhasExcluidas > 0) {
                            System.out.println("Pessoa excluida com sucesso!");
                        } else {
                            System.out.println("Nenhuma pessoa foi excluida.");
                        }
                    }
                } else {
                    System.out.println("Exclusao cancelada.");
                }
            }
        }
    }

    /**
     * Le e converte o ID informado pelo usuario.
     */
    private static int lerId() {
        System.out.print("Informe o ID da pessoa: ");
        int id = -1;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        }
        return id;
    }

    /**
     * Apresenta os dados de uma pessoa.
     */
    private static void exibirPessoa(ResultSet resultado) throws SQLException {
        System.out.println("------------------------------");
        System.out.println("ID: " + resultado.getInt("id"));
        System.out.println("Nome: " + resultado.getString("nome"));
    }
}
