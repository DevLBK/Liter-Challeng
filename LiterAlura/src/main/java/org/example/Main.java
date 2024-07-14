import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String BASE_URL = "https://gutendex.com/books";

    public static void main(String[] args) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL);

        try {
            // Executando a requisição HTTP
            org.apache.http.HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Verificando a resposta da API (debug)
            System.out.println("Resposta da API: " + jsonResponse);

            // Verificando se a resposta começa com '[' (indicando um array JSON válido)
            if (jsonResponse.startsWith("[")) {
                // Convertendo a resposta para um JSONArray
                JSONArray booksArray = new JSONArray(jsonResponse);

                // Iniciando a interação com o usuário
                Scanner scanner = new Scanner(System.in);
                int option;

                do {
                    System.out.println("\nEscolha uma opção:");
                    System.out.println("1 - Listar todos os livros");
                    System.out.println("2 - Buscar por título");
                    System.out.println("3 - Sair");
                    System.out.print("Opção: ");
                    option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            listBooks(booksArray);
                            break;
                        case 2:
                            System.out.print("Digite o título do livro: ");
                            scanner.nextLine(); // Consumir a quebra de linha pendente
                            String searchTerm = scanner.nextLine();
                            searchByTitle(booksArray, searchTerm);
                            break;
                        case 3:
                            System.out.println("Saindo...");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                } while (option != 3);

                scanner.close();
            } else {
                System.out.println("Resposta da API não está no formato esperado.");
                System.out.println("Resposta recebida: " + jsonResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para listar todos os livros
    private static void listBooks(JSONArray booksArray) {
        System.out.println("\nLista de Livros Disponíveis:");
        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject book = booksArray.getJSONObject(i);
            String title = book.getString("title");
            String authors = book.getJSONArray("authors").join(", ");
            System.out.println("Título: " + title);
            System.out.println("Autores: " + authors);
            System.out.println("---------------------");
        }
    }

    // Método para buscar por título
    private static void searchByTitle(JSONArray booksArray, String searchTerm) {
        boolean found = false;
        System.out.println("\nResultados da Busca por '" + searchTerm + "':");
        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject book = booksArray.getJSONObject(i);
            String title = book.getString("title");

            if (title.toLowerCase().contains(searchTerm.toLowerCase())) {
                found = true;
                String authors = book.getJSONArray("authors").join(", ");
                System.out.println("Título: " + title);
                System.out.println("Autores: " + authors);
                System.out.println("---------------------");
            }
        }
        if (!found) {
            System.out.println("Nenhum livro encontrado com o título '" + searchTerm + "'.");
        }
    }
}
