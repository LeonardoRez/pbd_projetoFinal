package backend;

import java.io.File;
import nl.knaw.dans.common.dbflib.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Leonardo Rezende e Thiago Silva
 */
public class DBFGerenciador {

    private Iterator<Record> table_iterator;
    private List<Record> registros;
    private List<Field> listaColunas;
    private final Table table;
    private int quantColunas;
    private String[] fieldName;
    private Object[][] row;
    private int quantRegistros;
    private int registroAtual;
    private FileInputStream path;

    public int getQuantColunas() {
        return this.quantColunas;
    }

    DBFGerenciador(String caminhoArquivo) {
        this.table = new Table(new File(caminhoArquivo));

        this.quantColunas = 0;
        this.registroAtual = 0;
    }

    public String[][] lerRegistros(int registro_inicial, int quant) {
        List<Record> lista = new ArrayList<Record>();

        try {
            lista = table.getRecordsAt(registro_inicial, quant);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lista.isEmpty()) {
            return null;
        } else {
            String[][] registros = new String[quant][this.quantColunas];
            int i = 0;
            for (Record rec : lista) {
                int j = 0;
                try {
                    for (final Field c : this.listaColunas) {
                        byte[] rawValue = rec.getRawValue(c);
                        registros[i][j++] = (rawValue == null ? "<NULL>" : new String(rawValue));
                    }

                } catch (DbfLibException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return registros;
        }
    }

    private List<Record> atualizarBuffer() {
        List<Record> lista = new ArrayList<Record>();

        try {
            lista = table.getRecordsAt(this.registroAtual, 10000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CorruptedTableException e) {
            e.printStackTrace();
        }

        return lista;

    }

    public synchronized String[] readNext() {

        if (this.registroAtual < this.getQuantRegistros()) {
            if (this.registroAtual % 10000 == 0) {
                this.registros = this.atualizarBuffer();
            }
            String data[] = new String[this.quantColunas];
            Record rec = this.registros.get(this.registroAtual % 10000);
            this.registroAtual++;
            if (rec != null) {
                int i = 0;
                for (final Field field : listaColunas) {
                    try {
                        byte[] rawValue = rec.getRawValue(field);
                        data[i++] = (rawValue == null ? "<NULL>" : new String(rawValue));
                        //System.out.println(field.getName() + ": " + data[i - 1]);
                    } catch (DbfLibException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return data;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void prepareDBF() {
        this.registroAtual = 0;
        try {

            this.table.open(IfNonExistent.ERROR);
            this.listaColunas = table.getFields();
            this.quantColunas = table.getFields().size();

            System.out.println(this.quantColunas);
            this.quantRegistros = this.table.getRecordCount();
            this.fieldName = new String[this.quantColunas];

            int i = 0;
            for (final Field field : listaColunas) {
                this.fieldName[i++] = field.getName();
            }
            this.table_iterator = table.recordIterator();

        } catch (CorruptedTableException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getFieldName() {
        return this.fieldName;
    }

    public Object[][] getRows() {

        return this.row;
    }

    public int getQuantRegistros() {
        return this.quantRegistros;
    }

    public void closeTable() {
        try {
            this.table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            DBFGerenciador d = new DBFGerenciador("arquivo_maior.dbf");
            d.prepareDBF();
            System.out.println("quant colunas: " + d.getQuantColunas());
            System.out.println("quant registros: " + d.getQuantRegistros());
            
        } catch (Exception e) {
            System.out.println("\ndeu ruim");
            e.printStackTrace();
        }
    }

}
