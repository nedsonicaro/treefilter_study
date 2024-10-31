import java.util.List;
import java.util.Objects;

public class Elemento {

    private int id;
    private Elemento elementoPai;
    private String nome;
    private List<Elemento> elementosFilhos;

    public Elemento() {
    }

    public Elemento(Elemento elementoPai,
                    String nome,
                    int id) {
        this.elementoPai = elementoPai;
        this.nome = nome;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Elemento getElementoPai() {
        return elementoPai;
    }

    public void setElementoPai(Elemento elementoPai) {
        this.elementoPai = elementoPai;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Elemento> getElementosFilhos() {
        return elementosFilhos;
    }

    public void setElementosFilhos(List<Elemento> elementosFilhos) {
        this.elementosFilhos = elementosFilhos;
    }

    public int obterNivel() {

        int nivel = 1;

        var elementoPai = this.getElementoPai();

        while (elementoPai != null) {
            nivel++;
            elementoPai = elementoPai.getElementoPai();
        }

        return nivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elemento elemento = (Elemento) o;
        return id == elemento.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
