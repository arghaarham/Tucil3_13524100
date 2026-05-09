# Ice Sliding Puzzle Solver
## Tugas Kecil 3 IF2211 Strategi Algoritma
![Cover](Cover.png)
### Deskripsi
Program untuk menyelesaikan permainan Ice Sliding Puzzle menggunakan algoritma pathfinding.
Karakter bergerak di atas permukaan es yang licin, di mana sekali bergerak tidak berhenti sampai
menabrak dinding. Program mencari jalur optimal dari titik awal (Z) ke titik tujuan (O)
sambil melewati semua checkpoint secara berurutan.

**Algoritma yang diimplementasikan:**
- UCS (Uniform Cost Search) — wajib
- GBFS (Greedy Best First Search) — wajib
- A* (A-Star) — wajib
- BFS (Breadth First Search) — bonus
- DFS (Depth First Search) — bonus

**Heuristik yang diimplementasikan:**
- H1: Manhattan Distance — utama
- H2: Checkpoint Aware — bonus
- H3: Euclidean Distance — bonus

### Requirement
- Java 17+
- Maven 3.6+ (JavaFX diunduh otomatis oleh Maven)

### Struktur Repository
```
Tucil3_13524100/
├── src/          -> source code Java
├── bin/          -> hasil kompilasi (.class) kalau manual. kalau dengan maven akan tersimpan di target/
├── test/         -> test case input (.txt)
├── doc/          -> laporan PDF
└── README.md
```

### Cara Kompilasi
```bash
mvn compile
```

### Cara Menjalankan
```bash
# Mode CLI
mvn exec:java

# Mode GUI
mvn javafx:run
```

### Format File Input
```
N M
[N baris grid]
[N baris cost]
```

Contoh:
```
7 7
XXXXXXX
X0****X
X**X**X
X****OX
X1***LX
XZ**X*X
XXXXXXX
999 999 999 999 999 999 999
999 3   5   2   8   1   999
999 7   4   999 6   9   999
999 2   8   3   5   4   999
999 6   1   7   2   999 999
999 9   3   4   999 8   999
999 999 999 999 999 999 999
```

**Keterangan tile:**
| Simbol | Keterangan |
|--------|------------|
| `*` | Path normal |
| `X` | Dinding — karakter berhenti sebelumnya |
| `L` | Lava — game over jika dilewati |
| `Z` | Posisi awal karakter |
| `O` | Titik tujuan |
| `0`-`9` | Checkpoint — harus dilewati berurutan |

### Author
13524100 - Arghawisesa Dwinanda Arham