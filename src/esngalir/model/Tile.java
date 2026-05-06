package esngalir.model;

public enum Tile{
    WALL ('X'),
    PATH ('*'),
    LAVA ('L'),
    START ('Z'),
    GOAL ('O'),
    CHECKPOINT0('0'),
    CHECKPOINT1('1'),
    CHECKPOINT2('2'),
    CHECKPOINT3('3'),
    CHECKPOINT4('4'),
    CHECKPOINT5('5'),
    CHECKPOINT6('6'),
    CHECKPOINT7('7'),
    CHECKPOINT8('8'),
    CHECKPOINT9('9');

    private final char symbol;
    Tile(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol(){
        return symbol;
    }

    public int getCheckPointNumber(){
        if (!isCheckPoint()) {
            throw new IllegalStateException("Tile " + this + " bukan checkpoint!");
        }
        return Integer.parseInt(String.valueOf(symbol));
    }

    public static Tile convertFromChar(char c){
        for (Tile t : values()) {
            if (t.getSymbol() == c) {
                return t;
            }
        }

        throw new IllegalArgumentException("Tile " + c + " tidak valid!");
    }

    public boolean isCheckPoint(){
        return this == CHECKPOINT0 || this == CHECKPOINT1 || this == CHECKPOINT2 || this == CHECKPOINT3 || this == CHECKPOINT4 || this == CHECKPOINT5 || this == CHECKPOINT6 || this == CHECKPOINT7 || this == CHECKPOINT8 || this == CHECKPOINT9;
    }

    public boolean isAccess(){
        return this != WALL;
    }

}
