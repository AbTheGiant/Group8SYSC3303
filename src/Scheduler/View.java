package Scheduler;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import net.miginfocom.swing.MigLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class View extends JFrame {
	
	private static JLabel floorElevator1;
	private static JLabel floorElevator2;
	private static JLabel floorElevator3;
	private static JLabel floorElevator4;

	private static JLabel hfaultElevator1;
	private static JLabel hfaultElevator2;
	private static JLabel hfaultElevator3;
	private static JLabel hfaultElevator4;

	private static JLabel sfaultElevator1;
	private static JLabel sfaultElevator2;
	private static JLabel sfaultElevator3;
	private static JLabel sfaultElevator4;
	
	private JPanel panel_3;
	private JLabel lblElevator_2;

	/**
	 * Create the frame.
	 */
	public View() {
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 10));
		setTitle("Elevator Display");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{97, 79, 81, 73, 0};
		gridBagLayout.rowHeights = new int[]{26, 19, 35, 85, 40, 14, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panel.setBorder(null);
		getContentPane().add(panel, gbc_panel);
		
		JLabel lblNewLabel = new JLabel("Elevator 1");
		lblNewLabel.setFont(new Font("Stencil", Font.PLAIN, 15));
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel_1.setBorder(null);
		getContentPane().add(panel_1, gbc_panel_1);
		
		JLabel lblElevator = new JLabel("Elevator 2");
		lblElevator.setFont(new Font("Stencil", Font.PLAIN, 15));
		panel_1.add(lblElevator);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.gridx = 2;
		gbc_panel_2.gridy = 0;
		getContentPane().add(panel_2, gbc_panel_2);
		
		JLabel lblElevator_1 = new JLabel("Elevator 3");
		lblElevator_1.setBorder(null);
		lblElevator_1.setFont(new Font("Stencil", Font.PLAIN, 15));
		panel_2.add(lblElevator_1);
		
		panel_3 = new JPanel();
		panel_3.setBorder(null);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 3;
		gbc_panel_3.gridy = 0;
		getContentPane().add(panel_3, gbc_panel_3);
		
		lblElevator_2 = new JLabel("Elevator 4");
		lblElevator_2.setFont(new Font("Stencil", Font.PLAIN, 15));
		panel_3.add(lblElevator_2);
		
		floorElevator1 = new JLabel("Current Floor:");
		floorElevator1.setBorder(new LineBorder(new Color(0, 0, 0)));
		floorElevator1.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		GridBagConstraints gbc_floorElevator1 = new GridBagConstraints();
		gbc_floorElevator1.anchor = GridBagConstraints.WEST;
		gbc_floorElevator1.insets = new Insets(0, 0, 5, 5);
		gbc_floorElevator1.gridx = 0;
		gbc_floorElevator1.gridy = 1;
		getContentPane().add(floorElevator1, gbc_floorElevator1);
		
		floorElevator2 = new JLabel("Current Floor: ");
		floorElevator2.setBorder(new LineBorder(new Color(0, 0, 0)));
		floorElevator2.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		GridBagConstraints gbc_floorElevator2 = new GridBagConstraints();
		gbc_floorElevator2.anchor = GridBagConstraints.WEST;
		gbc_floorElevator2.insets = new Insets(0, 0, 5, 5);
		gbc_floorElevator2.gridx = 1;
		gbc_floorElevator2.gridy = 1;
		getContentPane().add(floorElevator2, gbc_floorElevator2);
		
		floorElevator3 = new JLabel("Current Floor: ");
		floorElevator3.setBorder(new LineBorder(new Color(0, 0, 0)));
		floorElevator3.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		GridBagConstraints gbc_floorElevator3 = new GridBagConstraints();
		gbc_floorElevator3.anchor = GridBagConstraints.WEST;
		gbc_floorElevator3.insets = new Insets(0, 0, 5, 5);
		gbc_floorElevator3.gridx = 2;
		gbc_floorElevator3.gridy = 1;
		getContentPane().add(floorElevator3, gbc_floorElevator3);
		
		floorElevator4 = new JLabel("Current Floor: ");
		floorElevator4.setBorder(new LineBorder(new Color(0, 0, 0)));
		floorElevator4.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		GridBagConstraints gbc_floorElevator4 = new GridBagConstraints();
		gbc_floorElevator4.insets = new Insets(0, 0, 5, 0);
		gbc_floorElevator4.gridx = 3;
		gbc_floorElevator4.gridy = 1;
		getContentPane().add(floorElevator4, gbc_floorElevator4);
		
		sfaultElevator1 = new JLabel("SOFT FAULTS : NONE");
		sfaultElevator1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_sfaultElevator1 = new GridBagConstraints();
		gbc_sfaultElevator1.fill = GridBagConstraints.VERTICAL;
		gbc_sfaultElevator1.insets = new Insets(0, 0, 5, 5);
		gbc_sfaultElevator1.gridx = 0;
		gbc_sfaultElevator1.gridy = 3;
		getContentPane().add(sfaultElevator1, gbc_sfaultElevator1);
		
		sfaultElevator2 = new JLabel("SOFT FAULTS : NONE");
		sfaultElevator2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_sfaultElevator2 = new GridBagConstraints();
		gbc_sfaultElevator2.insets = new Insets(0, 0, 5, 5);
		gbc_sfaultElevator2.gridx = 1;
		gbc_sfaultElevator2.gridy = 3;
		getContentPane().add(sfaultElevator2, gbc_sfaultElevator2);
		
		sfaultElevator3 = new JLabel("SOFT FAULTS : NONE");
		sfaultElevator3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_sfaultElevator3 = new GridBagConstraints();
		gbc_sfaultElevator3.insets = new Insets(0, 0, 5, 5);
		gbc_sfaultElevator3.gridx = 2;
		gbc_sfaultElevator3.gridy = 3;
		getContentPane().add(sfaultElevator3, gbc_sfaultElevator3);
		
		sfaultElevator4 = new JLabel("SOFT FAULTS : NONE");
		sfaultElevator4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_sfaultElevator4 = new GridBagConstraints();
		gbc_sfaultElevator4.insets = new Insets(0, 0, 5, 0);
		gbc_sfaultElevator4.gridx = 3;
		gbc_sfaultElevator4.gridy = 3;
		getContentPane().add(sfaultElevator4, gbc_sfaultElevator4);
		
		hfaultElevator1 = new JLabel("HARD FAULTS : NONE");
		hfaultElevator1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_hfaultElevator1 = new GridBagConstraints();
		gbc_hfaultElevator1.insets = new Insets(0, 0, 5, 5);
		gbc_hfaultElevator1.gridx = 0;
		gbc_hfaultElevator1.gridy = 4;
		getContentPane().add(hfaultElevator1, gbc_hfaultElevator1);
		
		hfaultElevator2 = new JLabel("HARD FAULTS : NONE");
		hfaultElevator2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_hfaultElevator2 = new GridBagConstraints();
		gbc_hfaultElevator2.insets = new Insets(0, 0, 5, 5);
		gbc_hfaultElevator2.gridx = 1;
		gbc_hfaultElevator2.gridy = 4;
		getContentPane().add(hfaultElevator2, gbc_hfaultElevator2);
		
		hfaultElevator3 = new JLabel("HARD FAULTS : NONE");
		hfaultElevator3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_hfaultElevator3 = new GridBagConstraints();
		gbc_hfaultElevator3.insets = new Insets(0, 0, 5, 5);
		gbc_hfaultElevator3.gridx = 2;
		gbc_hfaultElevator3.gridy = 4;
		getContentPane().add(hfaultElevator3, gbc_hfaultElevator3);
		
		hfaultElevator4 = new JLabel("HARD FAULTS : NONE");
		hfaultElevator4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_hfaultElevator4 = new GridBagConstraints();
		gbc_hfaultElevator4.insets = new Insets(0, 0, 5, 0);
		gbc_hfaultElevator4.gridx = 3;
		gbc_hfaultElevator4.gridy = 4;
		getContentPane().add(hfaultElevator4, gbc_hfaultElevator4);
		setSize(600,600);
	}
	
	public static void updateElevatorFloors(int e1, int e2, int e3, int e4) {
		floorElevator1.setText("Current Floor: "+ e1);
		floorElevator2.setText("Current Floor: "+ e2);
		floorElevator3.setText("Current Floor: "+ e3);
		floorElevator4.setText("Current Floor: "+ e4);
	}
	
	public static void updateHardFaults(boolean b, boolean c, boolean d, boolean e) {
		hfaultElevator1.setText("HARD FAULTS : " + b);
		hfaultElevator2.setText("HARD FAULTS : " + c);
		hfaultElevator3.setText("HARD FAULTS : " + d);
		hfaultElevator4.setText("HARD FAULTS : " + e);
	}

	public static void updateSoftFaults(boolean a, boolean b, boolean c, boolean d) {
		sfaultElevator1.setText("SOFT FAULTS : " + a);
		sfaultElevator2.setText("SOFT FAULTS : " + b);
		sfaultElevator3.setText("SOFT FAULTS : " + c);
		sfaultElevator4.setText("SOFT FAULTS : " + d);
	}
}
