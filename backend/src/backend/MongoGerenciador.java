/*
 * Classe que registra o conteúdo do DBF no MongoDB
 */
package backend;

import com.mongodb.*;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;

/**
 *
 * @author Leonardo Rezende e Thiago Silva
 *
 */
public class MongoGerenciador {

    private MongoClientURI conexaoString;
    private MongoClient mongoClient;
    DBFGerenciador dbfG;

    public MongoGerenciador(String conexao, String arquivoDBF) {
        conexaoString = new MongoClientURI(conexao);
        dbfG = new DBFGerenciador(arquivoDBF);
        try {
            mongoClient = new MongoClient(conexaoString);
        } catch (UnknownHostException e) {
            System.out.println("string de conexão inválida");
        }
    }

    public void converterDBFtoMONGO() {
        DB database = mongoClient.getDB("pessoa");

        DBCollection collection = database.getCollection("pessoa");
        System.out.println("Coleção acessada no banco com sucesso");

        try {
            String[] colunasDBF = dbfG.carregarColunas();

            for (Object[] obj : dbfG.carregarRegistros()) {
                //objeto que recebe as informações para serem gravadas no mongo
                BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
                //pega coluna por coluna e adiciona no objeto docBuilder (criando um dicionário, que é como o mongo recebe e retorna as informações)
                for (int i = 0; i < colunasDBF.length; i++) {
                    docBuilder.append(colunasDBF[i], obj[i]);
                }
                //adiciona o registro lido (linha no DBF) no mongo
                collection.insert(docBuilder.get());

            }
            System.out.println("terminou de migrar");
        } catch (FileNotFoundException f) {
            System.err.println("Arquivo DBF não encontrado.");
            f.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DBCollection getCollection(String nomeDB, String nomeColecao) {
		DB db = mongoClient.getDB(nomeDB);
		return db.getCollection(nomeColecao);
	}

    public static void main(String[] args) {
        MongoGerenciador m = new MongoGerenciador("mongodb://localhost:27017", "arquivo.dbf");
        m.converterDBFtoMONGO();
        int quant=0;
        DBCursor allResults = m.getCollection("pessoa","pessoa").find();
        while (allResults.hasNext()) {
            allResults.next();
            quant++;
        }
        System.out.println("quantidade de registros: " + quant);
    }
}
