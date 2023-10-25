import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * This view works as a main view when an admin logs into the system, showing all products in the system.
 */
public class AdminProductListView extends View {

    private static final long serialVersionUID = 1L;

    private JPanel scrollPanel;
    public static DefaultTableModel tableModel;
    public static JTable productTable;
    private static final String[] TABLE_COLUMNS = { "Type", "Name", "Price", "Quantity" };

    // Type content
    public static final ProductType[] TYPE = { ProductType.GAME, ProductType.MUSIC, ProductType.MOVIE, ProductType.TV };

    private JComboBox<ProductType> productType; // Parameterize JComboBox with ProductType

    public AdminProductListView() {
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        add(panel, BorderLayout.NORTH);

        JButton createProductButton = new JButton("Create A");
        panel.add(createProductButton);
        this.productType = new JComboBox<ProductType>(); // Parameterize JComboBox with ProductType
        panel.add(productType);
        createProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProductType selectedProductType = (ProductType) getProductType().getSelectedItem();
                switch (selectedProductType) {
                    case GAME:
                        AdminProductDetails.displayGameDetails(tableModel, getController(), ProductType.GAME);
                        break;
                    case MUSIC:
                        AdminProductDetails.displayMusicDetails(tableModel, getController(), ProductType.MUSIC);
                        break;
                    case MOVIE:
                        AdminProductDetails.displayMovieDetails(tableModel, getController(), ProductType.MOVIE);
                        break;
                    case TV:
                        AdminProductDetails.displayTVDetails(tableModel, getController(), ProductType.TV);
                        break;
                    default:
                        break;
                }
            }
        });

        JButton cartButton = new JButton("Generate report");

        cartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().showAdminReportView();
            }
        });

        panel.add(cartButton);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Do you want to logout and save all changes?", "Confirm", 2);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    getController().storeProductInfoByAdmin();
                    getController().getWindow().dispose();
                    getController().init();
                }
            }
        });
        panel.add(logoutBtn);

        AdminProductListView.productTable = new JTable(new DefaultTableModel(TABLE_COLUMNS, 0));
        tableModel = (DefaultTableModel) productTable.getModel();

        AdminProductListView.productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel propertyTableColumnModel = AdminProductListView.productTable.getColumnModel();
        propertyTableColumnModel.getColumn(0).setPreferredWidth(200);
        propertyTableColumnModel.getColumn(1).setPreferredWidth(300);
        propertyTableColumnModel.getColumn(2).setPreferredWidth(200);
        propertyTableColumnModel.getColumn(3).setPreferredWidth(200);

        scrollPanel = new JPanel();
        JScrollPane scroll = new JScrollPane(productTable);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.CENTER);
    }

    protected JComboBox<ProductType> getProductType() {
        return productType;
    }

    public void initialize() {
        scrollPanel.removeAll();
        List<Product> list = getController().getBackend().getProducts();
        this.clearProductTable();
        for (Product p : list) {
            ((DefaultTableModel) AdminProductListView.productTable.getModel())
                    .addRow(new Object[] { p.getType(), p.getName(), p.getPrice(), p.getQuantity() });
        }
        revalidate();
    }

    public void clearProductTable() {
        int numberOfRow = AdminProductListView.productTable.getModel().getRowCount();
        if (numberOfRow > 0) {
            DefaultTableModel tableModel = (DefaultTableModel) AdminProductListView.productTable.getModel();
            for (int index = (numberOfRow - 1); index >= 0; index--) {
                tableModel.removeRow(index);
            }
        }
    }
}
