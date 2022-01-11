package me.shreymeng.reminders.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CalendarView extends JPanel implements IRemindersView {

  private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"};

  private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

  private final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

  /**
   * The calendar header label, containing month and year information.
   */
  private final JLabel headerLabel;

  /**
   * The button to return to the previous month.
   */
  private final JButton previousButton;

  /**
   * The button to advance to the next month.
   */
  private final JButton nextButton;

  /**
   * The table model for the calendar.
   */
  private final DefaultTableModel tableModel;

  /**
   * The actual calendar component.
   */
  private final JTable table;

  /**
   * The current month being viewed.
   */
  private int month;
  /**
   * The current year being viewed.
   */
  private int year;

  public CalendarView() {
    setLayout(new BorderLayout());

    JPanel header = new JPanel(new BorderLayout());
    header.setBorder(new EmptyBorder(10, 5, 5, 5));

    this.headerLabel = new JLabel();
    headerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

    this.previousButton = new JButton("<<");
    this.nextButton = new JButton(">>");

    // Allows moving between months.
    previousButton.addActionListener(e -> {
      if (month == 0) {
        // Go back by 1 year.
        month = 11;
        year -= 1;
      } else {
        // Go back by 1 month.
        month -= 1;
      }

      refresh();
    });

    nextButton.addActionListener(e -> {
      if (month == 11) {
        // Go forwards by 1 year.
        month = 0;
        year += 1;
      } else {
        // Go forwards by 1 month.
        month += 1;
      }

      refresh();
    });

    header.add(previousButton, BorderLayout.LINE_START);
    header.add(headerLabel, BorderLayout.CENTER);
    header.add(nextButton, BorderLayout.LINE_END);

    this.tableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int rowInd, int colInd) {
        // Disable all cells from being edited.
        return false;
      }
    };

    // Create a new table with our custom model.
    this.table = new JTable(tableModel);

    for (String dayOfWeek : DAYS_OF_WEEK) {
      tableModel.addColumn(dayOfWeek);
    }

    // Set rows and columns.
    table.setRowHeight(96);
    tableModel.setColumnCount(7);
    tableModel.setRowCount(6);

    add(header, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);

    this.month = now.getMonthValue() - 1;
    this.year = now.getYear();

    // Fill the calendar.
    refresh();
  }

  @Override
  public void refresh() {

    GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    int startOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

    // Disable the "previous" button if the year is more than a year from now.
    if (month == 0 && year < now.getYear() - 1) {
      previousButton.setEnabled(false);
    }

    // Disable the "next" button if the year is more than 5 years from now.
    if (month == 11 && year >= now.getYear() + 5) {
      nextButton.setEnabled(false);
    }

    // Refresh the header label.
    headerLabel.setText(MONTHS[month] + " - " + year);

    // Clear the current table.
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 7; j++) {
        tableModel.setValueAt(null, i, j);
      }
    }

    // Add updated calendar dates.
    for (int i = 1; i <= daysInMonth; i++) {
      int row = (i + startOfMonth - 2) / 7;
      int column = (i + startOfMonth - 2) % 7;
      tableModel.setValueAt(" " + i, row, column);
    }

    // Apply the custom renderer.
    table.setDefaultRenderer(table.getColumnClass(0), new CalendarRenderer());
  }

  /**
   * The custom table cell renderer to change the display of table cells.
   */
  private static class CalendarRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean isFocused, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, column);

      setBorder(null);
      setForeground(Color.BLACK);

      if (value == null) {
        // The "day" is outside the current month.
        setBackground(Color.LIGHT_GRAY);
      } else {
        setBackground(Color.WHITE);
      }

      return this;
    }
  }
}
