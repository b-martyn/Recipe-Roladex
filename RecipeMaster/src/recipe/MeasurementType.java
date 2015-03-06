package recipe;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MeasurementType implements Serializable, Comparable<MeasurementType>{
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public MeasurementType(){
		
	}
	
	public MeasurementType(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ListCellRenderer<MeasurementType> getListCellRenderer(){
		return new MeasurementTypeCellRenderer(new DefaultListCellRenderer());
	}

	@Override
	public int compareTo(MeasurementType compare) {
		return name.compareTo(((MeasurementType) compare).name);
	}

	private static class MeasurementTypeCellRenderer extends JLabel implements ListCellRenderer<MeasurementType>{
		private static final long serialVersionUID = 1L;
		
		private final DefaultListCellRenderer defaultRenderer;
		
		public MeasurementTypeCellRenderer(DefaultListCellRenderer renderer){
			this.defaultRenderer = renderer;
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends MeasurementType> list, MeasurementType value,	int index, boolean isSelected, boolean cellHasFocus) {
			Component component = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			((JLabel)component).setText(value.name);
			
			return component;
		}
		
	}
}
