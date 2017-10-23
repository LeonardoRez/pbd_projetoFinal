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
    private String colecao;
    DBFGerenciador dbfG;

    public MongoGerenciador(String conexao, String arquivoDBF, String colecao) {
        this.conexaoString = new MongoClientURI(conexao);
        this.colecao = colecao;

        dbfG = new DBFGerenciador(arquivoDBF);
        try {
            mongoClient = new MongoClient(conexaoString);

        } catch (UnknownHostException e) {
            System.out.println("string de conexão inválida");
        }
    }

    public void converterDBFtoMONGO() {
        DB database = mongoClient.getDB(colecao);

        DBCollection collection = database.getCollection(colecao);
        System.out.println("Coleção acessada no banco com sucesso");

        try {
            dbfG.prepareDBF();
            String[] colunasDBF = dbfG.getFieldName();

            for (int i=0;i< dbfG.getQuantRegistros();i++) {
                //objeto que recebe as informações para serem gravadas no mongo
                BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
                //pega coluna por coluna e adiciona no objeto docBuilder (criando um dicionário, que é como o mongo recebe e retorna as informações)
                String reg[] = dbfG.readNext();
                for (int j = 0; j < colunasDBF.length; j++) {
                    docBuilder.append(colunasDBF[j], reg[j]);
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
        MongoGerenciador m = new MongoGerenciador("mongodb://localhost:27017", "arquivo_maior.dbf", "pessoa");
        m.converterDBFtoMONGO();
//        int quant=0;
//        DBCursor allResults = m.getCollection("pessoa","pessoa").find();
//        while (allResults.hasNext()) {
//            allResults.next();
//            quant++;
//        }
//        System.out.println("quantidade de registros: " + quant);

    }
}
