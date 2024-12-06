package cvbuilder.view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private final JTable table;
    private final Action action;
    private final JButton renderButton;
    private final JButton editButton;

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;
        this.renderButton = new JButton();
        this.editButton = new JButton();
        this.editButton.setFocusPainted(false);
        this.editButton.addActionListener((ActionEvent e) -> {
            int row = ButtonColumn.this.table.convertRowIndexToModel(ButtonColumn.this.table.getEditingRow());
            fireEditingStopped();
            ActionEvent event = new ActionEvent(ButtonColumn.this.table, ActionEvent.ACTION_PERFORMED, "" + row);
            ButtonColumn.this.action.actionPerformed(event);
        });

        this.table.getColumnModel().getColumn(column).setCellRenderer(this);
        this.table.getColumnModel().getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }
        renderButton.setText((value == null) ? "" : value.toString());
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editButton.setText((value == null) ? "" : value.toString());
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editButton.getText();
    }
}
