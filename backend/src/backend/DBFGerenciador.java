package backend;

import com.linuxense.javadbf.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author Leonardo Rezende e Thiago Silva
 */
public class DBFGerenciador {

    private DBFReader leitor;
    private String arquivo;

    public DBFGerenciador(String caminhoDBF) {
            this.arquivo = caminhoDBF;
    }

    public String[] carregarColunas() throws FileNotFoundException {
        String[] colunas;
        leitor = new DBFReader(new FileInputStream(arquivo));

        // get the field count if you want for some reasons like the following
        int qtdCampos = leitor.getFieldCount();

        colunas = new String[qtdCampos];
        int c = 0;
        for (int i = 0; i < qtdCampos; i++) {

            DBFField campo = leitor.getField(i);

            colunas[c++] = campo.getName();
//            System.out.println(campo.getName());

        }
//        Object[] linha = leitor.nextRecord();
//        for(Object o: linha){
//            System.out.print(o + "\t");
//        }
        return colunas;
    }

    public ArrayList<Object[]> carregarRegistros() throws FileNotFoundException {
        ArrayList<Object[]> registros = new ArrayList();
        leitor = new DBFReader(new FileInputStream(arquivo));
        Object[] r;
        while ((r = leitor.nextRecord()) != null) {
            registros.add(r);

        }

        return registros;
    }

    public static void main(String[] args) {
        DBFGerenciador d = new DBFGerenciador("arquivo.dbf");
        try {
            for (String s : d.carregarColunas()) {
                System.out.print(s + "\t");
            }
            for (Object[] o : d.carregarRegistros()) {
                System.out.print("\n");
                for (Object ob : o) {
                    System.out.print(ob + "\t");
                }
            }

        } catch (Exception e) {
            System.out.println("\ndeu ruim");
            e.printStackTrace();
        }

    }

}
