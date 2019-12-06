package takuzu.jeu.graphique;
import javax.swing.table.DefaultTableModel;

// cette classe permet de fixer les valeurs préalablement remplies et les rendre non éditables
public class MyDefaultTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5176142061455511644L;
	private boolean[][] editable_cells; // la matrice

    private MyDefaultTableModel(int rows, int cols) { // constructeur
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
    }

	public MyDefaultTableModel(Object[][] objects, String[] strings) {
		super(objects, strings);
		this.editable_cells = new boolean[objects.length][strings.length];
		for(int i = 0 ; i< objects.length; i++ )
			for(int j = 0 ; j< objects.length; j++ ) {
				this.editable_cells[i][j] = true; // rendre la case de coordonnées i, j éditable
			}
	}

	@Override
	//getter
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return this.editable_cells[row][column];
    }

	//setter
    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        this.fireTableCellUpdated(row, col);
    }
}