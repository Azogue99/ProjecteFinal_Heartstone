package classes;

public class Carta_admin_table {
    
    private String nom;
    private int classe;
    private int mana;
    private int atac;
    private int vida;
    private int raresa;
    private int tipus;

    public Carta_admin_table() {
    }
    
    public Carta_admin_table(String nom, int classe, int mana, int atac, int vida, int raresa, int tipus) {
        this.nom = nom;
        this.classe = classe;
        this.mana = mana;
        this.atac = atac;
        this.vida = vida;
        this.raresa = raresa;
        this.tipus = tipus;
    }

    // Getter and setter
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAtac() {
        return atac;
    }

    public void setAtac(int atac) {
        this.atac = atac;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getRaresa() {
        return raresa;
    }

    public void setRaresa(int raresa) {
        this.raresa = raresa;
    }

    public int getTipus() {
        return tipus;
    }

    public void setTipus(int tipus) {
        this.tipus = tipus;
    }
    
    @Override
    public String toString() {
        return "Carta_Admin{" + "nom=" + nom + ", classe=" + classe + ", mana=" + mana + ", atac=" + atac + ", vida=" + vida + ", raresa=" + raresa + ", tipus=" + tipus + '}';
    }
    
}
