import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class NombitsFrame extends JFrame {

    private static final Color BG_WINDOW = new Color(250, 250, 250);
    private static final Color BG_OUTER = new Color(216, 218, 221);
    private static final Color BG_CARD = new Color(238, 239, 241);
    private static final Color BG_HOVER = new Color(224, 226, 229);
    private static final Color BG_IMG = new Color(112, 115, 120);
    private static final Color TEXT_PRIMARY = new Color(28, 28, 30);
    private static final Color TEXT_MUTED = new Color(92, 96, 102);
    private static final Color BORDER_SUBTLE = new Color(186, 190, 196);

    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_SECTION = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);
    private final JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

    private JTextField usernameField, searchMenuField, searchOrderIdField;
    private JPasswordField passwordField;
    private JButton loginButton, logoutBtn, sortPriceBtn, addBurgerBtn, addIcedTeaBtn, addFriesBtn;
    private JButton undoLastChangeBtn, placeOrderBtn, processPaymentBtn, startPreparingBtn, markAsReadyBtn;
    private JTextArea currentOrderArea, nextUpItemsArea, preparingItemsArea;
    private JLabel totalAmountLabel, selectedOrderLabel, nextUpOrderLabel, nextUpStatusLabel, preparingOrderLabel,
            preparingStatusLabel;
    private JTable incomingOrdersTable;
    private DefaultTableModel ordersTableModel;

    public NombitsFrame() {
        setTitle("Nombits");
        setSize(1060, 630);
        setMinimumSize(new Dimension(920, 560));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(BG_WINDOW);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        navBar.setBackground(BG_WINDOW);
        addNav("1. Menu & Order", "MENU");
        addNav("2. Cashier Dashboard", "CASHIER");
        addNav("3. Kitchen Display", "KITCHEN");

        cardPanel.add(loginView(), "LOGIN");
        cardPanel.add(menuView(), "MENU");
        cardPanel.add(cashierView(), "CASHIER");
        cardPanel.add(kitchenView(), "KITCHEN");

        root.add(navBar, BorderLayout.NORTH);
        root.add(cardPanel, BorderLayout.CENTER);
        add(root);

        showScreen("LOGIN");
    }

    private void addNav(String label, String screen) {
        JButton b = boxBtn(label);
        b.addActionListener(e -> showScreen(screen));
        navBar.add(b);
    }

    public void showScreen(String name) {
        navBar.setVisible(!"LOGIN".equals(name));
        cards.show(cardPanel, name);
    }

    private JPanel loginView() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_OUTER);

        JPanel box = cardPanelBox(new BorderLayout(0, 14));
        box.setPreferredSize(new Dimension(340, 250));
        box.setBorder(new CompoundBorder(
                new LineBorder(BORDER_SUBTLE, 1),
                new EmptyBorder(20, 22, 20, 22)));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setOpaque(false);
        header.add(logoLabel("N", 28));
        header.add(lbl("Nombits Login", FONT_TITLE, TEXT_PRIMARY));

        JPanel fields = new JPanel(new GridLayout(4, 1, 0, 6));
        fields.setOpaque(false);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        styleUnderline(usernameField);
        styleUnderline(passwordField);

        fields.add(lbl("Username", FONT_SMALL, TEXT_MUTED));
        fields.add(usernameField);
        fields.add(lbl("Password", FONT_SMALL, TEXT_MUTED));
        fields.add(passwordField);

        loginButton = boxBtn("Login");
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.addActionListener(e -> showScreen("MENU"));

        box.add(header, BorderLayout.NORTH);
        box.add(fields, BorderLayout.CENTER);
        box.add(loginButton, BorderLayout.SOUTH);

        outer.add(box);
        return outer;
    }

    private JPanel menuView() {
        JPanel outer = outerBox();

        JPanel top = cardPanelBox(new BorderLayout());
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brand.setOpaque(false);
        brand.add(logoLabel("N", 24));
        brand.add(lbl("Nombits", FONT_TITLE, TEXT_PRIMARY));

        searchMenuField = new JTextField("Search Menu", 15);
        styleUnderline(searchMenuField);
        sortPriceBtn = flatBtn("Sort Price");

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        right.setOpaque(false);
        right.add(searchMenuField);
        right.add(sortPriceBtn);

        top.add(brand, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        JPanel left = cardPanelBox(new BorderLayout(0, 12));
        left.add(lbl("Menu Items", FONT_SECTION, TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        addBurgerBtn = flatBtn("+ Add");
        addIcedTeaBtn = flatBtn("+ Add");
        addFriesBtn = flatBtn("+ Add");

        grid.add(itemCard("Burger", "$10.00", addBurgerBtn));
        grid.add(itemCard("Iced Tea", "$5.00", addIcedTeaBtn));
        grid.add(itemCard("Fries", "$3.00", addFriesBtn));
        grid.add(new JLabel());
        left.add(grid, BorderLayout.CENTER);

        JPanel rightOrder = cardPanelBox(new BorderLayout(0, 12));
        rightOrder.add(lbl("Current Order", FONT_SECTION, TEXT_PRIMARY), BorderLayout.NORTH);

        currentOrderArea = textArea("1. Burger          $ 10.00\n2. Chicken         $ 15.00");
        totalAmountLabel = lbl("$25.00", FONT_BODY, TEXT_PRIMARY);

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.add(lbl("Total", FONT_BODY, TEXT_PRIMARY), BorderLayout.WEST);
        totalRow.add(totalAmountLabel, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_SUBTLE);

        JPanel mid = new JPanel(new BorderLayout(0, 8));
        mid.setOpaque(false);
        mid.add(currentOrderArea, BorderLayout.NORTH);
        mid.add(sep, BorderLayout.CENTER);
        mid.add(totalRow, BorderLayout.SOUTH);

        undoLastChangeBtn = flatBtn("Undo Last Change (STACK)");
        placeOrderBtn = boxBtn("Place Order");
        placeOrderBtn.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(undoLastChangeBtn, BorderLayout.NORTH);
        bottom.add(placeOrderBtn, BorderLayout.SOUTH);

        rightOrder.add(mid, BorderLayout.CENTER);
        rightOrder.add(bottom, BorderLayout.SOUTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 16, 0));
        body.setOpaque(false);
        body.add(left);
        body.add(rightOrder);

        outer.add(top, BorderLayout.NORTH);
        outer.add(body, BorderLayout.CENTER);
        return outer;
    }

    private JPanel itemCard(String name, String price, JButton btn) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);

        JPanel img = new JPanel(new GridBagLayout());
        img.setBackground(BG_IMG);
        img.setPreferredSize(new Dimension(78, 82));
        JLabel l = lbl("Image", FONT_SMALL, Color.WHITE);
        img.add(l);

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(4, 0, 0, 0));
        info.add(lbl(name, FONT_BODY, TEXT_PRIMARY));
        info.add(lbl(price, FONT_SMALL, TEXT_MUTED));
        info.add(btn);

        p.add(img, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JPanel cashierView() {
        JPanel outer = outerBox();

        JPanel top = cardPanelBox(new BorderLayout());
        searchOrderIdField = new JTextField("Search Order ID (Linear Search)", 20);
        styleUnderline(searchOrderIdField);
        logoutBtn = flatBtn("Log Out");
        logoutBtn.addActionListener(e -> showScreen("LOGIN"));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        right.setOpaque(false);
        right.add(searchOrderIdField);
        right.add(logoutBtn);

        top.add(lbl("Cashier Dashboard", FONT_TITLE, TEXT_PRIMARY), BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        JPanel card = cardPanelBox(new BorderLayout(0, 12));
        card.add(lbl("Incoming Orders (Queue/FIFO)", FONT_SECTION, TEXT_PRIMARY), BorderLayout.NORTH);

        String[] cols = { "Order ID", "Customer name", "Items", "Total", "Status" };
        Object[][] rows = { { "#101", "Raymark Pandita", "Burger, Fries", "$15.00", "Pending" } };
        ordersTableModel = new DefaultTableModel(rows, cols) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        incomingOrdersTable = new JTable(ordersTableModel);
        incomingOrdersTable.setRowHeight(30);
        incomingOrdersTable.setBackground(BG_CARD);
        incomingOrdersTable.setForeground(TEXT_PRIMARY);
        incomingOrdersTable.setFont(FONT_BODY);
        incomingOrdersTable.setShowGrid(false);
        incomingOrdersTable.getTableHeader().setFont(FONT_SMALL);
        incomingOrdersTable.getTableHeader().setBackground(BG_CARD);
        incomingOrdersTable.getTableHeader().setForeground(TEXT_MUTED);

        JScrollPane sp = new JScrollPane(incomingOrdersTable);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_CARD);
        card.add(sp, BorderLayout.CENTER);

        selectedOrderLabel = lbl("Selected: #104", FONT_BODY, TEXT_PRIMARY);
        processPaymentBtn = boxBtn("Action:  Process Payment & Send to Kitchen Queue");

        JPanel foot = new JPanel(new GridLayout(2, 1, 0, 8));
        foot.setOpaque(false);
        foot.add(selectedOrderLabel);
        foot.add(processPaymentBtn);
        card.add(foot, BorderLayout.SOUTH);

        outer.add(top, BorderLayout.NORTH);
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private JPanel kitchenView() {
        JPanel outer = outerBox();

        JPanel top = cardPanelBox(new BorderLayout());
        top.add(lbl("Kitchen Staff Display", FONT_TITLE, TEXT_PRIMARY), BorderLayout.WEST);
        top.add(lbl("Queue Mode: FIFO (FIRST IN)", FONT_SECTION, TEXT_PRIMARY), BorderLayout.EAST);

        nextUpOrderLabel = lbl("Order #104", FONT_BODY, TEXT_PRIMARY);
        nextUpItemsArea = textArea("    -2x Burger\n    -1x Fries");
        nextUpStatusLabel = lbl("Status: Waiting", FONT_BODY, TEXT_MUTED);
        startPreparingBtn = boxBtn("[START PREPARING]");

        preparingOrderLabel = lbl("Order #103", FONT_BODY, TEXT_PRIMARY);
        preparingItemsArea = textArea("    -1X Iced Tea");
        preparingStatusLabel = lbl("Status: Preparing in Progress", FONT_BODY, TEXT_MUTED);
        markAsReadyBtn = boxBtn("[MARK AS READY]");

        JPanel cols = new JPanel(new GridLayout(1, 2, 18, 0));
        cols.setOpaque(false);
        cols.add(kitchenCard("NEXT UP", nextUpOrderLabel, nextUpItemsArea, nextUpStatusLabel, startPreparingBtn));
        cols.add(kitchenCard("PREPARING", preparingOrderLabel, preparingItemsArea, preparingStatusLabel,
                markAsReadyBtn));

        outer.add(top, BorderLayout.NORTH);
        outer.add(cols, BorderLayout.CENTER);
        return outer;
    }

    private JPanel kitchenCard(String head, JLabel ord, JTextArea items, JLabel st, JButton act) {
        JPanel p = cardPanelBox(new BorderLayout(0, 12));

        JPanel top = new JPanel(new GridLayout(3, 1, 0, 6));
        top.setOpaque(false);
        top.add(lbl(head, FONT_SECTION, TEXT_PRIMARY));
        top.add(ord);
        top.add(items);

        JPanel bot = new JPanel(new GridLayout(2, 1, 0, 10));
        bot.setOpaque(false);
        bot.add(st);
        bot.add(act);

        p.add(top, BorderLayout.NORTH);
        p.add(bot, BorderLayout.CENTER);
        return p;
    }

    private JPanel outerBox() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(BG_OUTER);
        p.setBorder(new EmptyBorder(14, 14, 14, 14));
        return p;
    }

    private JPanel cardPanelBox(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(14, 16, 14, 16));
        return p;
    }

    private JLabel logoLabel(String fallbackText, int size) {
        URL url = getClass().getResource("/image.png");
        Image raw = null;

        if (url != null) {
            raw = new ImageIcon(url).getImage();
        } else {
            java.io.File localFile = new java.io.File("src/resources/image.png");
            if (localFile.exists()) {
                raw = new ImageIcon(localFile.getPath()).getImage();
            }
        }

        if (raw != null) {
            Image scaled = raw.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            JLabel iconLbl = new JLabel(new ImageIcon(scaled));
            iconLbl.setBorder(new LineBorder(BORDER_SUBTLE, 1));
            return iconLbl;
        }
        return lbl(fallbackText, FONT_TITLE, TEXT_PRIMARY);
    }

    private void styleUnderline(JTextField field) {
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_CARD);
        field.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_SUBTLE),
                new EmptyBorder(4, 4, 4, 4)));
    }

    private JLabel lbl(String txt, Font font, Color color) {
        JLabel l = new JLabel(txt);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private JTextArea textArea(String txt) {
        JTextArea ta = new JTextArea(txt);
        ta.setFont(FONT_BODY);
        ta.setOpaque(false);
        ta.setEditable(false);
        ta.setForeground(TEXT_PRIMARY);
        return ta;
    }

    private JButton flatBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFont(FONT_BODY);
        b.setForeground(TEXT_PRIMARY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setForeground(TEXT_MUTED);
            }

            public void mouseExited(MouseEvent e) {
                b.setForeground(TEXT_PRIMARY);
            }
        });
        return b;
    }

    private JButton boxBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFont(FONT_SMALL);
        b.setForeground(TEXT_PRIMARY);
        b.setBackground(BG_WINDOW);
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(new LineBorder(BORDER_SUBTLE, 1), new EmptyBorder(6, 12, 6, 12)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(BG_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(BG_WINDOW);
            }
        });
        return b;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JTextField getSearchMenuField() {
        return searchMenuField;
    }

    public JButton getSortPriceBtn() {
        return sortPriceBtn;
    }

    public JButton getAddBurgerBtn() {
        return addBurgerBtn;
    }

    public JButton getAddIcedTeaBtn() {
        return addIcedTeaBtn;
    }

    public JButton getAddFriesBtn() {
        return addFriesBtn;
    }

    public JTextArea getCurrentOrderArea() {
        return currentOrderArea;
    }

    public JLabel getTotalAmountLabel() {
        return totalAmountLabel;
    }

    public JButton getUndoLastChangeBtn() {
        return undoLastChangeBtn;
    }

    public JButton getPlaceOrderBtn() {
        return placeOrderBtn;
    }

    public JTextField getSearchOrderIdField() {
        return searchOrderIdField;
    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public JTable getIncomingOrdersTable() {
        return incomingOrdersTable;
    }

    public DefaultTableModel getOrdersTableModel() {
        return ordersTableModel;
    }

    public JLabel getSelectedOrderLabel() {
        return selectedOrderLabel;
    }

    public JButton getProcessPaymentBtn() {
        return processPaymentBtn;
    }

    public JLabel getNextUpOrderLabel() {
        return nextUpOrderLabel;
    }

    public JTextArea getNextUpItemsArea() {
        return nextUpItemsArea;
    }

    public JLabel getNextUpStatusLabel() {
        return nextUpStatusLabel;
    }

    public JButton getStartPreparingBtn() {
        return startPreparingBtn;
    }

    public JLabel getPreparingOrderLabel() {
        return preparingOrderLabel;
    }

    public JTextArea getPreparingItemsArea() {
        return preparingItemsArea;
    }

    public JLabel getPreparingStatusLabel() {
        return preparingStatusLabel;
    }

    public JButton getMarkAsReadyBtn() {
        return markAsReadyBtn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NombitsFrame().setVisible(true));
    }
}