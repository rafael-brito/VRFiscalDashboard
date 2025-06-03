package br.com.vrsoftware.enums;

public enum Meses {

    JANEIRO(0, "Janeiro"),
    FEVEREIRO(1, "Fevereiro"),
    MARCO(2, "Março"),
    ABRIL(3, "Abril"),
    MAIO(4, "Maio"),
    JUNHO(5, "Junho"),
    JULHO(6, "Julho"),
    AGOSTO(7, "Agosto"),
    SETEMBRO(8, "Setembro"),
    OUTUBRO(9, "Outubro"),
    NOVEMBRO(10, "Novembro"),
    DEZEMBRO(11, "Dezembro");

    private final int indice;
    private final String nome;

    Meses(int indice, String nome) {
        this.indice = indice;
        this.nome = nome;
    }

    public int getIndice() {
        return indice;
    }

    public String getNome() {
        return nome;
    }

    public static Meses getFromIndice(int indice) {
        for (Meses mes : values()) {
            if (mes.getIndice() == indice) {
                return mes;
            }
        }

        throw new IllegalArgumentException("Índice inválido: " + indice);
    }

    public static Meses getFromNome(String nome) {
        for (Meses mes : values()) {
            if (mes.getNome().equalsIgnoreCase(nome)) {
                return mes;
            }
        }

        throw new IllegalArgumentException("Nome inválido: " + nome);
    }

    public boolean matchesByIndice(int indice) {
        return this.indice == indice;
    }

    public boolean matchesByNome(String nome) {
        return this.nome.equalsIgnoreCase(nome);
    }
}
