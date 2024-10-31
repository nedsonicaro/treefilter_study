import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.*;

public class Main {

    public static void main(String[] args) {
        var arvoreDeElementos = obterArvoreDeElementosDeTresNiveis();
        informarFilhos(arvoreDeElementos);

        var filtrosDeNivelUmETres = obterFiltrosDeNivelUmETres(arvoreDeElementos);

        var arvoreFiltrada = filtrarPorNivel(filtrosDeNivelUmETres, arvoreDeElementos);

        for (var elemento : arvoreFiltrada) {
            System.out.println(elemento.getNome());
        }
    }

    private static List<Elemento> obterArvoreDeElementosDeTresNiveis() {

        var elementoRaiz1 = new Elemento(null, "01", 1);
        var elementoRaiz2 = new Elemento(null, "02", 2);

        var elementoNivel2Um = new Elemento(elementoRaiz1, "01.01", 3);
        var elementoNivel2Dois = new Elemento(elementoRaiz1, "01.02", 4);
        var elementoNivel2Tres = new Elemento(elementoRaiz2, "02.01", 5);
        var elementoNivel2Quatro = new Elemento(elementoRaiz2, "02.02", 6);

        var elementoNivel3Um = new Elemento(elementoNivel2Um, "01.01.01", 7);
        var elementoNivel3Dois = new Elemento(elementoNivel2Um, "01.01.02", 8);
        var elementoNivel3Tres = new Elemento(elementoNivel2Um, "01.01.03", 9);
        var elementoNivel3Quatro = new Elemento(elementoNivel2Dois, "01.02.01", 10);
        var elementoNivel3Cinco = new Elemento(elementoNivel2Dois, "01.02.02", 11);
        var elementoNivel3Seis = new Elemento(elementoNivel2Dois, "01.02.03", 12);
        var elementoNivel3Sete = new Elemento(elementoNivel2Tres, "02.01.01", 13);
        var elementoNivel3Oito = new Elemento(elementoNivel2Tres, "02.01.02", 14);
        var elementoNivel3Nove = new Elemento(elementoNivel2Tres, "02.01.03", 15);
        var elementoNivel3Dez = new Elemento(elementoNivel2Quatro, "02.02.01", 16);
        var elementoNivel3Onze = new Elemento(elementoNivel2Quatro, "02.02.02", 17);
        var elementoNivel3Doze = new Elemento(elementoNivel2Quatro, "02.02.03", 18);


        return new ArrayList<>(List.of(
                elementoRaiz1,
                elementoRaiz2,
                elementoNivel2Um,
                elementoNivel2Dois,
                elementoNivel2Tres,
                elementoNivel2Quatro,
                elementoNivel3Um,
                elementoNivel3Dois,
                elementoNivel3Tres,
                elementoNivel3Quatro,
                elementoNivel3Cinco,
                elementoNivel3Seis,
                elementoNivel3Sete,
                elementoNivel3Oito,
                elementoNivel3Nove,
                elementoNivel3Dez,
                elementoNivel3Onze,
                elementoNivel3Doze
        ));
    }

    private static List<FiltroNivelElemento> obterFiltrosDeNivelUmETres(List<Elemento> arvoreDeElementos) {

        var elementoRaiz1 = arvoreDeElementos
                .stream()
                .filter(elemento -> elemento.getId() == 1)
                .collect(Collectors.toSet());

        var filtroNivel1 = new FiltroNivelElemento();
        filtroNivel1.setNivel(1);
        filtroNivel1.setElementosASeremFiltrados(elementoRaiz1);

        var elementosNivel3 = arvoreDeElementos
                .stream()
                .filter(elemento -> elemento.obterNivel() == 3)
                .collect(Collectors.toSet());

        var filtroNivel3 = new FiltroNivelElemento();
        filtroNivel3.setNivel(3);
        filtroNivel3.setElementosASeremFiltrados(elementosNivel3);

        return new ArrayList<>(List.of(filtroNivel1, filtroNivel3));
    }

    private static void informarFilhos(List<Elemento> arvoreDeElementos) {

        Map<Integer, List<Elemento>> elementosPorPai = new HashMap<>();

        for (var elemento : arvoreDeElementos) {
            if (isNull(elemento.getElementoPai())) continue;

            var elementoPai = elemento.getElementoPai();

            elementosPorPai.putIfAbsent(elementoPai.getId(), new ArrayList<>());
            elementosPorPai.get(elementoPai.getId()).add(elemento);
        }

        arvoreDeElementos.forEach(elemento -> {
            var filhos = elementosPorPai.getOrDefault(elemento.getId(), new ArrayList<>());
            elemento.setElementosFilhos(filhos);
        });
    }

    private static List<Elemento> filtrarPorNivel(List<FiltroNivelElemento> filtrosDeNivel,
                                                  List<Elemento> arvoreDeElementos) {

        Set<Elemento> arvoreFiltradaPorNivel = new HashSet<>();

        var filtrosPorNivel = filtrosDeNivel
                .stream()
                .collect(Collectors.groupingBy(FiltroNivelElemento::getNivel));

        var elementosPorNivel = arvoreDeElementos
                .stream()
                .collect(Collectors.groupingBy(Elemento::obterNivel));

        var filtroDeNivelMaisProfundo = filtrosDeNivel
                .stream()
                .sorted(Comparator.comparing(FiltroNivelElemento::getNivel).reversed())
                .toList()
                .get(0);

        var elementosDeUltimoNivelFiltrados = elementosPorNivel.get(filtroDeNivelMaisProfundo.getNivel())
                .stream()
                .filter(elemento -> filtroDeNivelMaisProfundo.getElementosASeremFiltrados().contains(elemento))
                .toList();

        var elementosDeUltimoNivelFiltradosComBaseNosFiltrosAnteriores = new LinkedList<Elemento>();

        for (var elementoDeUltimoNivel : elementosDeUltimoNivelFiltrados) {

            boolean deveSerAdicionado = true;

            var elementoPai = elementoDeUltimoNivel.getElementoPai();

            while (nonNull(elementoPai)) {

                var nivelDoElementoPai = elementoPai.obterNivel();

                if (filtrosPorNivel.containsKey(nivelDoElementoPai)) {
                    var filtroDoNivel = filtrosPorNivel.get(nivelDoElementoPai);

                    Elemento finalElementoPai = elementoPai;

                    if (filtroDoNivel.stream().noneMatch(filtro -> filtro.getElementosASeremFiltrados().contains(finalElementoPai))) {
                        deveSerAdicionado = false;
                        break;
                    }
                }

                elementoPai = elementoPai.getElementoPai();
            }

            if (deveSerAdicionado) elementosDeUltimoNivelFiltradosComBaseNosFiltrosAnteriores.add(elementoDeUltimoNivel);
        }

        for (var elementoDeUltimoNivelFiltradosComBaseNosFiltrosAnterios : elementosDeUltimoNivelFiltradosComBaseNosFiltrosAnteriores) {

            arvoreFiltradaPorNivel.add(elementoDeUltimoNivelFiltradosComBaseNosFiltrosAnterios);

            var elementoPai = elementoDeUltimoNivelFiltradosComBaseNosFiltrosAnterios.getElementoPai();

            while (nonNull(elementoPai)) {
                arvoreFiltradaPorNivel.add(elementoPai);
                elementoPai = elementoPai.getElementoPai();
            }
        }

        return arvoreFiltradaPorNivel
                .stream()
                .sorted(Comparator.comparing(Elemento::getNome))
                .toList();
    }
}