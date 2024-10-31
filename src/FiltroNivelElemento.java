import java.util.Set;

public class FiltroNivelElemento {

    private int nivel;

    private Set<Elemento> elementosASeremFiltrados;

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Set<Elemento> getElementosASeremFiltrados() {
        return elementosASeremFiltrados;
    }

    public void setElementosASeremFiltrados(Set<Elemento> elementosASeremFiltrados) {
        this.elementosASeremFiltrados = elementosASeremFiltrados;
    }
}
