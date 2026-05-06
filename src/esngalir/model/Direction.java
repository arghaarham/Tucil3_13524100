package esngalir.model;


public enum Direction{ //simbol, row, col
    UP ('U', -1, 0),
    DOWN ('D', 1, 0),
    LEFT ('L', 0, -1),
    RIGHT ('R', 0, 1);

    private final char symbol; //biar gabisa diubah
    private final int deltaRow;
    private final int deltaCol;

    Direction(char symbol, int deltaRow, int deltaCol){
        this.symbol = symbol;
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }
    
    public char getSymbol(){
        return symbol;
    }

    public int getDeltaRow(){
        return deltaRow;
    }

    public int getDeltaCol(){
        return deltaCol;
    }

    public static Direction convertFromChar(char c){
        for (Direction d : values()) { //loop semua isi enum Direction
            if (d.getSymbol() == c) {
                return d;
            }
        }
        throw new IllegalArgumentException("Direction " + c + " tidak valid!");
    }
}
