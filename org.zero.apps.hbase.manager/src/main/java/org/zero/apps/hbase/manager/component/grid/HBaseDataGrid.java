package org.zero.apps.hbase.manager.component.grid;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.BoardPane;
import org.zero.apps.hbase.manager.support.event.SimpleMouseClickEvent;

@Component
@Scope(value="prototype")
public class HBaseDataGrid extends JPanel {
	
	private JTable dataGrid;
	
	public HBaseDataGrid() {
		System.out.println("Init HBaseDataGrid");
		setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		dataGrid = new JTable();
		dataGrid.setFillsViewportHeight(true);
		scrollPane.setViewportView(dataGrid);
		
		dataGrid.addMouseListener(new SimpleMouseClickEvent() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					int selectedColumn = dataGrid.getSelectedColumn();
					int selectedRow = dataGrid.getSelectedRow();
					Object valueAt = dataGrid.getModel().getValueAt(selectedRow, selectedColumn);
					BoardPane board = new BoardPane();
					board.setText(valueAt.toString());
					JOptionPane.showMessageDialog(dataGrid,board);
				}
			}
			
		});
	}

	public void setDataModel(TableModel hBaseDataGridModel) {
		dataGrid.setModel(hBaseDataGridModel);
	}
}
